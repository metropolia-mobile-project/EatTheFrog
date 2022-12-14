package com.metropolia.eatthefrog.screens.addTask.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.database.TaskType
import com.metropolia.eatthefrog.navigation.NavigationItem
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create task button saves task to database if name and description is inserted to text fields.
 * Saves subtask list to database if subtasks have been created.
 * if function is launched in edit mode 'create task' button is changed to edit task and
 * instead of creating new task it will make changes for the current task
 *
 * @param viewModel: AddTaskScreenViewModel of the parent composable.
 * @param navHost: NavHostController of the application.
 * @param newTask: Task object if not in edit mode.
 * @param isEditMode: Edit mode status.
 * @param editTask: Task object if in edit mode.
 * @param onTitleChange: function to be called when title changed.
 * @param onDescChange: function to be called when description changed.
 * @param onTaskTypeChange: function to be called when TaskType changed.
 * @param onFrogChange: function to be called when isFrog changed.
 */
@Composable
fun AddTaskCreateButtonContainer(
    viewModel: AddTaskScreenViewModel,
    navHost: NavHostController,
    newTask: Task,
    isEditMode: Boolean?,
    editTask: Task,
    onTitleChange: (String) -> Unit,
    onDescChange: (String) -> Unit,
    onTaskTypeChange: (TaskType) -> Unit,
    onFrogChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val taskTypes = viewModel.getTaskTypes().observeAsState(listOf(TaskType(name = stringResource(id = R.string.loading), icon = null)))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Button(
            onClick = {

                if (newTask.name == "" && newTask.description == "") {
                    Toast.makeText(
                        context,
                        R.string.task_name_description_toast,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (newTask.name == "") {
                    Toast.makeText(context, R.string.task_name_toast, Toast.LENGTH_SHORT).show()
                } else if (newTask.description == "") {
                    Toast.makeText(context, R.string.task_description_toast, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (isEditMode == false) {
                        if(newTask.isFrog){
                            viewModel.toggleFrogsFalse(newTask.deadline)
                        }

                        viewModel.saveTask(newTask)

                        onTitleChange("")
                        onDescChange("")
                        onFrogChange(false)
                        onTaskTypeChange(taskTypes.value[0])
                        
                    } else {
                        viewModel.updateTask(editTask)
                        viewModel.insertEditedTasks()
                        viewModel.clearEditSubtaskList()
                        navHost.navigate(NavigationItem.Home.route)
                    }
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            if (isEditMode == true) {
                Text(text = "Edit task")
            } else {
                Text(text = stringResource(id = R.string.create_task))
            }
        }
    }
}
