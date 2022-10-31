package com.metropolia.eatthefrog.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.metropolia.eatthefrog.screens.AddTaskScreen
import com.metropolia.eatthefrog.screens.HomeScreen
import com.metropolia.eatthefrog.screens.ProfileScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen()
        }
        composable(NavigationItem.AddTask.route) {
            AddTaskScreen()
        }
        composable(NavigationItem.Profile.route) {
            ProfileScreen()
        }
    }
}