package com.metropolia.eatthefrog.navigation

import android.app.Application
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.metropolia.eatthefrog.screens.HomeScreen
import com.metropolia.eatthefrog.screens.ProfileScreen
import com.metropolia.eatthefrog.screens.addTask.AddTaskScreen

@ExperimentalMaterialApi
@Composable
fun Navigation(navController: NavHostController, username: String, application: Application) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen(username, application, navController)
        }
        composable("add_task/{isEdit}/{taskTitle}/{taskDesc}/{dateDeadline}/{timeDeadline}", arguments = listOf(
            navArgument(name = "isEdit") {
                type = NavType.BoolType
                defaultValue = false
            },
            navArgument(name = "taskTitle") {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(name = "taskDesc") {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(name = "dateDeadline") {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(name = "timeDeadline") {
                type = NavType.StringType
                defaultValue = ""
            }
        )
        ) { navBackStackEntry ->
            AddTaskScreen(
                application = application, navHost = navController,
                isEditMode = navBackStackEntry.arguments!!.getBoolean("isEdit"),
                editTitle = navBackStackEntry.arguments!!.getString("taskTitle"),
                editDesc = navBackStackEntry.arguments!!.getString("taskDesc"),
                dateDeadline = navBackStackEntry.arguments!!.getString("dateDeadline"),
                timeDeadline = navBackStackEntry.arguments!!.getString("timeDeadline"),

            )
        }
        composable(NavigationItem.Profile.route) {
            ProfileScreen(username, application)
        }
    }
}