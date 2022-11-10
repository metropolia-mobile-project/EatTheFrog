package com.metropolia.eatthefrog.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import com.metropolia.eatthefrog.constants.CHANNEL_ID

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "ChannelName",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Some description"
        }

        // Register the channel with the system
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}