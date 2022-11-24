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

@ExperimentalMaterialApi
@Composable
fun MainScreen(username: String, application: Application) {

    val navController = rememberNavController()
    /*val builder = NotificationCompat.Builder(LocalContext.current, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_frog_cropped)
        .setContentTitle("This is the title")
        .setContentText("This is the text")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()*/

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        content = { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Navigation(navController = navController, username, application)
        }
    },
    backgroundColor = MaterialTheme.colors.surface)

    /*NotificationManagerCompat.from(LocalContext.current).notify(0, builder)
    with(NotificationManagerCompat.from(LocalContext.current)) {
        notify(0, builder)
    }*/
}