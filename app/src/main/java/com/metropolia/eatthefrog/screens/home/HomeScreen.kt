package com.metropolia.eatthefrog.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.metropolia.eatthefrog.screens.home.components.ProfileContainer
import com.metropolia.eatthefrog.screens.home.components.TasksContainer
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel
import java.util.*

@ExperimentalMaterialApi
@Composable
fun HomeScreen(username: String, application: Application, navController: NavController) {
    val homeScreenViewModel = HomeScreenViewModel(application)
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileContainer(username, homeScreenViewModel)
        TasksContainer(homeScreenViewModel, currentWeek)
    }
    TaskScreen(homeScreenViewModel, navController)
    FrogCompletedScreen(homeScreenViewModel)
}