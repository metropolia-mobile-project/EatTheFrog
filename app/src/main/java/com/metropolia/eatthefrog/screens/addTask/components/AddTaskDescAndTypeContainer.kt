package com.metropolia.eatthefrog.screens.addTask.components

import android.os.Handler
import android.util.Log
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.database.TaskType
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
    onTaskChange: (TaskType) -> Unit,
    isEditMode: Boolean,
    editTaskType: Long,
    viewModel: AddTaskScreenViewModel
) {

    val initialTaskSaved = viewModel.initialTaskSaved.observeAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }
    val taskTypes = viewModel.getTaskTypes().observeAsState()
    val more = TaskType(name = "${stringResource(id = R.string.more)}...", icon = null)
    var items = listOf<TaskType>()
    if (taskTypes.value != null) {
        items = if (taskTypes.value!!.size < 3) taskTypes.value!!.plus(more) else taskTypes.value!!.take(3).plus(more)
    }
    val disabledValue = ""
    val firstTaskType = viewModel.getFirstTaskType().observeAsState()
    val initialTaskType = (
            if (!isEditMode) viewModel.getFirstTaskType()
            else viewModel.getTaskType(editTaskType)
            ).observeAsState(TaskType(name = stringResource(id = R.string.loading), icon = null))

    val selectedTask = viewModel.selectedTaskType.observeAsState(null)

    // Making sure initial task type has been loaded from Room
    if (initialTaskSaved.value == false) {
        Handler().postDelayed({
            if (initialTaskSaved.value != null) {
                onTaskChange(initialTaskType.value)
                viewModel.selectedTaskType.postValue(initialTaskType.value)
            }
            viewModel.initialTaskSaved.postValue(true)
        }, 100)
    }

    Column(Modifier.padding(10.dp)) {
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.description),
            )
            Row {
                // Initial task type can be null here, ignore the warning
                Text(
                    text = if (selectedTask.value?.name != null) selectedTask.value!!.name else if (initialTaskType.value?.name != null) initialTaskType.value.name else firstTaskType.value!!.name,
                    modifier = Modifier
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
                                expanded = false
                                viewModel.selectedTaskType.postValue(s)
                                onTaskChange(s)
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
                        .clickable(onClick = { expanded = true }),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
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
        )
        AddTaskTypeDialog(viewModel = viewModel, onTaskChange = { onTaskChange(it)})
    }
}