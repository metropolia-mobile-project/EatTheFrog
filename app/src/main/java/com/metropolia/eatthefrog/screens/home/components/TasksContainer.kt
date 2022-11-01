package com.metropolia.eatthefrog.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.placeholder_data.PlaceholderTasks
import com.metropolia.eatthefrog.viewmodels.DateFilter
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Bottom half of the Home screen, containing the task list with filtering for the current day, week
 * and month.
 */
@Composable
fun TasksContainer(homeScreenViewModel: HomeScreenViewModel) {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM, uuuu")
    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 50.dp))
        .background(MaterialTheme.colors.secondary)) {
        Column() {
            Row(modifier = Modifier.padding(20.dp)) {
                Text(text = stringResource(id = R.string.tasks), color = Color.White, fontSize = 24.sp, modifier = Modifier.align(Alignment.CenterVertically))
                Text(text = " | ", color = Color.Black, fontSize = 24.sp, modifier = Modifier.align(Alignment.CenterVertically))
                Text(text = LocalDate.now().format(formatter), color = Color.Black, fontSize = 15.sp, modifier = Modifier.align(Alignment.CenterVertically))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                DateFilterButton(type = DateFilter.TODAY, homeScreenViewModel = homeScreenViewModel)
                DateFilterButton(type = DateFilter.WEEK, homeScreenViewModel = homeScreenViewModel)
                DateFilterButton(type = DateFilter.MONTH, homeScreenViewModel = homeScreenViewModel)
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary)
    ) {
        items(items = PlaceholderTasks.tasks, itemContent = { item ->
            Box(Modifier.padding(10.dp)) {
                SingleTaskContainer(item)
            }
        })
    }
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