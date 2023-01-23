package com.example.neoweather

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.neoweather.data.workers.notifications.WEATHER_CHANNEL_ID
import com.example.neoweather.data.workers.notifications.WEATHER_CHANNEL_NAME
import com.example.neoweather.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class NeoWeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NeoWeatherApplication)
            workManagerFactory()
            modules(appModule)
        }
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                WEATHER_CHANNEL_ID,
                WEATHER_CHANNEL_NAME,
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