package com.metropolia.eatthefrog.screens.addTask.components

import android.util.Log
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
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.navigation.NavigationItem
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel

/**
 * Create task button saves task to database if name and description is inserted to text fields.
 * Saves subtask list to database if subtasks have been created.
 * Navigates to 'home' screen when tasks are saved.
 */
@Composable
fun AddTaskCreateButtonContainer(
    viewModel: AddTaskScreenViewModel,
    navHost: NavHostController,
    newTask: Task,
    isEditMode: Boolean?,
    editTask: Task
) {
    val context = LocalContext.current
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
                            "Task needs name and description",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (newTask.name == "") {
                        Toast.makeText(context, "Give task a name", Toast.LENGTH_SHORT).show()
                    } else if (newTask.description == "") {
                        Toast.makeText(context, "Give task a description", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        if(isEditMode == false) {
                            viewModel.insertTask(newTask)
                            viewModel.insertSubTask()
                            navHost.navigate(NavigationItem.Home.route)
                        } else {
                            viewModel.updateTask(editTask)
                            viewModel.insertEditedTasks()
                            viewModel.clearEditSubtaskList()
                            navHost.navigate(NavigationItem.Home.route)
                        }
                    }
            }, modifier = Modifier
                .width(200.dp)
                .padding(top = 50.dp)
        ) {
            if(isEditMode == true){
                Text(text = "Edit task")
            } else {
                Text(text = stringResource(id = R.string.create_task))
            }
        }
    }
}