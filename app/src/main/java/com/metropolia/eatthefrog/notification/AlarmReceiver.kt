package com.metropolia.eatthefrog.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.activities.MainActivity
import com.metropolia.eatthefrog.constants.CHANNEL_ID
import com.metropolia.eatthefrog.database.Task
import java.util.*

/**
 * AlarmReceiver is in charge of contents of the notification when the call is made
 * It receives the task which is invoking the notification and uses the title and description of the task as notification title and text.
 * This is the class that gets called at a specific time to invoke the notification.
 */
class AlarmReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null

    override fun onReceive(p0: Context?, p1: Intent?) {
        val task = p1?.getSerializableExtra("task") as? Task

        // Bold the notification title
        val boldTitle: Spannable = SpannableString(task?.name)
        val sb: Spannable = boldTitle
        task?.name?.length?.let {
            sb.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                it,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // FLAG_ACTIVITY_SINGLE_TOP used only when app has only one activity
        val onTap = Intent(p0, MainActivity::class.java)
        onTap.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        // One of the getActivity() flags set as FLAG_IMMUTABLE
        // It is required to have in the latest APIs, so its smart to have already considered for possible later upgrades
        val pendingIntent: PendingIntent =
            getActivity(p0, 0, onTap, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val notification = p0?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentTitle(boldTitle)
                .setStyle(NotificationCompat.BigTextStyle().bigText(task?.description))
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

/**
 * Function takes task as a parameter to use the tasks uid as a requestCode, which needs to be different for every alarm.
 * The function will launch a notification when prompted and redirects user to MainActivity when the notification is clicked.
 */
fun setAlarm(task: Task, time: String = "09:00", context: Context?) {
    val converter = DateTimeConverter()
    val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra("task", task)

    val pendingIntent = getBroadcast(context, task.uid.toInt(), intent, FLAG_IMMUTABLE)
    val mainActivityIntent = Intent(context, MainActivity::class.java)
    val basicPendingIntent =
        getActivity(context, task.uid.toInt(), mainActivityIntent, FLAG_IMMUTABLE)

    val date: Date = converter.toTimestamp(time)
    val now = Date()
    Log.d("FUU date", date.toString())
    Log.d("FUU date.time", date.time.toString())
    Log.d("FUU now", now.toString())

    if (date > now) {
        val clockInfoTest = AlarmManager.AlarmClockInfo(date.time, basicPendingIntent)
        alarmManager.setAlarmClock(clockInfoTest, pendingIntent)
    }
}


// TODO: Cancelling an alarm if task is completed beforehand
