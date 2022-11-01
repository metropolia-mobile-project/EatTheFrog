package com.metropolia.eatthefrog.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.PopupView

/**
 * Popup window which displays the selected Task object and its data.
 */

// TODO: Modify TaskScreen to match Task implementation in Room db.
@ExperimentalMaterialApi
@Composable
fun TaskScreen(task: Task = getMockTask()) {
    
    // Example use of PopupView. headerContent can be added, as displayed below.
    PopupView/*(
        headerContent = {
            Column(
                Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.Blue),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Example header")
            }
        }
    )*/ {

        Column(Modifier.fillMaxSize()) {
            Text(task.description)
            LazyColumn (
                Modifier
                    .fillMaxWidth()
            ) {
                items(task.subTasks) { st ->
                    Text(
                        st.description,
                        modifier = Modifier
                            .padding(10.dp))
                }
            }
        }


    }
}


// Placeholder data before Room is implemented.
class SubTask(val description: String)
class Task(var title: String, var description: String, var subTasks: List<SubTask>)
fun getMockTask(): Task {
    
    val st1 = SubTask("Sub-task 1")
    val st2 = SubTask("Sub-task 2")
    val st3 = SubTask("Sub-task 3")
    val st4 = SubTask("Sub-task 4")
    val st5 = SubTask("Sub-task 5")
    
    val subTasks = listOf<SubTask>(st1, st2, st3, st4, st5)
    
    return Task("Mock task", "A fitting description for a mock task", subTasks)
}