package com.jay30.localnotificationhandler

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.jay30.localnotificationhandler.notification.NotificationChannelHandler
import com.jay30.localnotificationhandler.notification.NotificationHandler
import com.jay30.localnotificationhandler.ui.theme.LocalNotificationHandlerTheme

val notificationId = 1
val notificationChannelId = "my_channel_id"
val notificationChannelName = "my_channel_name"


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalNotificationHandlerTheme {
                val data = intent?.getStringExtra("data") ?: ""
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (data.isEmpty()) {
                        NotificationWithIntentExample()
                    } else {
                        NotificationContent(data)
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationWithIntentExample() {
    val context = LocalContext.current
    val activity = context as Activity
    val locationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    LaunchedEffect(Unit) {
        if (locationPermission != null) {
            if (!locationPermission.status.isGranted) {
                locationPermission.launchPermissionRequest()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(modifier = Modifier.fillMaxWidth(0.7f),
            onClick = {
                val requestCode = 0
                val contentIcon = android.R.drawable.ic_dialog_info
                val contentTitle = "Local Notification"
                val contentText = "Tap to open and see the message."
                val priority = NotificationCompat.PRIORITY_DEFAULT
                val notificationManager: NotificationManager? =
                    NotificationChannelHandler.createNotificationChannel(
                        context,
                        notificationChannelId,
                        notificationChannelName
                    )
                val intent = NotificationHandler.createIntent(
                    context,
                    MainActivity::class.java,
                )
                val paddingIntent = NotificationHandler.createPendingIntent(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
                val notification = NotificationHandler.createNotification(
                    context,
                    contentIcon,
                    contentTitle,
                    contentText,
                    priority,
                    paddingIntent,
                )
                NotificationHandler.sendNotification(
                    context,
                    notification,
                    locationPermission,
                )
            }) {
            Text("Send Notification")
        }
        Button(modifier = Modifier.fillMaxWidth(0.7f),
            onClick = { NotificationManagerCompat.from(context).cancel(notificationId) }) {
            Text(stringResource(R.string.cancel_notification_button_text))
        }
    }
}

@Composable
fun NotificationContent(content: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(text = content, style = MaterialTheme.typography.titleLarge)
    }
}