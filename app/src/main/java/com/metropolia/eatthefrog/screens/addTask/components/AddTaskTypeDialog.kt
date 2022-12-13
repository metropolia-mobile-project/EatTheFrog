package com.metropolia.eatthefrog.screens.addTask.components

import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.metropolia.eatthefrog.ui_components.PopupView
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.ICON_LIST
import com.metropolia.eatthefrog.constants.MAX_TASK_TYPE_NAME_LENGTH
import com.metropolia.eatthefrog.database.TaskType

/**
 * Dialog box for adding and selecting task types. When launching the application for the
 * first time, three default types are created already. User can add and delete types here as they wish.
 * Task types have a name and a icon that the user selects from a scrollable bar. One task type has to always
 * exist, so the last type in the list cannot be deleted.
 *
 * @param viewModel: AddTaskScreenViewModel of the parent composable.
 * @param onTaskChange: function to be called when Task is changed.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddTaskTypeDialog(
    viewModel: AddTaskScreenViewModel,
    onTaskChange: (TaskType) -> Unit
) {
    val context = LocalContext.current
    val visible = viewModel.typeDialogVisible.observeAsState()
    val taskTypes = viewModel.getTaskTypes().observeAsState(listOf())
    val firstTaskType = viewModel.getFirstTaskType().observeAsState()
    var typeInput by remember { mutableStateOf("") }
    var chosenIcon by remember { mutableStateOf(ICON_LIST[0]) }

    if (visible.value == true) {
        Dialog(onDismissRequest = { viewModel.typeDialogVisible.postValue(false) }) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.surface)
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.choose_task_type),
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

                LazyColumn(Modifier.fillMaxHeight(0.5f)) {
                    items(taskTypes.value) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                elevation = 5.dp,
                                onClick = {
                                    viewModel.selectedTaskType.postValue(item)
                                    onTaskChange(item)
                                    viewModel.typeDialogVisible.postValue(false)
                                }
                            ) {
                                Row(modifier = Modifier
                                    .padding(5.dp),
                                ) {
                                    Image(
                                        painter = painterResource(
                                            id = item.icon ?: R.drawable.ic_null
                                        ),
                                        contentDescription = item.name,
                                        modifier = Modifier.padding(end = 5.dp)
                                    )
                                    Text(text = item.name)
                                }
                            }
                            Icon(
                                painterResource(id = R.drawable.ic_add),
                                contentDescription = "Delete Subtask from list",
                                modifier = Modifier
                                    .rotate(45F)
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.Black, CircleShape)
                                    .clickable {
                                        if (taskTypes.value.size != 1) {
                                            if (viewModel.selectedTaskType.value?.uid == item.uid) {
                                                viewModel.deleteTaskType(item.uid)
                                                // If we delete the first task type, give the DB a bit to reload the new first task type
                                                Handler().postDelayed({
                                                    viewModel.selectedTaskType.postValue(firstTaskType.value)
                                                    onTaskChange(firstTaskType.value!!)
                                                }, 100)
                                            } else {
                                                viewModel.deleteTaskType(item.uid)
                                            }
                                        } else {
                                            Toast
                                                .makeText(
                                                    context,
                                                    context.getText(R.string.cannot_delete_last_type),
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.new_task_type),
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    TextField(
                        value = typeInput,
                        onValueChange = {
                            if (it.length <= MAX_TASK_TYPE_NAME_LENGTH) typeInput = it
                        },
                        label = {
                            Text(stringResource(id = R.string.task_type_name))
                        },
                        maxLines = 1
                    )
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(ICON_LIST) { item ->
                            val isChosen = item == chosenIcon
                            val borderColor =
                                if (isChosen) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.surface
                            val insideColor =
                                if (isChosen) ButtonDefaults.buttonColors(backgroundColor = borderColor) else ButtonDefaults.outlinedButtonColors(
                                    contentColor = borderColor
                                )
                            val imageColor = if (isChosen) Color.White else Color.Black
                            Button(
                                border = BorderStroke(1.dp, color = borderColor),
                                colors = insideColor,
                                modifier = Modifier.padding(horizontal = 2.dp),
                                onClick = { chosenIcon = item }
                            ) {
                                Image(
                                    painter = painterResource(id = item),
                                    contentDescription = "icon",
                                    modifier = Modifier.padding(horizontal = 2.dp),
                                    colorFilter = ColorFilter.tint(color = imageColor)
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            if (typeInput != "") {
                                viewModel.insertTaskType(
                                    TaskType(
                                        name = typeInput,
                                        icon = chosenIcon
                                    )
                                )
                                typeInput = ""
                                chosenIcon = ICON_LIST[0]
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getText(R.string.add_task_type_name),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 20.dp)
                    ) {
                        Text(text = stringResource(id = R.string.add_type))
                    }
                    Text(
                        text = "x",
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(horizontal = 10.dp)
                            .clickable { viewModel.typeDialogVisible.postValue(false) },
                        fontSize = 30.sp,
                        color = MaterialTheme.colors.secondary
                    )
                }
            }
        }
    }

}