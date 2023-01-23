package com.example.neoweather.data.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.neoweather.data.repository.PreferencesRepository
import com.example.neoweather.data.repository.WeatherDataRepository
import com.example.neoweather.data.workers.location.*
import com.example.neoweather.data.workers.weather.UPDATE_WEATHER_WORK_NAME
import com.example.neoweather.data.workers.weather.UpdateWeatherInDatabaseWorker
import com.example.neoweather.ui.isBackgroundPermissionGranted

const val QUEUE_HANDLER_WORK_NAME = "handler_worker"

class QueueHandlerWorker(
    private val workManager: WorkManager,
    private val preferencesRepository: PreferencesRepository,
    private val weatherDataRepository: WeatherDataRepository,
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val updateInBackground = preferencesRepository.dataPreferences.value?.updateInBackground
        Log.d("QueueHandlerWorker", "$updateInBackground")
        if (updateInBackground != true)
            return Result.failure()

        val preferredLocationId =
            preferencesRepository.dataPreferences.value?.preferredLocationId ?: 0

        val preferredLocation =
            weatherDataRepository.placesList.value?.getOrNull(preferredLocationId)
                ?: return Result.failure()

        if (preferredLocation.isGpsLocation && isBackgroundPermissionGranted(context))
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