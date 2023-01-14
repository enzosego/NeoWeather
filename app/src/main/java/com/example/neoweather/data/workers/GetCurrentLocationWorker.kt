package com.example.neoweather.data.workers

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.work.*
import com.example.neoweather.ui.isGpsEnabled
import com.example.neoweather.data.workers.NotificationUtils.LATITUDE_PARAM
import com.example.neoweather.data.workers.NotificationUtils.LONGITUDE_PARAM
import com.example.neoweather.data.workers.NotificationUtils.NOTIFICATION_WORK_NAME
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCurrentLocationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    //private val workManager: WorkManager by inject()

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result = withContext(Dispatchers.Main) {

        if (!isGpsEnabled(context))
            return@withContext Result.failure()

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val cancellationTokenSource = CancellationTokenSource()

        val task = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
        )
        val currentLocation: Location = task.await(cancellationTokenSource)

        if (task.isComplete) {
            val inputData = createInputData(currentLocation)
            enqueueWorkers(inputData)
            return@withContext Result.success()
        } else
            return@withContext Result.retry()
    }

    private fun enqueueWorkers(inputData: Data) {
        val networkConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val updateDatabaseRequest =
            OneTimeWorkRequestBuilder<UpdateCurrentLocationInDatabaseWorker>()
                .setInputData(inputData)
                .setConstraints(networkConstraint)
                .build()
        val showNotificationRequest =
            OneTimeWorkRequestBuilder<ShowCurrentWeatherNotificationWorker>()
                .setConstraints(networkConstraint)
                .build()

        WorkManager
            .getInstance(context)
            .beginUniqueWork(
                NOTIFICATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                updateDatabaseRequest
            )
            .then(showNotificationRequest)
            .enqueue()
    }

    private fun createInputData(location: Location): Data =
        Data.Builder()
            .putDouble(LATITUDE_PARAM, location.latitude)
            .putDouble(LONGITUDE_PARAM, location.longitude)
            .build()
}