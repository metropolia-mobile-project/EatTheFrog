package com.metropolia.eatthefrog.screens.addTask.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.database.TaskType
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel
import java.text.SimpleDateFormat
import com.metropolia.eatthefrog.R
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
    editTaskType: Long,
    isFrogBoolean: Boolean
) {


    val subList: List<Subtask>
    val subs = viewModel.getHighlightedSubtasks(editTaskId).observeAsState()
    val taskTypes = viewModel.getTaskTypes().observeAsState(
        listOf(
            TaskType(
                uid = 1000,
                name = stringResource(R.string.loading),
                icon = null
            )
        )
    )

    if (subs.value != null && viewModel.editedSubTaskList.value!!.isEmpty()) {
        subList = subs.value!!.toList()
        viewModel.updateEditSubTaskList(subList)
    }

    val sdf = SimpleDateFormat(DATE_FORMAT)
    val currentDate = sdf.format(Date())

    var description by remember {
        mutableStateOf(
            if (isEditMode) {
                editDesc
            } else {
                ""
            }
        )
    }
    var taskTitle by remember {
        mutableStateOf(
            if (isEditMode) {
                editTitle
            } else {
                ""
            }
        )
    }
    val observeEditTaskType = viewModel.getTaskType(editTaskType).observeAsState(taskTypes.value[0])
    var taskType: TaskType by remember { mutableStateOf(if (!isEditMode) taskTypes.value[0] else observeEditTaskType.value) }
    var sDate by remember {
        mutableStateOf(
            if (isEditMode) {
                dateDeadline
            } else {
                currentDate
            }
        )
    }
    var sTime by remember {
        mutableStateOf(
            if (isEditMode) {
                timeDeadline
            } else {
                "16.00"
            }
        )
    }
    var isFrog by remember { mutableStateOf(false) }

    val newTask = Task(
        0, taskTitle ?: "", description ?: "",
        taskType.uid, sDate ?: currentDate, sTime, completed = false, isFrog = false
    )

    val editTask = Task(
        editTaskId, taskTitle ?: "", description ?: "", taskType.uid,
        sDate ?: currentDate, sTime, completed = false, isFrog = false
    )

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .fillMaxSize()
            .focusRequester(focusRequester),
        verticalArrangement = Arrangement.Bottom

    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .focusRequester(focusRequester)
        ) {

            AddTaskContentCardWrapper {
                AddTaskTitleContainer(viewModel,
                    taskTitle ?: "",
                    onNameChange = { taskTitle = it })
            }

            AddTaskContentCardWrapper {
                AddTaskDescAndTypeContainer(
                    description ?: "",
                    onDescChange = { description = it },
                    onTaskChange = { taskType = it },
                    isEditMode,
                    editTaskType,
                    viewModel
                )
            }

            AddTaskContentCardWrapper {
                AddTaskDateAndTimeContainer(
                    onDateChange = { sDate = it },
                    onTimeChange = { sTime = it },
                    isEditMode,
                    dateDeadline,
                    timeDeadline,
                    sDate
                )
            }

            AddTaskContentCardWrapper {
                Column(Modifier.fillMaxWidth().padding(10.dp)) {
                    AddTaskLazyColumnContainer(viewModel = viewModel,
                        isEditMode,
                        sDate,
                        isFrog,
                        onIsFrogChange = { isFrog = it })
                    AddTaskCreateSubsContainer(
                        viewModel = viewModel,
                        isEditMode,
                        editTaskId
                    )
                    AddTaskCreateButtonContainer(viewModel = viewModel,
                        navHost = navHost,
                        newTask,
                        isEditMode,
                        editTask,
                        onTitleChange = { taskTitle = it },
                        onDescChange = { description = it },
                        onTaskTypeChange = { taskType = it },
                        onFrogChange = { isFrog = it })
                }
            }
        }
    }
}

/**
 * Wraps the AddTask composables in a Card composable
 */
@Composable
fun AddTaskContentCardWrapper(
    modifier: Modifier =
        Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp)),

    content: @Composable () -> Unit) {
    Card(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 25.dp
    ) {
        content()
    }
}