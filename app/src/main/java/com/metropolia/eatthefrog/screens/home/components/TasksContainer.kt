package com.metropolia.eatthefrog.screens.home.components

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.Key.Companion.D
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.ui_components.SingleTaskContainer
import com.metropolia.eatthefrog.viewmodels.DateFilter
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel
import org.intellij.lang.annotations.JdkConstants
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Bottom half of the Home screen, containing the task list with filtering for the current day, week
 * and month.
 */
@Composable
fun TasksContainer(homeScreenViewModel: HomeScreenViewModel, currentWeek: Int) {

    val today = homeScreenViewModel.today
    val formatter = DateTimeFormatter.ofPattern("dd MMMM, uuuu")
    val calendar = Calendar.getInstance()

    val currentDateFilter = homeScreenViewModel.selectedFilter.observeAsState()
    val tasks = homeScreenViewModel.getTasks().observeAsState(null)
    val searchVisible = homeScreenViewModel.searchVisible.observeAsState()
    val tasksFiltered = (tasks.value?.filter { it.deadline == today })?.filter { it.isFrog }
    homeScreenViewModel.dailyFrogSelected.postValue(tasksFiltered?.isNotEmpty())

    var taskItems: List<Task>? = listOf()
    val emptyTasksText: String

    when(currentDateFilter.value) {
        DateFilter.TODAY -> {
            emptyTasksText = if (searchVisible.value == false) {
                stringResource(R.string.no_tasks_for_today)
            } else stringResource(R.string.no_tasks_found)

            taskItems = tasks.value?.filter { it.deadline == today }
        }
        DateFilter.WEEK -> {
            emptyTasksText = if (searchVisible.value == false) {
                stringResource(id = R.string.no_tasks_for_this_week)
            } else stringResource(R.string.no_tasks_found)

            taskItems = tasks.value?.filter {
                val deadlineDate = SimpleDateFormat(DATE_FORMAT).parse(it.deadline)
                calendar.time = deadlineDate
                calendar.get(Calendar.WEEK_OF_YEAR) == currentWeek
            }
            taskItems = taskItems?.sortedByDescending { parseStringToDate(it.deadline) }
        }
        DateFilter.MONTH -> {
            emptyTasksText = if (searchVisible.value == false) {
                stringResource(id = R.string.no_tasks_for_this_month)
            } else stringResource(R.string.no_tasks_found)

            taskItems = tasks.value?.filter {
                val deadlineArray = it.deadline.split(".")
                val todayArray = today.split(".")
                deadlineArray[1] == todayArray[1] && deadlineArray[2] == todayArray[2]
            }
            taskItems = taskItems?.sortedByDescending { parseStringToDate(it.deadline) }
        }
        else -> { emptyTasksText = "Invalid DateFilter" }
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 50.dp))
        .background(MaterialTheme.colors.secondary),
    ) {
        Column {

            Box(modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
                .height(60.dp)) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !(searchVisible.value as Boolean),
                    enter = fadeIn(),
                    exit = fadeOut()) {
                    Box(Modifier.fillMaxSize()) {
                        Row(Modifier.align(Alignment.CenterStart)) {
                            Text(text = stringResource(id = R.string.tasks), color = Color.White, fontSize = 24.sp, modifier = Modifier.align(Alignment.CenterVertically))
                            Text(text = " | ", color = Color.Black, fontSize = 24.sp, modifier = Modifier.align(Alignment.CenterVertically))
                            Text(text = LocalDate.now().format(formatter), color = Color.Black, fontSize = 15.sp, modifier = Modifier.align(Alignment.CenterVertically))
                        }
                        Image(
                            painter = painterResource(R.drawable.ic_baseline_search_36),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clickable { homeScreenViewModel.showSearch() },
                            contentDescription = "open search button",
                        )
                    }
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = searchVisible.value ?: false,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    SearchContainer(homeScreenViewModel)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                DateFilterButton(type = DateFilter.TODAY, homeScreenViewModel = homeScreenViewModel)
                DateFilterButton(type = DateFilter.WEEK, homeScreenViewModel = homeScreenViewModel)
                DateFilterButton(type = DateFilter.MONTH, homeScreenViewModel = homeScreenViewModel)
            }
        }
    }
    if (taskItems != null) {
         if (taskItems.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.secondary)
                    .padding(top = 10.dp)
            ) {
                items(items = taskItems, itemContent = { item ->
                    Box(Modifier.padding(10.dp)) {
                        SingleTaskContainer(item, homeScreenViewModel)
                    }
                })
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.secondary),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = R.drawable.ic_add_task), modifier = Modifier
                    .padding(top = 30.dp)
                    .size(100.dp), contentDescription = "plus sign", colorFilter = ColorFilter.tint(MaterialTheme.colors.surface))
                Text(text = emptyTasksText, Modifier.padding(20.dp), color = MaterialTheme.colors.surface, fontSize = 18.sp, textAlign = TextAlign.Center)
                if (searchVisible.value != true) {
                    Text(text = stringResource(id = R.string.go_to_add_task), Modifier.padding(20.dp), color = MaterialTheme.colors.surface, fontSize = 18.sp, textAlign = TextAlign.Center)
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.secondary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp).padding(top = 50.dp),
                color = MaterialTheme.colors.primaryVariant,
                strokeWidth = 10.dp
            )
        }
    }
}

private fun parseStringToDate(string: String): Date {
    var d = Date()
    try {
        d = SimpleDateFormat(DATE_FORMAT).parse(string)
    } catch (e: Exception) {
        Log.d("Failed to parse date", e.message.toString())
    }
    return d
}

@Composable
fun DateFilterButton(type: DateFilter, homeScreenViewModel: HomeScreenViewModel) {
    val currentDateFilter = homeScreenViewModel.selectedFilter.observeAsState()
    val text = when(type) {
        DateFilter.TODAY -> stringResource(id = R.string.today)
        DateFilter.WEEK -> stringResource(id = R.string.week)
        DateFilter.MONTH -> stringResource(id = R.string.month)
    }

    TextButton(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .height(40.dp),
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = if (currentDateFilter.value == type) MaterialTheme.colors.primaryVariant else Color.Transparent, contentColor = Color.White),
        onClick = { homeScreenViewModel.selectDateFilter(type) }
    ) {
        Text(text = text, modifier = Modifier.padding(horizontal = 10.dp))
    }
}