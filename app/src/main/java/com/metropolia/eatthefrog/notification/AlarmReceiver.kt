package com.metropolia.eatthefrog.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.activities.MainActivity
import com.metropolia.eatthefrog.constants.CHANNEL_ID
import com.metropolia.eatthefrog.database.Task

/**
 * AlarmReceiver is in charge of contents of the notification when the call is made
 * It receives the task which is invoking the notification and uses the title and description of the task as notification title and text.
 * This is the class that gets called at a specific time to invoke the notification.
 */
class AlarmReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null

    override fun onReceive(p0: Context?, p1: Intent?) {
        val task = p1?.getSerializableExtra("task") as? Task

        // FLAG_ACTIVITY_SINGLE_TOP used only when app has only one activity
        val onTap = Intent(p0, MainActivity::class.java)
        onTap.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        // One of the getActivity() flags set as FLAG_IMMUTABLE
        // It is required to have in the latest APIs, so its smart to have already considered for possible later upgrades
        val pendingIntent: PendingIntent =
            getActivity(p0, 0, onTap, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val notification = p0?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentTitle(task?.name)
                .setContentText(task?.description)
                .setSmallIcon(R.drawable.ic_frog_cropped)
                .setAutoCancel(true)                            // Whether the notification disappears onTap or not
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        }

        notificationManager = p0?.let { NotificationManagerCompat.from(it) }
        notification?.let {
            task?.let { item ->
                notificationManager?.notify(
                    item.uid.toInt(),
                    it
                )
            }
        }
    }
}

private fun setAlarm(task: Task, context: Context?) {
    val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra("task", task)
    val pendingIntent = getBroadcast(context, task.uid.toInt(), intent, FLAG_IMMUTABLE)
    val mainActivityIntent = Intent(context, MainActivity::class.java)
    val basicPendingIntent = getActivity(context, task.uid.toInt(), mainActivityIntent, FLAG_IMMUTABLE)
    val clockInfoTest = task.time?.let { AlarmManager.AlarmClockInfo(it.toLong(), basicPendingIntent) }
    alarmManager.setAlarmClock(clockInfoTest, pendingIntent)
}