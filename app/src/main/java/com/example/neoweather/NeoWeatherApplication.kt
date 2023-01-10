package com.example.neoweather

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.neoweather.data.NeoWeatherDatabase
import com.example.neoweather.repository.NeoWeatherRepository
import com.example.neoweather.workers.NotificationUtils

class NeoWeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    private val database: NeoWeatherDatabase by lazy { NeoWeatherDatabase.getDatabase(this) }

    val repository: NeoWeatherRepository
        get() = ServiceLocator.provideRepository(database)

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationUtils.WEATHER_CHANNEL_ID,
                NotificationUtils.WEATHER_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Weather update!"
            channel.setShowBadge(false)

            val notificationManager = getSystemService(
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}