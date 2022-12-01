package com.metropolia.eatthefrog.notification

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.viewmodels.DateFilter
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel
import com.metropolia.eatthefrog.viewmodels.NotificationsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
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

    Log.d("HOHHOH today without sdf.format", Date().toString())
    Log.d("HOHHOH today formatted", today)
    Log.d("HOHHOH tomorrow", tomorrow)

    val tasks = viewModel.getTasks().observeAsState(null)
    val tasksToday = (tasks.value?.filter { it.deadline == today })
    val tasksTomorrow = (tasks.value?.filter { it.deadline == tomorrow })
    val frogToday = (tasks.value?.filter { it.deadline == today })?.filter { it.isFrog }

    // Invoke notifications for tomorrows tasks
    if (tasksTomorrow != null && tasksTomorrow.isNotEmpty()) {

        // If only one task due tomorrow
        if (tasksTomorrow.size == 1) {
            val task = tasksTomorrow[0].uid
            scheduleNotification(task, viewModel, context)
        }
        // If multiple tasks due tomorrow
        else {
            var task: Long
            for (item in tasksTomorrow) {
                task = item.uid
                scheduleNotification(task, viewModel, context)
            }
        }
    }

    // Invoke notifications for today's tasks
    if (tasksToday != null && tasksToday.isNotEmpty()) {

        // If only one task due today
        if (tasksToday.size == 1) {
            val task = tasksToday[0].uid
            scheduleNotification(task, viewModel, context)
        }
        // If multiple tasks due today
        else {
            var task: Long
            for (item in tasksToday) {
                task = item.uid
                scheduleNotification(task, viewModel, context)
            }
        }
    }

    // Invoke notifications for today's frog
    if (frogToday != null) {
        if (frogToday.isNotEmpty()) {
            val frogId = frogToday[0].uid
            scheduleNotification(frogId, viewModel, context)
        }
    }
}

/**
 * Function responsible for calling setAlarm() and sending the corresponding task for it
 */
fun scheduleNotification(id: Long, viewModel: NotificationsViewModel, context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        val task = viewModel.getCertainTask(id)

        if (task.time != null) setAlarm(task, task.time, context)
        else setAlarm(task = task, context = context)
    }
}
