package com.metropolia.eatthefrog.screens.addTask.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.metropolia.eatthefrog.ui_components.PopupView
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.ICON_LIST
import com.metropolia.eatthefrog.database.TaskType

@Composable
fun AddTaskTypeDialog(
    viewModel: AddTaskScreenViewModel
) {
    val visible = viewModel.typeDialogVisible.observeAsState()
    val taskTypes = viewModel.getTaskTypes().observeAsState(listOf())
    var typeInput by remember { mutableStateOf("") }
    var chosenIcon by remember { mutableStateOf(ICON_LIST[0]) }

    if (visible.value == true) {
        Dialog(onDismissRequest = { viewModel.typeDialogVisible.postValue(false) }) {
            Column(modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)) {
                LazyColumn(Modifier.fillMaxHeight(0.5f)) {
                    items(taskTypes.value) { item ->
                        Row() {
                            Image(painter = painterResource(id = item.icon ?: R.drawable.ic_null), contentDescription = item.name)
                            Text(text = item.name)
                        }
                    }
                }
                Column(modifier = Modifier.padding(vertical = 50.dp)) {
                    TextField(value = typeInput, onValueChange = { typeInput = it })
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(ICON_LIST) { item ->
                            val isChosen = item == chosenIcon
                            val borderColor = if (isChosen) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.onSurface
                            val insideColor = if (isChosen) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors(contentColor = borderColor)
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
                    Button(onClick = {
                        viewModel.insertTaskType(TaskType(name = typeInput, icon = chosenIcon))
                        typeInput = ""
                        chosenIcon = ICON_LIST[0]
                    }) {
                        Text(text = stringResource(id = R.string.add_type))
                    }
                }
            }
        }
    }

}