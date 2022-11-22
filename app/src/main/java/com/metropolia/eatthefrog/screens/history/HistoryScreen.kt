package com.metropolia.eatthefrog.screens.history

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.metropolia.eatthefrog.screens.history.components.CompletedTasksContainer
import com.metropolia.eatthefrog.screens.history.components.HistoryScreenContainer
import com.metropolia.eatthefrog.screens.history.components.HistoryScreenTaskPopup
import com.metropolia.eatthefrog.viewmodels.HistoryScreenViewModel


/**
 * Displays completed tasks, as well as tasks which are past due date and incomplete.
 */
@Composable
fun HistoryScreen(application: Application, navController: NavController) {

    val vm = HistoryScreenViewModel(application)

    HistoryScreenContainer(vm)
    HistoryScreenTaskPopup(vm, navController)
}