package com.example.neoweather.workers

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.neoweather.R

object NotificationUtils {
    const val LATITUDE_PARAM = "latitude"
    const val LONGITUDE_PARAM = "longitude"
    const val NOTIFICATION_WORK_NAME = "current_weather_notification_work"

    const val WEATHER_CHANNEL_NAME = "Current Weather Update"
    const val WEATHER_CHANNEL_ID = "current_weather"
    private const val NOTIFICATION_ID = 0

    internal fun NotificationManager.sendNotification(
        messageTitle: String,
        messageBody: String,
        applicationContext: Context
    ) {
        val builder = NotificationCompat.Builder(
            applicationContext,
            WEATHER_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notify(NOTIFICATION_ID, builder.build())
    }

    internal fun NotificationManager.cancelNotifications() {
        cancelAll()
    }
}