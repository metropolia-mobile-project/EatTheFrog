package com.metropolia.eatthefrog.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.activities.MainActivity
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.placeholder_data.PlaceholderTask
import com.metropolia.eatthefrog.placeholder_data.PlaceholderTasks
import com.metropolia.eatthefrog.viewmodels.DateFilter
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel
import com.metropolia.eatthefrog.viewmodels.TasksViewModel
import java.util.*

/**
 * Container for a singular task in the Home screen list
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SingleTaskContainer(task: Task, vm: TasksViewModel) {
    val backgroundColor = if (task.isFrog) MaterialTheme.colors.primaryVariant else Color.White
    val taskNameTextColor = if (task.isFrog) Color.White else Color.Black
    val subtaskTextColor = if (task.isFrog) MaterialTheme.colors.secondary else MaterialTheme.colors.primary
    val subtaskAmount = vm.getSubtasksAmount(task.uid).observeAsState()
    val subtasks = vm.getSubtasks(task.uid).observeAsState()
    val closedSubtaskAmount = subtasks.value?.filter { it.completed }?.size ?: 0
    val subtaskText = if (subtaskAmount.value == 0) stringResource(id = R.string.no_subtasks)
                      else "$closedSubtaskAmount/${subtaskAmount.value} ${stringResource(id = if (subtaskAmount.value == 1) R.string.subtask else R.string.subtasks)} ${stringResource(id = R.string.done)}"
    val typeText = task.taskTypeOld.toString().lowercase().capitalize()
    val deadlineText = if (vm.selectedFilter.value == DateFilter.TODAY) "${stringResource(id = R.string.at)} ${task.time}" else "${task.deadline} ${stringResource(id = R.string.at)} ${task.time}"

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = backgroundColor,
        elevation = 5.dp,
        onClick = {
            vm.updateHighlightedTask(task)
            vm.showPopup()
        }
    ) {
        Column() {
            Text(text = typeText, color = MaterialTheme.colors.secondary, fontSize = 14.sp, modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp))
            Row(
                Modifier.padding(horizontal = 20.dp)) {
                Column() {
                    Image(
                        painter = painterResource(id = R.drawable.ic_chart),
                        contentDescription = "Chart icon",
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                        modifier = Modifier.size(50.dp)
                    )
                    if (task.isFrog) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_frog_cropped),
                            contentDescription = "Chart icon",
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .size(50.dp)
                        )
                    }
                }

                Column(Modifier.padding(horizontal = 10.dp)) {
                    Text(text = task.name, color = taskNameTextColor, fontSize = 24.sp)
                    Text(text = subtaskText, color = subtaskTextColor)
                }
            }
            Row(modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(vertical = 10.dp)
                .align(Alignment.End)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_deadline),
                    contentDescription = stringResource(id = R.string.task_deadline),
                    colorFilter = ColorFilter.tint(color = taskNameTextColor),
                    modifier = Modifier.padding(horizontal = 5.dp))
                Text(text = deadlineText, color = taskNameTextColor)
            }

        }

    }
}