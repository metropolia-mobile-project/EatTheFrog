package com.metropolia.eatthefrog.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import com.metropolia.eatthefrog.constants.CHANNEL_ID

// Taking context as a parameter as the function is outside of the MainActivity
// Otherwise cannot use the getSystemService()
fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        CHANNEL_ID,
        "ChannelName",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Some description"
    }

    // Register the channel with the system
    val notificationManager =
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}