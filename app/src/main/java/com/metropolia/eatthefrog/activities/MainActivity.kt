package com.metropolia.eatthefrog.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import com.google.accompanist.insets.ProvideWindowInsets
import com.metropolia.eatthefrog.screens.MainScreen
import com.metropolia.eatthefrog.screens.WelcomeScreen
import com.metropolia.eatthefrog.ui.theme.EatTheFrogTheme

const val SHARED_PREF_KEY = "PREFERENCES_KEY"
const val USERNAME_KEY = "USERNAME_KEY"

open class MainActivity : ComponentActivity() {

    var popupVisible = mutableStateOf(false)

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        val sharedPreferences = this.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(USERNAME_KEY, null)

        setContent {
            ProvideWindowInsets {
                EatTheFrogTheme {
                    if (username == null) {
                        WelcomeScreen(application, this)
                    } else MainScreen(username)
                }
            }
        }
    }
}