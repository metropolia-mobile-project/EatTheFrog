package com.metropolia.eatthefrog.screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
    val taski = addTaskViewModel.getStuff(1).observeAsState()
    val subi = addTaskViewModel.getSubtasks(0).observeAsState()

    val task =
        Task(
            0,
            "Testing",
            TaskType.DEVELOPMENT,
            "24-12-2022",
            "12:00",
            isFrog = false,
            1
        )
    val subtask =
        Subtask(
            0,
            5,
            "Sub Testing",
            false
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
            //addTaskViewModel.addSubtask(subtask)
            //addTaskViewModel.addTask(task)
            Log.d("BOOP", taski.value.toString())
            Log.d("BOOP", subi.value.toString())
        }) {
            Text(text = "Create")
        }
    }
}

class AddTaskViewModel(application: Application) : AndroidViewModel(application) {
    private val database = InitialDB.get(application)

    fun getStuff(id: Long): LiveData<Task> = database.taskDao().getSpecificTask(id)
    fun getAllTasks(): LiveData<List<Task>> = database.taskDao().getAllTasks()

    fun getSubtasks(id: Long): LiveData<List<Subtask>> = database.subtaskDao().getSubtaskById(id)

    fun getBoop(id: Long) {
        viewModelScope.launch { database.subtaskDao().getSubtaskById(id) }
    }

    fun addSubtask(subtask: Subtask) {
        viewModelScope.launch { database.subtaskDao().insertSubtask(subtask) }
    }
    fun addTask(task: Task) {
        viewModelScope.launch { database.taskDao().insert(task) }
    }
}