package com.metropolia.eatthefrog.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.metropolia.eatthefrog.PopupView
import com.metropolia.eatthefrog.screens.MainScreen
import com.metropolia.eatthefrog.ui.theme.EatTheFrogTheme

class MainActivity : ComponentActivity() {

    var popupVisible = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EatTheFrogTheme {
                MainScreen()
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                ) {

                    Button(onClick = {  popupVisible.value = !popupVisible.value }) {
                        Text("Open test popup")
                    }

                    Column(Modifier.fillMaxSize()) {
                        PopupView(200, popupVisible) {
                            Text("Popup window test")
                        }
                    }
                    

                }
            }
        }
    }
}