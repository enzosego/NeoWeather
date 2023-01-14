package com.example.neoweather

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.work.Configuration
import com.example.neoweather.data.workers.NotificationUtils
import com.example.neoweather.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NeoWeatherApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NeoWeatherApplication)
            modules(appModule)
        }
        createNotificationChannel()
    }

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

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .build()
}