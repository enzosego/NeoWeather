package com.example.neoweather.data.workers.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.neoweather.R
import com.example.neoweather.ui.MainActivity

const val NOTIFICATION_WORK_NAME = "current_weather_notification_work"

const val WEATHER_CHANNEL_NAME = "Current Weather Update"
const val WEATHER_CHANNEL_ID = "current_weather"
private const val NOTIFICATION_ID = 0

internal fun NotificationManager.sendNotification(
    messageTitle: String,
    messageBody: String,
    applicationContext: Context
) {
    val intent = Intent(applicationContext, MainActivity::class.java)

    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )
    val builder = NotificationCompat.Builder(
        applicationContext,
        WEATHER_CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(messageTitle)
        .setContentText(messageBody)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(NOTIFICATION_ID, builder.build())
}

internal fun NotificationManager.cancelNotifications() {
    cancelAll()
}