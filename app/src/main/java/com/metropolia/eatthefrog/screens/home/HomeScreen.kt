package com.metropolia.eatthefrog.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.metropolia.eatthefrog.notification.Scheduler
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.navigation.NavigationItem
import com.metropolia.eatthefrog.screens.home.components.ProfileContainer
import com.metropolia.eatthefrog.screens.home.components.TasksContainer
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel
import com.metropolia.eatthefrog.viewmodels.NotificationsViewModel
import java.util.*

lateinit var notificationViewModel: NotificationsViewModel


/**
 * The main screen of the application. Displays the Tasks and filtering functionality for them, as well as
 * the current and longest frog completion streak. Contains also a FloatingActionButton for opening up the AddTaskScreen.
 * @param username: name of the user.
 * @param application: Application context.
 * @param navController: navController of the application.
 */
@ExperimentalMaterialApi
@Composable
fun HomeScreen(username: String, application: Application, navController: NavController) {
    val homeScreenViewModel = HomeScreenViewModel(application)
    notificationViewModel = NotificationsViewModel(application)
    Scheduler(viewModel = notificationViewModel)

    val calendar = Calendar.getInstance()
    calendar.time = Date()
    val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileContainer(username, homeScreenViewModel)
        TasksContainer(homeScreenViewModel, currentWeek)
    }
    FrogCompletedScreen(homeScreenViewModel)
    Box(Modifier.padding(20.dp).fillMaxSize()) {
        FloatingActionButton(
            backgroundColor = MaterialTheme.colors.primary,
            onClick = { navController.navigate(NavigationItem.AddTask.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
            } },
            shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                .height(50.dp)
                .width(50.dp)
                .align(Alignment.BottomEnd)) {
            Image(
                painterResource(id = R.drawable.ic_plus),
                contentDescription = "",
                colorFilter = ColorFilter.tint(White)
            )
        }
    }
    TaskScreen(homeScreenViewModel, navController)
}