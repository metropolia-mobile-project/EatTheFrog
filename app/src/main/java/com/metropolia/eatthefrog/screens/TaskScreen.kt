package com.metropolia.eatthefrog.screens

import android.util.Log
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.metropolia.eatthefrog.ui_components.PopupView
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.navigation.NavigationItem
import com.metropolia.eatthefrog.ui_components.ConfirmWindow
import com.metropolia.eatthefrog.viewmodels.DateFilter
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel


/**
 * Popup window which displays the selected Task object and its data. Enables the user to set Sub-tasks as complete, as well as
 * edit the Task object in CreateTaskScreen.
 */
@ExperimentalMaterialApi
@Composable

fun TaskScreen(vm: HomeScreenViewModel, navController: NavController) {


    val subtasks = vm.getHighlightedSubtasks().observeAsState(listOf())
    val task = vm.getSelectedTask().observeAsState()
    val dailyFrogSelected = vm.dailyFrogSelected.observeAsState()

    navController.addOnDestinationChangedListener { _, destination, _->
        if (destination.route != NavigationItem.Home.route) {
            vm.resetPopupStatus()
            Log.d("Navigated to another view", "another view")
        }
    }

    PopupView(vm.popupVisible, callback = {vm.resetPopupStatus()}) {

        Box(
            Modifier
                .fillMaxSize()
                .padding(20.dp)) {

            Row(
                Modifier
                .align(alignment = Alignment.TopStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Switch(
                    checked = task.value?.completed ?: false,
                    onCheckedChange = { vm.openTaskConfirmWindow() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primaryVariant
                    ),
                )
                Text(
                    if (task.value?.completed == false)
                    stringResource(R.string.close)
                    else stringResource(R.string.open))
            }


            Image(
                painter = painterResource(R.drawable.edit_24),
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .clickable { navController.navigate("add_task/${task.value!!.uid}/true/${task.value!!.name}/${task.value!!.description}/${task.value!!.deadline}/${task.value!!.time}/${task.value!!.taskType}")},
                contentDescription = "edit button")

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(task.value?.name ?: "", Modifier.padding(bottom = 15.dp), fontWeight = FontWeight.Bold)

                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f)
                        .padding(top = 20.dp)
                ) {
                    item {
                        Text(task.value?.description ?: "", Modifier.padding(bottom = 15.dp))

                        if (vm.selectedFilter.value == DateFilter.TODAY) {
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
                                    onCheckedChange = { vm.openFrogConfirmWindow() }
                                )
                            }
                        }
                    }

                    if (subtasks.value.isNotEmpty()) {
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
                                        Text(st.name, modifier = Modifier.padding(start = 10.dp).fillMaxWidth(0.8f))
                                        Checkbox(
                                            modifier = Modifier.fillMaxWidth(),
                                            checked = st.completed,
                                            onCheckedChange = { vm.updateSubTask(st, it) })
                                    }
                                }
                            }
                        }
                    } else {
                        item {
                            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(painter = painterResource(id = R.drawable.ic_add_task),
                                    modifier = Modifier
                                        .padding(top = 30.dp)
                                        .size(100.dp), contentDescription = "plus sign", colorFilter = ColorFilter.tint(MaterialTheme.colors.surface))
                                Text(text = stringResource(id = R.string.no_subtasks), Modifier.padding(20.dp), color = MaterialTheme.colors.surface, fontSize = 18.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }

    if (vm.showTaskDoneConfirmWindow.value) {
        val desc = stringResource(
            if (task.value?.completed == false) R.string.close_task else R.string.open_task,
            task.value?.name ?: "")
        ConfirmWindow({ vm.toggleTaskCompleted(task.value) },{vm.closeTaskConfirmWindow()}, desc)
    }

    if (vm.showFrogConfirmWindow.value) {
        val desc = stringResource(
            if (task.value?.isFrog == false) R.string.set_frog else R.string.remove_frog,
            task.value?.name ?: "")
        ConfirmWindow({ vm.toggleTaskFrog() },{vm.closeFrogConfirmWindow()}, desc)
    }
}


