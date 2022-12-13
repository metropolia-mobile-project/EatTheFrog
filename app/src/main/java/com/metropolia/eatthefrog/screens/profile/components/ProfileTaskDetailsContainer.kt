package com.metropolia.eatthefrog.screens.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.ProfileScreenViewModel

/**
 * ProfileTaskDetailsContainer function creates the middle part of the profile screen,
 * displaying statistics of completed Tasks and frogs.
 * @param vm: ProfileScreenViewModel of the parent composable.
 */
@Composable
fun ProfileTaskDetailsContainer(vm: ProfileScreenViewModel) {
    val closedTasks = vm.getClosedTasks().observeAsState(0)
    val activeTasks = vm.getActiveTasks().observeAsState(0)
    val frogsEaten = vm.getFrogsEaten().observeAsState(0)
    val totalTasks = vm.getTotalTaskCount().observeAsState(0)

    ProfileTaskDetailsRow(stringResource(id = R.string.closed_tasks), closedTasks.value, R.drawable.ic_baseline_task_alt_24,
        stringResource(id = R.string.frogs_eaten), frogsEaten.value, R.drawable.ic_frog_cropped, true)
    ProfileTaskDetailsRow(stringResource(id = R.string.active_tasks), activeTasks.value, R.drawable.ic_chart,
        stringResource(id = R.string.total_tasks), totalTasks.value, R.drawable.ic_done_all, false)
}

/**
 * Creates a row with 2 TaskInfoContainers.
 * @param firstTaskTitle: Title of the left TaskInfoContainer.
 * @param firstTaskAmount: info/amount of the left TaskInfoContainer.
 * @param firstTaskIcon: Icon of the left TaskInfoContainer.
 * @param secondTaskTitle: Title of the right TaskInfoContainer.
 * @param secondTaskAmount: info/amount of the right TaskInfoContainer.
 * @param secondTaskIcon: Icon of the right TaskInfoContainer.
 */
@Composable
fun ProfileTaskDetailsRow(firstTaskTitle: String, firstTaskAmount: Int, firstTaskIcon: Int,
                          secondTaskTitle: String, secondTaskAmount: Int, secondTaskIcon: Int, upperRow: Boolean) {
    Row(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .height(140.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(end = 5.dp)
                .fillMaxWidth(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TaskInfoContainer(firstTaskTitle, firstTaskAmount.toString(), firstTaskIcon, upperRow)
        }
        Column(
            modifier = Modifier
                .padding(start = 5.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            TaskInfoContainer(secondTaskTitle, secondTaskAmount.toString(), secondTaskIcon, !upperRow)
        }
    }
}

/**
 * Creates a Card with a title, icon and with a info text.
 * @param title: Title of the composable, displayed on the top left
 * @param info: Info of the composable, displayed on the bottom left. For example, how many frog completed.
 * @param icon: Icon displayed on the middle of the composable.
 */
@Composable
fun TaskInfoContainer(title: String, info: String, icon: Int, lightBackground: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = if (lightBackground) MaterialTheme.colors.surface else MaterialTheme.colors.background,
        elevation = 5.dp,
    ) {
        Column(
            Modifier.padding(15.dp).fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
            ) {
            Text(title, fontSize = 13.sp)
            Image(
                modifier = Modifier
                    .size(40.dp),
                painter = painterResource(icon),
                contentDescription = "task_info_icon"
            )
            Text(info, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
    }
}