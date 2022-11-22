package com.metropolia.eatthefrog.screens.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.viewmodels.HistoryScreenViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun IncompleteTasksContainer(vm: HistoryScreenViewModel) {

    val tasks = vm.getIncompleteTasks().observeAsState(listOf())
    val selectedType = vm.selectedTypes.observeAsState(listOf())
    val all = stringResource(id = R.string.all)
    fun parseStringToDate(string: String) = SimpleDateFormat(DATE_FORMAT).parse(string)

    if (tasks.value.isNotEmpty()) {

        var curDate = parseStringToDate(tasks.value[0].deadline)

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {

            itemsIndexed(tasks.value) { index, task ->

                if (selectedType.value.contains(task.taskType.name) || selectedType.value.contains(all)) {

                    val before = Calendar.getInstance()
                    before.add(Calendar.DATE, -1)

                    if (parseStringToDate(task.deadline).before(before.time)) {
                        if (curDate != null && curDate != parseStringToDate(task.deadline)) {
                            Text(
                                task.deadline,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            curDate = parseStringToDate(task.deadline)
                        }

                        Row(
                            Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HistoryScreenTaskContainer(task, vm)
                        }
                    }
                }
            }
        }
    }
}