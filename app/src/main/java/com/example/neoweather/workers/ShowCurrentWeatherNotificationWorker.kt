package com.example.neoweather.workers

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.neoweather.remote.reverse_geocoding.ReverseGeoCodingApiImpl
import com.example.neoweather.remote.reverse_geocoding.model.getName
import com.example.neoweather.remote.weather.NeoWeatherApiImpl
import com.example.neoweather.ui.utils.WeatherUnits.getTempUnit
import com.example.neoweather.workers.NotificationUtils.LATITUDE_PARAM
import com.example.neoweather.workers.NotificationUtils.LONGITUDE_PARAM
import com.example.neoweather.workers.NotificationUtils.cancelNotifications
import com.example.neoweather.workers.NotificationUtils.sendNotification

class ShowCurrentWeatherNotificationWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val latitude = workerParams.inputData.getDouble(LATITUDE_PARAM, 0.0)
        val longitude = workerParams.inputData.getDouble(LONGITUDE_PARAM, 0.0)

        return try {
            val weatherResponse = NeoWeatherApiImpl.create()
                .getCurrentWeather(latitude, longitude)
                .currentWeather

            val addressResponse = ReverseGeoCodingApiImpl.create()
                .getLocationName(latitude, longitude)
                .address

            val temperature = weatherResponse.temperature
            val addressName = addressResponse.getName()

            showNotification(addressName, temperature)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
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
            getTempUnit(temperature, isFahrenheitEnabled = false),
            context
        )
    }
}