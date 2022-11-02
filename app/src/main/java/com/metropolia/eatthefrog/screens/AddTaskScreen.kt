package com.metropolia.eatthefrog.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.database.TaskType
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Composable
fun AddTaskScreen(application: Application) {
    val addTaskViewModel = AddTaskViewModel(application)
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val stf = SimpleDateFormat("HH:mm")
    /*val subtasks = listOf(
        Subtask("Subtask 1", false),
        Subtask("Subtask 2", false),
        Subtask("Subtask 3", false),
        Subtask("Subtask 4", false),
    )*/
    val task =
        Task(
            0,
            "Testing",
            //subtasks,
            TaskType.MEETING,
            "24-12-2022",
            "12:00",
            isFrog = false
        )

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Add Task View",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
        Button(onClick = {
            addTaskViewModel.addTask(task)
        }) {
            Text(text = "Create")
        }
    }
}

class AddTaskViewModel(application: Application) : AndroidViewModel(application) {
    private val database = InitialDB.get(application)

    fun addTask(task: Task) {
        viewModelScope.launch { database.taskDao().insert(task) }
    }
}