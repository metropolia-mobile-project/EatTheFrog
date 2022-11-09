package com.metropolia.eatthefrog.screens.addTask.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavHostController
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.database.TaskType
import com.metropolia.eatthefrog.screens.addTask.*
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel
import java.text.SimpleDateFormat
import java.util.*


/**
 * This container holds variables that other containers need as a parameter to build new task object.
 * Every other container is inside this function and 'AddTaskBuildScreenContainer' is called in 'AddTaskScreen'
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddTaskBuildScreenContainer(viewModel: AddTaskScreenViewModel, navHost: NavHostController) {

    val taskTypeList = listOf(TaskType.PLANNING, TaskType.MEETING, TaskType.DEVELOPMENT)
    val sdf = SimpleDateFormat(DATE_FORMAT)
    val currentDate = sdf.format(Date())
    var description by remember { mutableStateOf("") }
    var taskTitle by remember { mutableStateOf("") }
    var taskType: TaskType by remember { mutableStateOf(taskTypeList[0]) }
    var sDate by remember { mutableStateOf(currentDate) }
    var sTime by remember { mutableStateOf("16.00") }
    val newTask =
        Task(0, taskTitle, description, taskType, sDate, sTime, completed = false, isFrog = false)

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .focusRequester(focusRequester)
            .clickable { keyboardController?.hide(); focusManager.clearFocus() }
    ) {

        AddTaskTitleContainer(viewModel, taskTitle, onNameChange = { taskTitle = it })
        AddTaskDescAndTypeContainer(viewModel = viewModel, description, onDescChange = { description = it }, onTaskChange = { taskType = it })
        AddTaskDateAndTimeContainer(onDateChange = { sDate = it }, onTimeChange = { sTime = it })
        AddTaskLazyColumnContainer(viewModel = viewModel)
        AddTaskCreateSubsContainer(viewModel = viewModel)
        AddTaskCreateButtonContainer(viewModel = viewModel, navHost = navHost, newTask)
    }
}