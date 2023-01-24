package com.example.neoweather.data.workers.notifications

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.neoweather.data.repository.PlacesRepository
import com.example.neoweather.data.repository.WeatherRepository
import com.example.neoweather.data.workers.location.LOCATION_ID_PARAM
import com.example.neoweather.domain.use_case.FormatTempUnitUseCase

class ShowCurrentWeatherNotificationWorker(
    private val weatherRepository: WeatherRepository,
    private val placesRepository: PlacesRepository,
    private val formatTempUnit: FormatTempUnitUseCase,
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        return try {

            val locationId = inputData.getInt(LOCATION_ID_PARAM, -1)
            if (locationId == -1)
                return Result.failure()

            val addressName = placesRepository.placesList[locationId].name
            val temperature = weatherRepository.currentWeatherList[locationId].temperature

            showNotification(addressName, temperature)

            Result.success()
        } catch (e: Exception) {
            Log.d("ShowNotificationWorker", "$e")
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