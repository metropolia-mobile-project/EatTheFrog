package com.metropolia.eatthefrog.screens.history

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.metropolia.eatthefrog.screens.history.components.HistoryScreenContainer
import com.metropolia.eatthefrog.screens.history.components.HistoryScreenTaskPopup
import com.metropolia.eatthefrog.viewmodels.HistoryScreenViewModel


/**
 * Displays completed tasks, as well as tasks which are past due date and incomplete.
 * @param application: context of the application
 * @param navController: NavController of the application
 */
@Composable
fun HistoryScreen(application: Application, navController: NavController) {

    val vm = HistoryScreenViewModel(application)

    HistoryScreenContainer(vm)
    HistoryScreenTaskPopup(vm, navController)
}