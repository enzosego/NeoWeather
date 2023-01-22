package com.example.neoweather.data.workers.weather

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.neoweather.data.repository.WeatherDataRepository
import com.example.neoweather.data.workers.location.LOCATION_ID_PARAM
import com.example.neoweather.data.workers.notifications.NOTIFICATION_WORK_NAME
import com.example.neoweather.data.workers.notifications.ShowCurrentWeatherNotificationWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateWeatherInDatabaseWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val weatherDataRepository: WeatherDataRepository by inject()

    override suspend fun doWork(): Result {

        val locationId = inputData.getInt(LOCATION_ID_PARAM, -1)
        if (locationId == -1) {
            Log.d("update_database_worker", "Wrong input")
            return Result.failure()
        }
        weatherDataRepository.refreshPlaceWeather(locationId)

        val outputData = workDataOf(
            LOCATION_ID_PARAM to locationId
        )
        enqueueNotificationRequest(outputData)

        return Result.success()
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

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                NOTIFICATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                showNotificationRequest
            )
    }
}