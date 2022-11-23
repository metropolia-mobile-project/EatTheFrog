package com.metropolia.eatthefrog.screens.addTask.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.database.TaskType
import com.metropolia.eatthefrog.database.TaskTypeOld
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel

/**
 * Description title and TextField
 * Dropdown menu for picking task type
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddTaskDescAndTypeContainer(
    description: String,
    onDescChange: (String) -> Unit,
    onTaskChange: (TaskTypeOld) -> Unit,
    isEditMode: Boolean,
    editTaskType: String?,
    viewModel: AddTaskScreenViewModel
) {

    val editTaskTypeIndex = when (editTaskType) {
        "PLANNING" -> 0
        "MEETING" -> 1
        "DEVELOPMENT" -> 2
        else -> {
            0
        }
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }
    val taskTypes = viewModel.getTaskTypes().observeAsState(listOf())
    val more = TaskType(name = "${stringResource(id = R.string.more)}...", icon = null)
    val items = if (taskTypes.value.size < 3) taskTypes.value.plus(more) else taskTypes.value.take(3).plus(more)
    var selectedIndex by remember {
        mutableStateOf(
            if (isEditMode) {
                editTaskTypeIndex
            } else {
                0
            }
        )
    }
    val disabledValue = ""
    val taskTypeOldLists = listOf(TaskTypeOld.PLANNING, TaskTypeOld.MEETING, TaskTypeOld.DEVELOPMENT)
    var taskType by remember { mutableStateOf(taskTypeOldLists[0]) }

    onTaskChange(taskType)

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(40.dp, 30.dp, 0.dp, 0.dp),

        ) {
        Row() {
            Text(
                text = stringResource(id = R.string.description)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopStart)
                    .padding(start = 110.dp, end = 30.dp)
            ) {
                Text(
                    items[selectedIndex].name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { expanded = true })
                        .background(
                            Color.Transparent
                        )
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .wrapContentWidth()
                        .background(
                            MaterialTheme.colors.primary
                        )
                ) {
                    items.forEachIndexed { index, s ->
                        DropdownMenuItem(onClick = {
                            if (index != items.size -1) {
                                selectedIndex = index
                                expanded = false
                                taskType = taskTypeOldLists[selectedIndex]
                            } else {
                                expanded = false
                                viewModel.typeDialogVisible.postValue(true)
                            }
                        }) {
                            val disabledText = if (s.name == disabledValue) {
                                " (Disabled)"
                            } else {
                                ""
                            }
                            Text(text = s.name + disabledText)
                        }
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_dropdownarrow),
                    contentDescription = "",
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                        .align(Alignment.CenterEnd)
                )
            }
        }

        TextField(
            value = description,
            onValueChange = onDescChange,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide(); focusManager.clearFocus()
            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 30.dp, 15.dp)
        )
        AddTaskTypeDialog(viewModel = viewModel)
    }
}