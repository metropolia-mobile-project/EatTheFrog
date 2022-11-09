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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.PopupView
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.ui_components.ConfirmWindow
import com.metropolia.eatthefrog.viewmodels.DateFilter
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Popup window which displays the selected Task object and its data. Enables the user to set Sub-tasks as complete, as well as
 * edit the Task object in CreateTaskScreen.
 */
@ExperimentalMaterialApi
@Composable
fun TaskScreen(vm: HomeScreenViewModel) {

    val subtasks = vm.getHighlightedSubtasks().observeAsState(listOf())
    val task = vm.getSelectedTask().observeAsState()
    val dailyFrogSelected = vm.dailyFrogSelected.observeAsState()


    if (task.value?.completed == true) {
        vm.getMotivationQuote()
    }


    PopupView(vm.popupVisible.value, callback = {vm.resetPopupStatus()}) {

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
                    if (task.value?.completed == true)
                    stringResource(R.string.closed)
                    else stringResource(R.string.open))
            }


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

                if (vm.selectedFilter.value == DateFilter.TODAY && (dailyFrogSelected.value == false || task.value?.isFrog == true)) {
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

    if (vm.showTaskDoneConfirmWindow.value) {
        val desc = stringResource(
            if (task.value?.completed == false) R.string.close_task else R.string.open_task,
            task.value?.name ?: "")
        ConfirmWindow({ vm.toggleTaskCompleted() },{vm.closeTaskConfirmWindow()}, desc)
    }

    if (vm.showFrogConfirmWindow.value) {
        val desc = stringResource(
            if (task.value?.isFrog == false) R.string.set_frog else R.string.remove_frog,
            task.value?.name ?: "")
        ConfirmWindow({ vm.toggleTaskFrog() },{vm.closeFrogConfirmWindow()}, desc)
    }

    if (vm.showQuoteWindow.value) {
        ConfirmWindow({ vm.closeQuoteWindow() }, { vm.closeQuoteWindow() }, "Love is trash, bitches need cash \n   - Mahatma Gandhi")
    }
}

