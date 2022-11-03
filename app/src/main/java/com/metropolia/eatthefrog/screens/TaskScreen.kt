package com.metropolia.eatthefrog.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.PopupView
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel

/**
 * Popup window which displays the selected Task object and its data. Enables the user to set Sub-tasks as complete, as well as
 * edit the Task object in CreateTaskScreen.
 */
@ExperimentalMaterialApi
@Composable
fun TaskScreen(vm: HomeScreenViewModel) {

    val subtasks = vm.getHighlightedSubTasks().observeAsState(listOf())
    val task = vm.getSelectedTask().observeAsState()

    PopupView(vm.popupVisible.value, callback = {vm.resetPopupStatus()}) {

        Box(
            Modifier
                .fillMaxSize()
                .padding(20.dp)) {

            Image(
                painter = painterResource(R.drawable.edit_24),
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .clickable { /* Open up CreateTaskScreen with the task*/ },
                contentDescription = "edit button")

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {


                Text(task.value?.name ?: "", Modifier.padding(bottom = 15.dp), fontWeight = FontWeight.Bold)
                Text(task.value?.description ?: "", Modifier.padding(bottom = 15.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.daily_frog))
                    Checkbox(
                        checked = task.value?.isFrog ?: false,
                        onCheckedChange = { vm.setTaskAsDailyFrog(it) })
                }

                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f)
                ) {

                    itemsIndexed(subtasks.value) { i, st ->
                        Row {

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = if (!st.completed) {
                                        painterResource(R.drawable.radio_btn_checked)
                                    } else {
                                        painterResource(R.drawable.radio_btn_unchecked)
                                    },
                                    contentDescription = ""
                                )

                                if (i < subtasks.value.size - 1) {
                                    Spacer(Modifier.padding(2.dp))
                                    Divider(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .width(2.dp), color = MaterialTheme.colors.primary
                                    )
                                    Spacer(Modifier.padding(2.dp))
                                }
                            }

                            Spacer(Modifier.width(5.dp))

                            Card(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(15.dp))
                                    .wrapContentSize(), elevation = 25.dp
                            ) {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                        .background(MaterialTheme.colors.background),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(st.name, modifier = Modifier.padding(start = 10.dp))
                                    Checkbox(
                                        checked = st.completed,
                                        onCheckedChange = { vm.updateSubTask(st, it) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}