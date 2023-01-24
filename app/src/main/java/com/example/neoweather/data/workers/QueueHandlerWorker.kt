package com.example.neoweather.data.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.neoweather.data.repository.PlacesRepository
import com.example.neoweather.data.repository.PreferencesRepository
import com.example.neoweather.data.workers.location.*
import com.example.neoweather.data.workers.weather.UPDATE_WEATHER_WORK_NAME
import com.example.neoweather.data.workers.weather.UpdateWeatherInDatabaseWorker
import com.example.neoweather.ui.isBackgroundPermissionGranted

const val QUEUE_HANDLER_WORK_NAME = "handler_worker"

class QueueHandlerWorker(
    private val workManager: WorkManager,
    private val preferencesRepository: PreferencesRepository,
    private val placesRepository: PlacesRepository,
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        val backgroundUpdates = preferencesRepository.dataPreferences.updateInBackground

        Log.d("QueueHandlerWorker", "$backgroundUpdates")
        if (!backgroundUpdates)
            return Result.failure()

        val locationId = preferencesRepository.dataPreferences.preferredLocationId

        val location = placesRepository.placesList[locationId]

        Log.d("QueueHandlerWorker", "$locationId")

        if (location.isGpsLocation && isBackgroundPermissionGranted(context))
            enqueueWork(
                GET_CURRENT_LOCATION_WORK_NAME,
                OneTimeWorkRequestBuilder<GetCurrentLocationWorker>()
            )
        else
            enqueueWork(
                UPDATE_WEATHER_WORK_NAME,
                OneTimeWorkRequestBuilder<UpdateWeatherInDatabaseWorker>()
            )

        return Result.success()
    }

    private fun enqueueWork(workName: String, request: OneTimeWorkRequest.Builder) {
        val networkConstrains =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            request
                .setConstraints(networkConstrains)
                .build()
        )
    }
}