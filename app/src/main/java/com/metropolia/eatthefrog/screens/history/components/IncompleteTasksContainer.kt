package com.metropolia.eatthefrog.screens.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.viewmodels.HistoryScreenViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun IncompleteTasksContainer(vm: HistoryScreenViewModel) {

    val tasks = vm.getIncompleteTasks().observeAsState(listOf())

    fun parseStringToDate(string: String) = SimpleDateFormat(DATE_FORMAT).parse(string)

    if (tasks.value.isNotEmpty()) {

        var curDate = parseStringToDate(tasks.value[0].deadline)

        LazyColumn(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {

            itemsIndexed(tasks.value) { index, task ->

                if (parseStringToDate(task.deadline) < Calendar.getInstance().time) {
                    if (curDate != null && curDate != parseStringToDate(task.deadline)) {
                        Text(task.deadline, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 10.dp))
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