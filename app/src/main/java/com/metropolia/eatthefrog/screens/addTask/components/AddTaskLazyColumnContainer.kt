package com.metropolia.eatthefrog.screens.addTask.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * This container shows created subtasks in lazyColumn.
 */
@Composable
fun AddTaskLazyColumnContainer(
    viewModel: AddTaskScreenViewModel,
    isEditMode: Boolean,
    sDate: String,
    isFrog: Boolean,
    onIsFrogChange: (Boolean) -> Unit
) {

    val subList = viewModel.subTaskList.observeAsState()
    val editSubList = viewModel.editedSubTaskList.observeAsState()
    val sdf = SimpleDateFormat(DATE_FORMAT)
    val currentDate = sdf.format(Date())

    Column(
        Modifier.heightIn(0.dp, 350.dp).fillMaxWidth(),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(R.string.subtasks_header)
            )
            if (currentDate == sDate && !isEditMode) {
                Row(modifier = Modifier) {
                    Text(
                        stringResource(R.string.daily_frog),
                    )
                    Switch(
                        checked = isFrog,
                        onCheckedChange = onIsFrogChange,
                        Modifier.height(25.dp)
                    )
                }
            }
        }


        LazyColumn(
            Modifier
                .padding(vertical = 20.dp)
                .wrapContentHeight()
                .fillMaxWidth()
        ) {

            itemsIndexed(
                if (isEditMode) {
                    editSubList.value!!.toList()
                } else {
                    subList.value!!.toList()
                }
            ) { index, sub ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = (index + 1).toString() + ". " + if (sub.name.length > 13) {
                            sub.name.substring(0, 13) + "..."
                        } else {
                            sub.name
                        }, modifier = Modifier
                            .padding(0.dp, 3.dp)
                    )
                    Icon(
                        painterResource(id = R.drawable.ic_add),
                        contentDescription = "Delete Subtask from list",
                        modifier = Modifier
                            .rotate(45F)
                            .size(20.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Black, CircleShape)
                            .clickable {
                                if (isEditMode) {
                                    viewModel.deleteSubTaskFromDatabase(sub.uid)
                                    viewModel.deleteEditedSubTask(index)
                                } else {
                                    viewModel.deleteSubTask(index)
                                }
                            }
                    )
                }
            }
        }
    }
}





