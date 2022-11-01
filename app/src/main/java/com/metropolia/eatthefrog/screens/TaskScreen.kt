package com.metropolia.eatthefrog.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.PopupView
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel

/**
 * Popup window which displays the selected Task object and its data. Enables the user to set Sub-tasks as complete, as well as
 * edit the Task object.
 */
// TODO: Modify TaskScreen to match Task implementation in Room db.
@ExperimentalMaterialApi
@Composable
fun TaskScreen(vm: HomeScreenViewModel) {

    // TODO: Add functionality when Room is avaiable
    fun toggleCompletedSubTask() {}


    // Example use of PopupView. headerContent can be added, as displayed below.
    PopupView(vm.popupVisible.value, callback = {vm.resetPopupStatus()}) {

        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {


            Text(vm.highlightedTask.value.name, Modifier.padding(bottom = 15.dp))
            Text(vm.highlightedTask.value.description, Modifier.padding(bottom = 15.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Daily frog")
                Checkbox(checked = vm.highlightedTask.value.isFrog, onCheckedChange = {vm.setTaskAsDailyFrog(it)})
            }


            LazyColumn (
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            ) {
                items(vm.highlightedTask.value.subtasks) { st ->
                    Row {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // TODO: Check if sub-task is completed, change Image accordingly (checked/unchecked).
                            Image(
                                painter = painterResource(R.drawable.radio_btn_checked),
                                contentDescription = ""
                            )
                            Spacer(Modifier.padding(2.dp))
                            Divider(modifier = Modifier
                                .height(120.dp)
                                .width(2.dp), color = MaterialTheme.colors.primary)
                            Spacer(Modifier.padding(2.dp))
                        }

                        Spacer(Modifier.width(5.dp))

                        Card(modifier = Modifier.clip(RoundedCornerShape(15.dp)), elevation = 25.dp) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .padding(10.dp)
                                    .background(MaterialTheme.colors.background),
                            ) {
                                Text(st.name)
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    // TODO: Toggle subtask completion status when checkbox is clicked
                                    Checkbox(checked = st.completed, onCheckedChange = { st.completed = it })
                                }
                            }
                        }


                    }

                }
            }

            // TODO: Move this to a better location, maybe top-left corner?
/*            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    *//* Open CreateTaskScreen with the given task *//*
                }) {
                Text("Edit")
            }*/
        }
    }
}