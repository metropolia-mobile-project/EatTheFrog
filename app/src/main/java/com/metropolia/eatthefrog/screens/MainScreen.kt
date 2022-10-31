package com.metropolia.eatthefrog.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.metropolia.eatthefrog.PopupView
import com.metropolia.eatthefrog.navigation.Navigation
import com.metropolia.eatthefrog.navigation.components.BottomNavigationBar

@ExperimentalMaterialApi
@Composable
fun MainScreen(username: String) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        content = { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Navigation(navController = navController, username)
        }
    },
    backgroundColor = MaterialTheme.colors.surface)
}