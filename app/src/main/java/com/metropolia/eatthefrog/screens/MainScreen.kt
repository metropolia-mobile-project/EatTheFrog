package com.metropolia.eatthefrog.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.metropolia.eatthefrog.navigation.Navigation
import com.metropolia.eatthefrog.navigation.components.BottomNavigationBar

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

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen("John Doe")
}