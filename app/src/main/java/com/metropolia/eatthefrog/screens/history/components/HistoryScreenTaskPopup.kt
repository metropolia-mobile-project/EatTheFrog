package com.metropolia.eatthefrog.screens.history.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.CONFIRM_WINDOW_KEY
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.database.TaskType
import com.metropolia.eatthefrog.navigation.NavigationItem
import com.metropolia.eatthefrog.screens.PopupSwitchRow
import com.metropolia.eatthefrog.ui_components.ConfirmWindow
import com.metropolia.eatthefrog.ui_components.PopupView
import com.metropolia.eatthefrog.viewmodels.DateFilter
import com.metropolia.eatthefrog.viewmodels.HistoryScreenViewModel
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel
import java.text.SimpleDateFormat

/**
 * Popup window which displays the selected Task object and its data.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryScreenTaskPopup(vm: HistoryScreenViewModel, navController: NavController) {

    val subtasks = vm.getHighlightedSubtasks().observeAsState(listOf())
    val task = vm.getSelectedTask().observeAsState()
    val taskType = vm.getTaskType(if (task.value != null) task.value!!.taskTypeId else 1).observeAsState(
        TaskType(name = stringResource(id = R.string.loading), icon = null)
    )


    navController.addOnDestinationChangedListener { _, destination, _->
        if (destination.route != NavigationItem.History.route) {
            vm.resetPopupStatus()
            Log.d("Navigated to another view", "another view")
        }
    }

    fun formatDateString(string: String): String {
        var newDateString = ""
        try {
            val newFormat = SimpleDateFormat("dd MMMM, yyyy")
            val date = SimpleDateFormat(DATE_FORMAT).parse(string)
            newDateString = newFormat.format(date)
        } catch (e: Exception) {
            Log.d("Failed to format date", string)
        }
        return newDateString
    }

    PopupView(vm.popupVisible, callback = {vm.resetPopupStatus()}) {

        Box(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)) {


            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {

                LazyColumn(
                    Modifier
                        .fillMaxSize()
                ) {
                    item {

                        Row(Modifier.fillMaxWidth().padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = taskType.value?.icon ?: R.drawable.ic_null),
                                    contentDescription = "type icon", tint = MaterialTheme.colors.secondary,
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                                Text(text = taskType.value?.name ?: "<${stringResource(id = R.string.deleted_type)}>", color = MaterialTheme.colors.secondary, fontSize = 14.sp)
                            }
                        }

                        Text(
                            text = task.value?.name ?: "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp, top = 10.dp),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start)

                        Text(task.value?.description ?: "", Modifier.padding(bottom = 15.dp))

                        Row(
                            Modifier
                                .padding(vertical = 5.dp)
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(MaterialTheme.colors.background)
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {


                            Image(painter = painterResource(id = R.drawable.ic_calendar), modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colors.surface)
                                .padding(10.dp)
                                .size(20.dp), contentDescription = "calendar icon", colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant))


                            Column(Modifier.padding(start = 10.dp)) {
                                Text(text = formatDateString(task.value?.deadline ?: ""))
                                Text(text = task.value?.time ?: "")
                            }
                        }

                        Column(
                            Modifier
                                .padding(vertical = 5.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(15.dp))
                                .background(MaterialTheme.colors.background)
                                .padding(15.dp),
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {


                            PopupSwitchRow(desc = if (task.value?.completed == false)
                                stringResource(R.string.close)
                            else stringResource(R.string.open),
                                enabledIcon = R.drawable.ic_baseline_task_alt_24,
                                enabled = task.value?.completed ?: false,
                                toggleState = { vm.openTaskConfirmWindow() }
                            )
                        }

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
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }

                    if (subtasks.value.isNotEmpty()) {
                        itemsIndexed(subtasks.value) { i, st ->
                            Row(
                                modifier = Modifier
                                    .padding(bottom = if (i < subtasks.value.size - 1) 10.dp else 0.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter =
                                    if (st.completed) painterResource(id = R.drawable.ic_baseline_task_alt_24)
                                    else painterResource(id = R.drawable.ic_baseline_task_alt_24_gray),
                                    contentDescription = "completed sign")

                                Spacer(Modifier.width(5.dp))

                                Card(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(15.dp))
                                        .wrapContentSize(), elevation = 225.dp
                                ) {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colors.background)
                                            .padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(st.name, modifier = Modifier
                                                .padding(start = 10.dp)
                                                .fillMaxWidth(0.8f))
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
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top) {
                                Image(painter = painterResource(id = R.drawable.ic_add_task),
                                    modifier = Modifier
                                        .size(100.dp), contentDescription = "plus sign", colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary))
                                Text(text = stringResource(id = R.string.no_subtasks), Modifier.padding(20.dp), color = MaterialTheme.colors.secondary, fontSize = 18.sp, textAlign = TextAlign.Center)
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

        if(vm.getBooleanFromPreferences(CONFIRM_WINDOW_KEY, true)) {
            ConfirmWindow(
                {vm.toggleTaskCompleted()},
                {vm.closeTaskConfirmWindow()},
                desc,
                modifier = Modifier.clip(
                    RoundedCornerShape(20.dp)
                ),
                vm.app
            )
        } else {
            vm.toggleTaskCompleted()
        }
    }
}