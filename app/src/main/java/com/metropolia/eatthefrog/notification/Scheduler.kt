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
import java.time.LocalDate
import java.util.*

@Composable
fun Scheduler(viewModel: NotificationsViewModel) {
    val context = LocalContext.current
    val today = viewModel.today
    //val tomorrow = viewModel.tomorrow
    val tomorrow = LocalDate.now().plusDays(1)

    Log.d("HOHHOH today without sdf.format", Date().toString())
    Log.d("HOHHOH tomorrow", tomorrow.toString())

    val tasks = viewModel.getTasks().observeAsState(null)
    //val tasksTomorrow = (tasks.value?.filter { it.deadline == tomorrow })
    val frogToday = (tasks.value?.filter { it.deadline == today })?.filter { it.isFrog }
    var taskItems: List<Task>? = listOf()

    // TODO: Figure out how to get tomorrow out of Date(), LocalDate.now().plusDays(1) doesn't work
    // Invoke notifications for tomorrows tasks
    /*if (tasksTomorrow != null) {
        if (tasksTomorrow.isNotEmpty()) {

            // If only one task due tomorrow
            if (tasksTomorrow.size == 1) {
                val test = tasksTomorrow[0].uid
                testingThis(test, viewModel, context)
            }

            // If only one task due today

            // Notification stuff from now on
            *//*if (tasks.value != null) {
            val context = LocalContext.current
            val id = tasks.value?.uid
            val date = tasks.value?.deadline
            if (id != null) {
                com.metropolia.eatthefrog.screens.testingThis(id, vm, context)
            }
        }*//*
        }
    }*/

    // Invoke notifications for today's frog
    if (frogToday != null) {
        if (frogToday.isNotEmpty()) {
            val frogId = frogToday[0].uid
            testingThis(frogId, viewModel, context)
        }
    }
    /*when(currentDateFilter.value) {
        DateFilter.TODAY -> {
            taskItems = tasks.value?.filter { it.deadline == today }
        }
    }*/
}

fun testingThis(id: Long, viewModel: NotificationsViewModel, context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        val task = viewModel.getCertainTask(id)
        setAlarm(task, context)
    }
}