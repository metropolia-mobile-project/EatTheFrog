package com.metropolia.eatthefrog.screens.addTask

import android.app.Application
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.database.TaskType
import com.metropolia.eatthefrog.screens.addTask.components.AddTaskBuildScreenContainer
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel

lateinit var addTaskScreenViewModel: AddTaskScreenViewModel

/**
 * Actual add task / edit task screen
 * only build screen is called here and gets all the parameters which are given to addTaskScreen
 * when navigated to it.
 *
 * @param application: application context
 * @param navHost: NavHostController of the application.
 * @param editTaskID: ID of the task to be edited
 * @param isEditMode: true, if AddTaskScreen is opened in the edit mode.
 * @param editTitle: title of the Task to be edited.
 * @param editDesc: description of the Task to be edited.
 * @param dateDeadline: deadline of the task
 * @param timeDeadline: time of the deadline.
 * @param editTaskType: TaskType of the Task to be edited.
 * @param isFrogBoolean: isFrog value of the Task.
 */
@OptIn(ExperimentalComposeUiApi::class)
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

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    addTaskScreenViewModel = AddTaskScreenViewModel(application)

    BoxWithConstraints {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    keyboardController?.hide(); focusManager.clearFocus()
                }
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.secondary)

        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomStart)) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(15.dp),
                    textAlign = TextAlign.Start,
                    text = stringResource(R.string.add_task_header),
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




