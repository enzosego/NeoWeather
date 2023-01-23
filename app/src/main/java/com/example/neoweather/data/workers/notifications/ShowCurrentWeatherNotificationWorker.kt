package com.example.neoweather.data.workers.notifications

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.neoweather.data.repository.WeatherDataRepository
import com.example.neoweather.data.workers.location.LOCATION_ID_PARAM
import com.example.neoweather.domain.use_case.FormatTempUnitUseCase

class ShowCurrentWeatherNotificationWorker(
    private val weatherDataRepository: WeatherDataRepository,
    private val formatTempUnit: FormatTempUnitUseCase,
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {

        return try {

            val locationId = inputData.getInt(LOCATION_ID_PARAM, -1)
            if (locationId == -1) {
                Log.d("update_database_worker", "Wrong input")
                return Result.failure()
            }
            val currentWeather = weatherDataRepository.currentWeatherList.value?.get(locationId)
                ?: return Result.failure()

            val location = weatherDataRepository.placesList.value?.get(locationId)
                ?: return Result.failure()

            val addressName = location.name
            val temperature = currentWeather.temperature

            showNotification(addressName, temperature)

            Result.success()
        } catch (e: Exception) {
            Log.d("notification_worker", "${e.printStackTrace()}")
            Result.retry()
        }
    }

    private fun showNotification(addressName: String, temperature: Double) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelNotifications()

        notificationManager.sendNotification(
            addressName,
            formatTempUnit(temperature),
            context
        )
    }
}