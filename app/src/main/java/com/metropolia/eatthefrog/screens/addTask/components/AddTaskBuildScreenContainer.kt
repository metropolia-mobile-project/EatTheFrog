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
import com.metropolia.eatthefrog.database.TaskType
import com.metropolia.eatthefrog.screens.addTask.*
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel
import java.text.SimpleDateFormat
import java.util.*


/**
 * This container holds variables that other containers need as a parameter to build new task object.
 * Same screen is also used for editing the existing tasks, if this gets paramater isEditmode = true it will launch as edit mode.
 * Every other container is inside this function and 'AddTaskBuildScreenContainer' is called in 'AddTaskScreen'.
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

    //All variables that are needed all over the screen is created here and passed to other functions
    val subList: List<Subtask>
    val subs = viewModel.getHighlightedSubtasks(editTaskId).observeAsState()

    //Gives existing subtasks to viewmodel so they can be edited in edit mode
    if(subs.value != null && viewModel.editedSubTaskList.value!!.isEmpty()) {
        subList = subs.value!!.toList()
        viewModel.updateEditSubTaskList(subList)
    }

    val taskTypeList = listOf(TaskType.PLANNING, TaskType.MEETING, TaskType.DEVELOPMENT)
    val sdf = SimpleDateFormat(DATE_FORMAT)
    val currentDate = sdf.format(Date())

    var description by remember { mutableStateOf( if(isEditMode) {editDesc} else {""}) }
    var taskTitle by remember { mutableStateOf( if(isEditMode) {editTitle} else {""}) }
    var taskType: TaskType by remember { mutableStateOf(taskTypeList[0]) }
    var sDate by remember { mutableStateOf( if(isEditMode) {dateDeadline} else {currentDate}) }
    var sTime by remember { mutableStateOf( if(isEditMode) {timeDeadline} else {"16.00"}) }
    var isFrog by remember { mutableStateOf(false) }

    val newTask = Task(0, taskTitle ?: "", description ?: "",
        taskType, sDate ?: currentDate, sTime, completed = false, isFrog = isFrog)

    val editTask = Task(editTaskId, taskTitle ?: "", description ?: "", taskType,
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
            onTaskChange = { taskType = it },
            isEditMode,
            editTaskType)

        AddTaskDateAndTimeContainer(
            onDateChange = { sDate = it },
            onTimeChange = { sTime = it },
            isEditMode,
            dateDeadline,
            timeDeadline,
            sDate
        )

        AddTaskLazyColumnContainer(viewModel = viewModel,
            isEditMode,
            sDate,
            isFrog,
            onIsFrogChange = { isFrog = it })

        AddTaskCreateSubsContainer(viewModel = viewModel,
            isEditMode,
            editTaskId)

        AddTaskCreateButtonContainer(viewModel = viewModel,
            navHost = navHost,
            newTask,
            isEditMode,
            editTask,
            onTitleChange = { taskTitle = it },
            onDescChange = { description = it},
            onTaskTypeChange = { taskType = it },
            onFrogChange = { isFrog = it })
    }
}