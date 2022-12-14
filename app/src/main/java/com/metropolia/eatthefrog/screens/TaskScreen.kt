package com.metropolia.eatthefrog.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
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
import com.metropolia.eatthefrog.ui_components.ConfirmWindow
import com.metropolia.eatthefrog.ui_components.PopupView
import com.metropolia.eatthefrog.viewmodels.DateFilter
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel
import java.text.SimpleDateFormat

/**
 * Popup window which displays the selected Task object and its data.
 * Enables the user to set Sub-tasks as complete, as well as edit the Task object in CreateTaskScreen.
 * @param vm: HomeScreenViewModel of the parent composable.
 * @param navController: navController of the application.
 */
@ExperimentalMaterialApi
@Composable
fun TaskScreen(vm: HomeScreenViewModel, navController: NavController) {
    val context = LocalContext.current

    val subtasks = vm.getHighlightedSubtasks().observeAsState(listOf())
    Log.d("subtasks", subtasks.value.toString())
    val task = vm.getSelectedTask().observeAsState()
    val taskType = vm.getTaskType(if (task.value != null) task.value!!.taskTypeId else 1)
        .observeAsState(TaskType(name = stringResource(id = R.string.loading), icon = null))
    val firstTaskType = vm.getFirstTaskType().observeAsState()
    navController.addOnDestinationChangedListener { _, destination, _ ->
        if (destination.route != NavigationItem.Home.route) {
            vm.resetPopupStatus()
            Log.d("Navigated to another view", "another view")
        }
    }

    /**
     * Formats the given string to a Date object, and then reformats it to the given pattern. (dd MMMM, yyyy)
     */
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

    PopupView(vm.popupVisible, callback = { vm.resetPopupStatus() }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                LazyColumn(
                    Modifier
                        .fillMaxSize()
                ) {
                    item {

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(
                                        id = taskType.value.icon ?: R.drawable.ic_null
                                    ),
                                    contentDescription = "type icon",
                                    tint = MaterialTheme.colors.secondary,
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                                // Task type can be null here, ignore the warning
                                Text(
                                    text = taskType.value?.name ?: "<${stringResource(id = R.string.deleted_type)}>",
                                    color = MaterialTheme.colors.secondary,
                                    fontSize = 14.sp
                                )
                            }
                            Row() {
                                Image(
                                    painter = painterResource(R.drawable.ic_delete),
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable {
                                            if (task.value != null) {
                                                vm.openTaskDeleteWindow()
                                            }
                                        },
                                    contentDescription = "delete button",
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                                )
                                Image(
                                    painter = painterResource(R.drawable.ic_baseline_edit_note),
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable {
                                            navController.navigate("add_task/${task.value!!.uid}/true/${task.value!!.name}/${task.value!!.description}/${task.value!!.deadline}/${task.value!!.time}/${taskType.value.uid}/${task.value!!.isFrog}") {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                    contentDescription = "edit button",
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                                )
                            }
                        }

                        Text(
                            text = task.value?.name ?: "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp, top = 10.dp),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )

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

                            Image(
                                painter = painterResource(id = R.drawable.ic_calendar),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colors.surface)
                                    .padding(10.dp)
                                    .size(20.dp),
                                contentDescription = "calendar icon",
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant)
                            )


                            Column(Modifier.padding(start = 10.dp)) {
                                Log.d("deadline date", task.value?.deadline.toString())

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

                            PopupSwitchRow(desc = stringResource(R.string.daily_frog),
                                enabledIcon = R.drawable.ic_frog_cropped,
                                enabled = task.value?.isFrog ?: false,
                                toggleState = { vm.openFrogConfirmWindow() }
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
                                    .padding(
                                        bottom = if (i < subtasks.value.size - 1) 10.dp else 0.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter =
                                    if (st.completed) painterResource(id = R.drawable.ic_baseline_task_alt_24)
                                    else painterResource(id = R.drawable.ic_baseline_task_alt_24_gray),
                                    contentDescription = "completed sign"
                                )

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
                                            Text(
                                                st.name, modifier = Modifier
                                                    .padding(start = 10.dp)
                                                    .fillMaxWidth(0.8f)
                                            )
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
                                verticalArrangement = Arrangement.Top
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_add_task),
                                    modifier = Modifier
                                        .size(100.dp),
                                    contentDescription = "plus sign",
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary)
                                )
                                Text(
                                    text = stringResource(id = R.string.no_subtasks),
                                    Modifier.padding(20.dp),
                                    color = MaterialTheme.colors.secondary,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (vm.showTaskDeleteConfirmWindow.value) {
        val desc = stringResource(R.string.delete_task, task.value?.name ?: "")
        ConfirmWindow(
            {
                vm.resetPopupStatus()
                vm.closeTaskDeleteWindow()
                vm.deleteTask(task.value!!.uid)
            },
            { vm.closeTaskDeleteWindow() },
            desc,
            modifier = Modifier.clip(
                RoundedCornerShape(20.dp)
            ),
            application = vm.app,
            showSlider = false
        )
    }

    if (vm.showTaskDoneConfirmWindow.value) {
        val desc = stringResource(
            if (task.value?.completed == false) R.string.close_task else R.string.open_task,
            task.value?.name ?: ""
        )

        if (vm.getBooleanFromPreferences(CONFIRM_WINDOW_KEY, true)) {
            ConfirmWindow(
                { vm.toggleTaskCompleted(task.value, context) },
                { vm.closeTaskConfirmWindow() },
                desc,
                modifier = Modifier.clip(
                    RoundedCornerShape(20.dp)
                ),
                application = vm.app
            )
        } else {
            vm.toggleTaskCompleted(task.value, LocalContext.current)
        }
    }

    if (vm.showFrogConfirmWindow.value) {
        val desc = stringResource(
            if (task.value?.isFrog == false) R.string.set_frog else R.string.remove_frog,
            task.value?.name ?: ""
        )

        if (vm.getBooleanFromPreferences(CONFIRM_WINDOW_KEY, true)) {
            ConfirmWindow(
                { vm.toggleTaskFrog() },
                { vm.closeFrogConfirmWindow() },
                desc,
                modifier = Modifier.clip(
                    RoundedCornerShape(20.dp)
                ),
                application = vm.app
            )
        } else {
            vm.toggleTaskFrog()
        }
    }
}

/**
 * Creates a Row containing an Image, Switch and a Text object for the Switch component.
 * @param desc: description text of the row.
 * @param enabledIcon: Icon displayed when switch status is true.
 * @param disabledIcon: Icon displayed when switch status is false.
 * @param enabled: Enabled status of the switch.
 * @param toggleState: function to be called when state is changed.
 */
@Composable
fun PopupSwitchRow(
    desc: String,
    enabledIcon: Int,
    disabledIcon: Int? = enabledIcon,
    enabled: Boolean, toggleState: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Card(
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .background(MaterialTheme.colors.background),
                shape = CircleShape,
                elevation = 5.dp,
            ) {

                Column(
                    Modifier
                        .padding(10.dp)
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(
                            if (enabled) enabledIcon
                            else disabledIcon ?: enabledIcon
                        ),
                        contentDescription = "darkmode_lightmode"
                    )
                }
            }

            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = desc,
                fontWeight = FontWeight.Light,
                fontSize = 13.sp
            )
        }

        Switch(
            checked = enabled,
            onCheckedChange = { toggleState() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primaryVariant
            )
        )
    }
}
