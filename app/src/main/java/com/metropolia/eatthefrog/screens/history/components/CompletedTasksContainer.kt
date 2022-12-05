package com.metropolia.eatthefrog.screens.history.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.ALL_UID
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.ui_components.SingleTaskContainer
import com.metropolia.eatthefrog.viewmodels.HistoryScreenViewModel

/**
 * Sorts the completed tasks according to the date values and displays them in the HistoryScreen "Complete" tab.
 */
@Composable
fun CompletedTasksContainer(vm: HistoryScreenViewModel) {

    val tasks = vm.getCompletedTasks().observeAsState(listOf())
    val selectedType = vm.selectedTypes.observeAsState(listOf())
    var taskItems: MutableList<Task>? = mutableListOf()

    for (task in tasks.value) {
        if (selectedType.value.any {it.uid == task.taskTypeId} || selectedType.value.any {it.uid == ALL_UID}) {
            taskItems?.add(task)
        }
    }

    if (taskItems != null) {
        if (taskItems.isNotEmpty()) {
            var firstDate = vm.parseStringToDate(tasks.value[0].deadline)
            var curDate = firstDate

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
            ) {

                itemsIndexed(taskItems) { index, task ->

                    if (index == 0) {
                        Text(task.deadline, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 10.dp))
                        curDate = vm.parseStringToDate(task.deadline)
                    } else if (curDate != null
                        && curDate != vm.parseStringToDate(task.deadline)
                        && vm.parseStringToDate(task.deadline) != firstDate) {
                        Text(task.deadline, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 10.dp))
                        curDate = vm.parseStringToDate(task.deadline)
                    }

                    Row(
                        Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SingleTaskContainer(task, vm)
                    }
                }
            }

        } else {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Image(painter = painterResource(id = R.drawable.ic_add_task), modifier = Modifier
                    .padding(top = 30.dp)
                    .size(100.dp), contentDescription = "plus sign", colorFilter = ColorFilter.tint(
                    MaterialTheme.colors.surface))
                Text(text = stringResource(R.string.no_tasks_found), Modifier.padding(20.dp), color = MaterialTheme.colors.surface, fontSize = 18.sp, textAlign = TextAlign.Center)
            }
        }
    }
}