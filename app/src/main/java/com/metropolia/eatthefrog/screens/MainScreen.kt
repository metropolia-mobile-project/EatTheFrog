package com.metropolia.eatthefrog.screens

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.metropolia.eatthefrog.navigation.Navigation
import com.metropolia.eatthefrog.navigation.components.BottomNavigationBar

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