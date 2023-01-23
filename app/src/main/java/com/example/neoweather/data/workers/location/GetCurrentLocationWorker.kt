package com.example.neoweather.data.workers.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.work.*
import com.example.neoweather.ui.isGpsEnabled
import com.example.neoweather.data.workers.weather.UpdateWeatherInDatabaseWorker
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class GetCurrentLocationWorker(
    private val workManager: WorkManager,
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {


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
            val output = workDataOf(
                LATITUDE_PARAM to currentLocation.latitude,
                LONGITUDE_PARAM to currentLocation.longitude
            )
            enqueueUpdates(output)
            Result.success()
        } else
            Result.retry()
    }

    private fun enqueueUpdates(output: Data) {
        val networkConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val updateLocationRequest =
            OneTimeWorkRequestBuilder<UpdateLocationInDatabaseWorker>()
                .setInputData(output)
                .setConstraints(networkConstraint)
                .build()
        val updateWeatherRequest =
            OneTimeWorkRequestBuilder<UpdateWeatherInDatabaseWorker>()
                .setConstraints(networkConstraint)
                .build()

        workManager.beginUniqueWork(
            UPDATE_LOCATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            updateLocationRequest
        )
            .then(updateWeatherRequest)
            .enqueue()
    }
}