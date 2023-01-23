package com.example.neoweather.data.workers.weather

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.neoweather.data.repository.PreferencesRepository
import com.example.neoweather.data.repository.WeatherDataRepository
import com.example.neoweather.data.workers.location.LOCATION_ID_PARAM
import com.example.neoweather.data.workers.notifications.NOTIFICATION_WORK_NAME
import com.example.neoweather.data.workers.notifications.ShowCurrentWeatherNotificationWorker
import java.io.IOException

const val UPDATE_WEATHER_WORK_NAME = "update_weather_work_name"

class UpdateWeatherInDatabaseWorker(
    private val workManager: WorkManager,
    private val weatherDataRepository: WeatherDataRepository,
    private val preferencesRepository: PreferencesRepository,
    context: Context,
    workerParams: WorkerParameters

) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val locationId = preferencesRepository.dataPreferences.value?.preferredLocationId
            ?: 0

        Log.d("UpdateWeatherWorker", "$locationId")
        return try {
            weatherDataRepository.refreshPlaceWeather(locationId)

            val output = workDataOf(
                LOCATION_ID_PARAM to locationId
            )
            enqueueNotificationRequest(output)

            Result.success()
        } catch (e: IOException) {
            Log.d("UpdateWeatherWorker", "$e")
            Result.retry()
        }
    }

    private fun enqueueNotificationRequest(outputData: Data) {
        val networkConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val showNotificationRequest =
            OneTimeWorkRequestBuilder<ShowCurrentWeatherNotificationWorker>()
                .setConstraints(networkConstraint)
                .setInputData(outputData)
                .build()

        workManager.enqueueUniqueWork(
            NOTIFICATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            showNotificationRequest
        )
    }
}