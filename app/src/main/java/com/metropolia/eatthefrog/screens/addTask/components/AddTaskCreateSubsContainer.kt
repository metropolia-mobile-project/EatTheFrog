package com.metropolia.eatthefrog.screens.addTask.components

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.MAX_SUBTASK_AMOUNT
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel
import kotlinx.coroutines.launch

/**
 * UI and functionality to add new subtasks to subtask list.
 * subtasks are shown in 'AddTaskLazyColumnContainer' before they are saved to database
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun AddTaskCreateSubsContainer(viewModel: AddTaskScreenViewModel) {

    val lastTask = viewModel.getLastTask().observeAsState()
    val subList = viewModel.subTaskList.observeAsState()
    var subTaskTitle by remember { mutableStateOf("") }
    var subTaskId: Long by remember { mutableStateOf(0) }
    val subTaskDone by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(30.dp, 30.dp, 0.dp, 0.dp)
            .wrapContentWidth()
            .bringIntoViewRequester(bringIntoViewRequester)
    ) {
        Text(
            text = stringResource(id = R.string.sub_task_title)
        )
        Row() {
            TextField(
                value = subTaskTitle,
                onValueChange = { subTaskTitle = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),

                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Start
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide(); focusManager.clearFocus() }),
                modifier = Modifier
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
                    .width(250.dp)
                    .padding(0.dp, 0.dp, 30.dp, 15.dp),
                trailingIcon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                subTaskId = if (lastTask.value == null) {
                                    1
                                } else lastTask.value!!.uid + 1
                                val list =
                                    listOf(Subtask(0, subTaskId, subTaskTitle, subTaskDone))
                                if (subList.value!!.size < MAX_SUBTASK_AMOUNT && subTaskTitle != "") {
                                    viewModel.updateSubTaskList(list); subTaskTitle = ""
                                } else if (subTaskTitle == "") {
                                    Toast.makeText(
                                        context,
                                        "Subtask must have title",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Maximum amount of subtasks reached",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            })
                }
            )
        }
    }
}


