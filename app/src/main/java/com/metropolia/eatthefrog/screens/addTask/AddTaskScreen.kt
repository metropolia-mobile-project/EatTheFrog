package com.metropolia.eatthefrog.screens.addTask

import android.app.Application
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.metropolia.eatthefrog.database.TaskType
import com.metropolia.eatthefrog.screens.addTask.components.AddTaskBuildScreenContainer
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel

lateinit var addTaskScreenViewModel: AddTaskScreenViewModel

@Composable
fun AddTaskScreen(
    application: Application,
    navHost: NavHostController,
    editTaskID: Long,
    isEditMode: Boolean,
    editTitle: String?,
    editDesc: String?,
    dateDeadline: String,
    timeDeadline: String,
    editTaskType: Long,
    isFrogBoolean: Boolean,
) {


    addTaskScreenViewModel = AddTaskScreenViewModel(application)

    BoxWithConstraints {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.secondary)

        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomStart)) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    textAlign = TextAlign.Start,
                    text = "Add Task",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp)
                AddTaskBuildScreenContainer(
                    viewModel = addTaskScreenViewModel,
                    navHost = navHost,
                    editTaskID,
                    isEditMode,
                    editTitle,
                    editDesc,
                    dateDeadline,
                    timeDeadline,
                    editTaskType,
                    isFrogBoolean,
                )
            }
        }
    }
}




