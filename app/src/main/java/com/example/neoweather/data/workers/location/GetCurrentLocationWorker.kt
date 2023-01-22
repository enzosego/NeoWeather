package com.example.neoweather.data.workers.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.work.*
import com.example.neoweather.ui.isGpsEnabled
import com.example.neoweather.data.workers.notifications.ShowCurrentWeatherNotificationWorker
import com.example.neoweather.data.workers.weather.UpdateWeatherInDatabaseWorker
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent

class GetCurrentLocationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

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

        return@withContext if (task.isComplete) {
            val inputData = createInputData(currentLocation)
            enqueueWorkers(inputData)
            Result.success()
        } else
            Result.retry()
    }

    private fun enqueueWorkers(inputData: Data) {
        val networkConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val updateLocationRequest =
            OneTimeWorkRequestBuilder<UpdateLocationInDatabaseWorker>()
                .setInputData(inputData)
                .setConstraints(networkConstraint)
                .build()
        val updateWeatherRequest =
            OneTimeWorkRequestBuilder<UpdateWeatherInDatabaseWorker>()
                .setConstraints(networkConstraint)
                .build()
        val showNotificationRequest =
            OneTimeWorkRequestBuilder<ShowCurrentWeatherNotificationWorker>()
                .setConstraints(networkConstraint)
                .build()

        WorkManager
            .getInstance(context)
            .beginUniqueWork(
                UPDATE_LOCATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                updateLocationRequest
            )
            .then(updateWeatherRequest)
            .then(showNotificationRequest)
            .enqueue()
    }

    private fun createInputData(location: Location): Data =
        Data.Builder()
            .putDouble(LATITUDE_PARAM, location.latitude)
            .putDouble(LONGITUDE_PARAM, location.longitude)
            .build()
}