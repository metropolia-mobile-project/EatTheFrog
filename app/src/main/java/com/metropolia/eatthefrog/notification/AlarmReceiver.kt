package com.metropolia.eatthefrog.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.activities.MainActivity
import com.metropolia.eatthefrog.constants.CHANNEL_ID
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.constants.TIME_FORMAT
import com.metropolia.eatthefrog.database.Task
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * AlarmReceiver is in charge of contents of the notification when the call is made
 * It receives the task which is invoking the notification and uses the title and description of the task as notification title and text.
 * This is the class that gets called at a specific time to invoke the notification.
 */
@SuppressLint("SimpleDateFormat")
class AlarmReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null
    private val dtf = DateTimeFormatter.ofPattern(DATE_FORMAT)
    private val sdf = SimpleDateFormat(TIME_FORMAT)

    override fun onReceive(p0: Context?, p1: Intent?) {
        val task = p1?.getSerializableExtra("task") as? Task
        val streak = p1?.getSerializableExtra("streak") as? Int
        val today: String = dtf.format(LocalDateTime.now())

        // Bold the notification title
        val boldTitle: Spannable
        val sb: Spannable
        if (task != null) {
            boldTitle = if (task.deadline == today) {
                if (task.isFrog) SpannableString("Today's frog: " + task.name)
                else SpannableString("Due today: " + task.name)
            } else SpannableString("Due tomorrow at " + sdf.format(Date()) + ": " + task.name)
            sb = boldTitle
        } else {
            boldTitle = SpannableString("Streak about to reset")
            sb = boldTitle
        }

        boldTitle.length.let {
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
                .setStyle(
                    NotificationCompat.BigTextStyle().bigText(
                        task?.description
                            ?: ("Streak of $streak days about to be reset. Continue by eating today's frog")
                    )
                )
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
                ?: streak?.let { item ->
                    notificationManager?.notify(
                        item,
                        it
                    )
                }
        }
    }
}
