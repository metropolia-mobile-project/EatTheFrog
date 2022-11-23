package com.metropolia.eatthefrog.screens.addTask.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavHostController
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.database.TaskTypeOld
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel
import java.text.SimpleDateFormat
import java.util.*


/**
 * This container holds variables that other containers need as a parameter to build new task object.
 * Every other container is inside this function and 'AddTaskBuildScreenContainer' is called in 'AddTaskScreen'
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddTaskBuildScreenContainer(
    viewModel: AddTaskScreenViewModel,
    navHost: NavHostController,
    editTaskId: Long,
    isEditMode: Boolean,
    editTitle: String?,
    editDesc: String?,
    dateDeadline: String,
    timeDeadline: String,
    editTaskType: String?
) {


    val subList: List<Subtask>
    val subs = viewModel.getHighlightedSubtasks(editTaskId).observeAsState()

    if(subs.value != null && viewModel.editedSubTaskList.value!!.isEmpty()) {
        subList = subs.value!!.toList()
        viewModel.updateEditSubTaskList(subList)
    }

    val taskTypeOldLists = listOf(TaskTypeOld.PLANNING, TaskTypeOld.MEETING, TaskTypeOld.DEVELOPMENT)
    val sdf = SimpleDateFormat(DATE_FORMAT)
    val currentDate = sdf.format(Date())

    var description by remember { mutableStateOf( if(isEditMode) {editDesc} else {""}) }
    var taskTitle by remember { mutableStateOf( if(isEditMode) {editTitle} else {""}) }
    var taskTypeOld: TaskTypeOld by remember { mutableStateOf(taskTypeOldLists[0]) }
    var sDate by remember { mutableStateOf( if(isEditMode) {dateDeadline} else {currentDate}) }
    var sTime by remember { mutableStateOf( if(isEditMode) {timeDeadline} else {"16.00"}) }

    val newTask = Task(0, taskTitle ?: "", description ?: "",
        taskTypeOld, sDate ?: currentDate, sTime, completed = false, isFrog = false)

    val editTask = Task(editTaskId, taskTitle ?: "", description ?: "", taskTypeOld,
        sDate ?: currentDate, sTime, completed = false, isFrog = false)

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .focusRequester(focusRequester)
            .clickable { keyboardController?.hide(); focusManager.clearFocus() }
    ) {

        AddTaskTitleContainer(viewModel,
            taskTitle ?: "",
            onNameChange = { taskTitle = it })

        AddTaskDescAndTypeContainer( description ?: "",
            onDescChange = { description = it },
            onTaskChange = { taskTypeOld = it },
            isEditMode,
            editTaskType,
            viewModel
        )

        AddTaskDateAndTimeContainer(onDateChange = { sDate = it },
            onTimeChange = { sTime = it },
            isEditMode,
            dateDeadline,
            timeDeadline)

        AddTaskLazyColumnContainer(viewModel = viewModel, isEditMode)

        AddTaskCreateSubsContainer(viewModel = viewModel,
            isEditMode,
            editTaskId)

        AddTaskCreateButtonContainer(viewModel = viewModel,
            navHost = navHost,
            newTask,
            isEditMode,
            editTask)
    }
}