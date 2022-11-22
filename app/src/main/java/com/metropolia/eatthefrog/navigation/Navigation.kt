package com.metropolia.eatthefrog.navigation

import android.app.Application
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.metropolia.eatthefrog.database.Subtask
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
        composable("add_task/{taskUid}/{isEdit}/{taskTitle}/{taskDesc}/{dateDeadline}/{timeDeadline}/{taskType}", arguments = listOf(
            navArgument(name = "taskUid"){
                type = NavType.LongType
                defaultValue = 0
            },
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
            },
            navArgument(name = "taskType") {
                type = NavType.StringType
                defaultValue = ""
            }
        )
        ) { navBackStackEntry ->
            navBackStackEntry.arguments!!.getString("dateDeadline")?.let {
                navBackStackEntry.arguments!!.getString("timeDeadline")?.let { it1 ->
                    AddTaskScreen(
                        application = application, navHost = navController,
                        editTaskID = navBackStackEntry.arguments!!.getLong("taskUid"),
                        isEditMode = navBackStackEntry.arguments!!.getBoolean("isEdit"),
                        editTitle = navBackStackEntry.arguments!!.getString("taskTitle"),
                        editDesc = navBackStackEntry.arguments!!.getString("taskDesc"),
                        dateDeadline = it,
                        timeDeadline = it1,
                        editTaskType = navBackStackEntry.arguments!!.getString("taskType")
                        )
                }
            }
        }
        composable(NavigationItem.Profile.route) {
            ProfileScreen(username, application)
        }
        composable(NavigationItem.History.route) {
            HistoryScreen(application, navController)
        }
    }
}