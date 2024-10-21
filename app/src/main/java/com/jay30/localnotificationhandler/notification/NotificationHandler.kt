package com.jay30.localnotificationhandler.notification

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentCallbacks
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.jay30.localnotificationhandler.R
import com.jay30.localnotificationhandler.notificationChannelId
import com.jay30.localnotificationhandler.notificationId

object NotificationHandler {
    fun createIntent(
        context: Context,
        cls: Class<*>,
    ): Intent {
        val intent = Intent(context, cls).apply {
            putExtra("data", "We have something new for you!!")
        }
        return intent
    }

    fun createPendingIntent(
        context: Context,
        requestCode: Int,
        intent: Intent,
        flags: Int,
    ): PendingIntent {
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, requestCode, intent,
            flags,
        )
        return pendingIntent
    }

    fun createNotification(
        context: Context,
        smallIcon: Int,
        contentTitle: String,
        contentText: String,
        priority: Int,
        pendingIntent: PendingIntent,
    ): Notification {
        val notification = NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setPriority(priority)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        return notification
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun sendNotification(
        context: Context,
        notification: Notification,
        permissionState: PermissionState?,
    ) {
        permissionState?.let {
            if (permissionState.status.isGranted) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                NotificationManagerCompat.from(context).notify(notificationId, notification)
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.notification_not_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}