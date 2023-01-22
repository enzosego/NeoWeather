package com.example.neoweather

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.neoweather.data.workers.QUEUE_HANDLER_WORK_NAME
import com.example.neoweather.data.workers.QUEUE_HANDLER_WORK_TAG
import com.example.neoweather.data.workers.QueueHandlerWorker
import com.example.neoweather.data.workers.notifications.WEATHER_CHANNEL_ID
import com.example.neoweather.data.workers.notifications.WEATHER_CHANNEL_NAME
import com.example.neoweather.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class NeoWeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NeoWeatherApplication)
            modules(appModule)
        }
        createNotificationChannel()
        enqueuePeriodicWork()
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

    private fun enqueuePeriodicWork() {
        val queueHandlerWorker =
            PeriodicWorkRequestBuilder<QueueHandlerWorker>(
                repeatInterval = 1L,
                TimeUnit.HOURS
            )
                .addTag(QUEUE_HANDLER_WORK_TAG)
                .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                QUEUE_HANDLER_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                queueHandlerWorker
            )
    }
}