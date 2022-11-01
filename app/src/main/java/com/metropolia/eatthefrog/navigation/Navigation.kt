package com.metropolia.eatthefrog.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.metropolia.eatthefrog.screens.AddTaskScreen
import com.metropolia.eatthefrog.screens.HomeScreen
import com.metropolia.eatthefrog.screens.ProfileScreen

@ExperimentalMaterialApi
@Composable
fun Navigation(navController: NavHostController, username: String) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen(username)
        }
        composable(NavigationItem.AddTask.route) {
            AddTaskScreen()
        }
        composable(NavigationItem.Profile.route) {
            ProfileScreen(username)
        }
    }
}