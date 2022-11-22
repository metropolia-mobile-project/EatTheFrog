package com.metropolia.eatthefrog.navigation

import android.app.Application
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.metropolia.eatthefrog.screens.HomeScreen
import com.metropolia.eatthefrog.screens.ProfileScreen
import com.metropolia.eatthefrog.screens.addTask.AddTaskScreen
import com.metropolia.eatthefrog.screens.history.HistoryScreen

@ExperimentalMaterialApi
@Composable
fun Navigation(navController: NavHostController, username: String, application: Application) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen(username, application, navController)
        }
        composable(NavigationItem.AddTask.route) {
            AddTaskScreen(application, navController)
        }
        composable(NavigationItem.Profile.route) {
            ProfileScreen(username, application)
        }
        composable(NavigationItem.History.route) {
            HistoryScreen(application, navController)
        }
    }
}