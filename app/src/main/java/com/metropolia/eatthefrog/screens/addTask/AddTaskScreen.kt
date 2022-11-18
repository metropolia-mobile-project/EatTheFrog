package com.metropolia.eatthefrog.screens.addTask

import android.app.Application
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
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
) {


    addTaskScreenViewModel = AddTaskScreenViewModel(application)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        AddTaskBuildScreenContainer(viewModel = addTaskScreenViewModel, navHost = navHost, editTaskID, isEditMode, editTitle, editDesc, dateDeadline, timeDeadline)
    }
}




