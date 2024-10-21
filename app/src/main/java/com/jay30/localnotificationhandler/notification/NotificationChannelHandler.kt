package com.jay30.localnotificationhandler.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannelHandler{
    fun createNotificationChannel(
        context: Context,
        notificationChannelId: String,
        notificationChannelName: String,
    ):  NotificationManager?{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(notificationChannelId, notificationChannelName, importance)
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            return notificationManager
        }
        return null
    }
}