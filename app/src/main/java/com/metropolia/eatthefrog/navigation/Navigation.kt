package com.metropolia.eatthefrog.navigation

import android.app.Application
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.metropolia.eatthefrog.screens.HomeScreen
import com.metropolia.eatthefrog.screens.ProfileScreen
import com.metropolia.eatthefrog.screens.addTask.AddTaskScreen

@ExperimentalMaterialApi
@Composable
fun Navigation(navController: NavHostController, username: String, application: Application) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen(username, application)
        }
        composable(NavigationItem.AddTask.route) {
            AddTaskScreen(application, navController)
        }
        composable(NavigationItem.Profile.route) {
            ProfileScreen(username)
        }
    }
}