package com.metropolia.eatthefrog.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.metropolia.eatthefrog.activities.MainActivity
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.viewmodels.NotificationsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.*

/**
 * Function responsible for observing tasks deadlines.
 * Sends frogs and tasks due today/tomorrow for scheduleNotification() function when detects upcoming due date.
 */
@Composable
fun Scheduler(viewModel: NotificationsViewModel) {
    val context = LocalContext.current
    val today = viewModel.today
    val tomorrow = viewModel.tomorrow
    val options = viewModel.listItems
    val index = viewModel.deadlineValue.observeAsState()

    val tasks = viewModel.getTasks().observeAsState(null)
    val tasksToday = (tasks.value?.filter { it.deadline == today })
    val tasksTomorrow = (tasks.value?.filter { it.deadline == tomorrow })
    val frogToday = (tasks.value?.filter { it.deadline == today })?.filter { it.isFrog }

    val streak = viewModel.currentStreak.observeAsState(null)
    val latestDate = viewModel.latestDate
    val todayDate = viewModel.todayDate

    // Invoke notifications for tomorrows tasks
    if (tasksTomorrow != null && tasksTomorrow.isNotEmpty()) {
        var task: Long
        for (item in tasksTomorrow) {
            if (!item.completed) {
                task = item.uid
                scheduleNotification(task, options[index.value ?: 0], viewModel, context)
            }
        }
    }

    // Invoke notifications for today's tasks
    if (tasksToday != null && tasksToday.isNotEmpty()) {
        var task: Long
        for (item in tasksToday) {
            if (!item.completed) {
                task = item.uid
                scheduleNotification(task, options[index.value ?: 0], viewModel, context)
            }
        }
    }

    // Invoke notifications for today's frog
    if (frogToday != null) {
        if (frogToday.isNotEmpty() && !frogToday[0].completed) {
            val frogId = frogToday[0].uid
            scheduleNotification(frogId, options[index.value ?: 0], viewModel, context)
        }
    }

    // Invoke notification for current streak to be about to reset
    val durationBetweenDates = Duration.between(latestDate, todayDate).toDays()
    if (durationBetweenDates.toInt() == 1) {
        return
    }

    // Cancel notification for current streak when today's frog is eaten
    if (durationBetweenDates.toInt() == 0) {
        return
    }
}

/**
 * Function responsible for calling setAlarm() and sending the corresponding task for it
 * Needed as accessing to the task from database has to happen in CoroutineScope, outside of the main thread
 */
fun scheduleNotification(
    id: Long,
    option: String,
    viewModel: NotificationsViewModel,
    context: Context
) {
    CoroutineScope(Dispatchers.IO).launch {
        val converter = DateTimeConverter()
        val task = viewModel.getCertainTask(id)
        val minutes = viewModel.minutes
        val hours = viewModel.hours

        if (task.time != null) {
            if (option in hours) {
                val modifiedTime = converter.modifyTime(task.time, hours = hours[option]!!)
                setAlarmForTask(task, modifiedTime.toString(), context)
            }
            if (option in minutes) {
                val modifiedTime = converter.modifyTime(task.time, minutes = minutes[option]!!)
                setAlarmForTask(task, modifiedTime.toString(), context)
            }
            setAlarmForTask(task, task.time, context)
        } else setAlarmForTask(task = task, context = context)
    }
}

/**
 * Function takes task as a parameter to use the tasks uid as a requestCode, which needs to be different for every alarm.
 * The function will launch a notification when prompted and redirects user to MainActivity when the notification is clicked.
 */
fun setAlarmForTask(task: Task, time: String = "09:00", context: Context?) {
    val converter = DateTimeConverter()
    val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra("task", task)

    val pendingIntent =
        PendingIntent.getBroadcast(context, task.uid.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)
    val mainActivityIntent = Intent(context, MainActivity::class.java)
    val basicPendingIntent = PendingIntent.getActivity(
        context,
        task.uid.toInt(),
        mainActivityIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val date: Date = converter.toTimestamp(time)
    val now = Date()
    Log.d("FUU date", date.toString())
    Log.d("FUU date.time", date.time.toString())
    Log.d("FUU now", now.toString())

    if (date > now) {
        val clockInfo = AlarmManager.AlarmClockInfo(date.time, basicPendingIntent)
        alarmManager.setAlarmClock(clockInfo, pendingIntent)
    }
}

/**
 * Function takes current streak as a parameter to use it as a requestCode, which needs to be different for every alarm.
 * The function will launch a notification when prompted and redirects user to MainActivity when the notification is clicked.
 */
fun setAlarmForStreak(streak: Int, context: Context?) {
    streak.plus(666)
    Log.d("BLOOP streak: ", streak.toString())
    val converter = DateTimeConverter()
    val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra("streak", streak)

    val pendingIntent = PendingIntent.getBroadcast(context, streak, intent, PendingIntent.FLAG_IMMUTABLE)
    val mainActivityIntent = Intent(context, MainActivity::class.java)
    val basicPendingIntent = PendingIntent.getActivity(
        context,
        streak,
        mainActivityIntent,
        PendingIntent.FLAG_IMMUTABLE
    )
    val date: Date = converter.toTimestamp("20:00")
    val clockInfo = AlarmManager.AlarmClockInfo(date.time, basicPendingIntent)
    alarmManager.setAlarmClock(clockInfo, pendingIntent)
}

/**
 * Function takes task as a parameter to use the tasks uid as a requestCode, which needs to be different for every alarm.
 * The function will cancel a notification when prompted, which is when a task or a frog is marked completed.
 */
fun cancelAlarmForTask(task: Task, context: Context?) {
    val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(context, task.uid.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)
    alarmManager.cancel(pendingIntent)
}

fun cancelAlarmForStreak(streak: Int, context: Context?) {
    streak.plus(666)
    val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, streak, intent, PendingIntent.FLAG_IMMUTABLE)
    alarmManager.cancel(pendingIntent)
}