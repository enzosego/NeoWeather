package com.example.neoweather.data.workers

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.neoweather.data.remote.reverse_geocoding.ReverseGeoCodingApiImpl
import com.example.neoweather.data.remote.reverse_geocoding.model.getName
import com.example.neoweather.data.remote.weather.NeoWeatherApiImpl
import com.example.neoweather.data.workers.NotificationUtils.LATITUDE_PARAM
import com.example.neoweather.data.workers.NotificationUtils.LONGITUDE_PARAM
import com.example.neoweather.data.workers.NotificationUtils.cancelNotifications
import com.example.neoweather.data.workers.NotificationUtils.sendNotification
import com.example.neoweather.domain.use_case.FormatTempUnitUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ShowCurrentWeatherNotificationWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val formatTempUnit: FormatTempUnitUseCase by inject()

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