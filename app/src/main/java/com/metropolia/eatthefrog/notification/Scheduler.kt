package com.metropolia.eatthefrog.notification

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.metropolia.eatthefrog.viewmodels.NotificationsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    Log.d("HOHHOH today without sdf.format", Date().toString())
    Log.d("HOHHOH today formatted", today)
    Log.d("HOHHOH tomorrow", tomorrow)

    val tasks = viewModel.getTasks().observeAsState(null)
    val tasksToday = (tasks.value?.filter { it.deadline == today })
    val tasksTomorrow = (tasks.value?.filter { it.deadline == tomorrow })
    val frogToday = (tasks.value?.filter { it.deadline == today })?.filter { it.isFrog }

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
}

/**
 * Function responsible for calling setAlarm() and sending the corresponding task for it
 */
fun scheduleNotification(id: Long, option: String, viewModel: NotificationsViewModel, context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        val converter = DateTimeConverter()
        val task = viewModel.getCertainTask(id)
        val minutes = viewModel.minutes
        val hours = viewModel.hours

        if (task.time != null) {
            if (option in hours) {
                val modifiedTime = converter.modifyTime(task.time, hours = hours[option]!!)
                setAlarm(task, modifiedTime.toString(), context)
            }
            if (option in minutes) {
                val modifiedTime = converter.modifyTime(task.time, minutes = minutes[option]!!)
                setAlarm(task, modifiedTime.toString(), context)
            }
            setAlarm(task, task.time, context)
        }
        else setAlarm(task = task, context = context)
    }
}
