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
import com.metropolia.eatthefrog.constants.CONFIRM_WINDOW_KEY
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


            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
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
                        Text(if (task.value?.completed == false)
                                stringResource(R.string.close)
                            else stringResource(R.string.open))
                    }

                    Image(
                        painter = painterResource(R.drawable.edit_24),
                        modifier = Modifier
                            .clickable { navController.navigate("add_task/${task.value!!.uid}/true/${task.value!!.name}/${task.value!!.description}/${task.value!!.deadline}/${task.value!!.time}/${task.value!!.taskTypeOld}") },
                        contentDescription = "edit button")
                }

                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f)
                        .padding(top = 20.dp)
                ) {
                    item {
                        Text(
                            text = task.value?.name ?: "",
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 15.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center)

                        Text(task.value?.description ?: "", Modifier.padding(bottom = 15.dp))

                        if (vm.selectedFilter.value == DateFilter.TODAY) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    if (subtasks.value.isNotEmpty()) stringResource(R.string.subtasks_header)
                                    else "",
                                )

                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        stringResource(R.string.daily_frog),
                                    )
                                    Switch(
                                        checked = task.value?.isFrog ?: false,
                                        onCheckedChange = { vm.openFrogConfirmWindow() },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = MaterialTheme.colors.primaryVariant
                                        )
                                    )
                                }


                            }
                        }
                    }

                    if (subtasks.value.isNotEmpty()) {
                        itemsIndexed(subtasks.value) { i, st ->
                            Row(
                                Modifier
                                    .padding(bottom = if (i < subtasks.value.size - 1) 10.dp else 0.dp
                                )) {

                                Image(
                                    painter =
                                    if (st.completed) painterResource(id = R.drawable.ic_baseline_task_alt_24)
                                    else painterResource(id = R.drawable.ic_baseline_task_alt_24_gray),
                                    contentDescription = "completed sign")

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

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(st.name, modifier = Modifier.padding(start = 10.dp).fillMaxWidth(0.8f))
                                        }

                                        Switch(
                                            checked = st.completed,
                                            onCheckedChange = { vm.updateSubTask(st, it) },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = MaterialTheme.colors.primaryVariant
                                            ),
                                        )

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

        if (vm.getBooleanFromPreferences(CONFIRM_WINDOW_KEY, true)) {
            ConfirmWindow(
                {vm.toggleTaskCompleted(task.value)},
                {vm.closeTaskConfirmWindow()},
                desc,
                modifier = Modifier.clip(
                    RoundedCornerShape(20.dp))
            )
        } else {
            vm.toggleTaskCompleted(task.value)
        }
    }

    if (vm.showFrogConfirmWindow.value) {
        val desc = stringResource(
            if (task.value?.isFrog == false) R.string.set_frog else R.string.remove_frog,
            task.value?.name ?: "")

        if (vm.getBooleanFromPreferences(CONFIRM_WINDOW_KEY, true)) {
            ConfirmWindow(
                {vm.toggleTaskFrog()},
                {vm.closeFrogConfirmWindow()},
                desc,
                modifier = Modifier.clip(
                    RoundedCornerShape(20.dp))
            )
        } else {
            vm.toggleTaskFrog()
        }
    }
}


