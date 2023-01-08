package com.example.neoweather.workers

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.neoweather.ui.isGpsEnabled
import com.example.neoweather.workers.NotificationUtils.LATITUDE_PARAM
import com.example.neoweather.workers.NotificationUtils.LONGITUDE_PARAM
import com.example.neoweather.workers.NotificationUtils.NOTIFICATION_WORK_NAME
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class GetCurrentLocationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result = withContext(Dispatchers.Main) {

        val isLocationPermissionDenied =
            ContextCompat.checkSelfPermission(context, android.Manifest.permission
                .ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
        if (!isGpsEnabled(context) && isLocationPermissionDenied) {
            Log.d("location_worker", "Location services are disabled")
            return@withContext Result.failure()
        }
        Log.d("location_worker", "GOT HERE")

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val cancellationTokenSource = CancellationTokenSource()

        val task = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
        )
        val currentLocation: Location = task.await(cancellationTokenSource)


        if (task.isComplete) {
            Log.d("location_worker", "${currentLocation.latitude} - ${currentLocation.longitude}")
            val inputData = createInputData(currentLocation)
            enqueueWorkers(inputData)
            return@withContext Result.success()
        } else {
            Log.d("location_worker", "Could not get your current location")
            return@withContext Result.retry()
        }
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

        WorkManager.getInstance(context)
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