package com.metropolia.eatthefrog.screens

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.rememberNavController
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.CHANNEL_ID
import androidx.navigation.compose.rememberNavController
import com.metropolia.eatthefrog.navigation.Navigation
import com.metropolia.eatthefrog.navigation.components.BottomNavigationBar
import com.metropolia.eatthefrog.notification.Scheduler
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel
import com.metropolia.eatthefrog.viewmodels.NotificationsViewModel

/**
 * Base screen component that holds the navigation and other screens in it.
 */

@ExperimentalMaterialApi
@Composable
fun MainScreen(username: String, application: Application) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        content = { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Navigation(navController = navController, username, application)
        }
    },
    backgroundColor = MaterialTheme.colors.surface)
}