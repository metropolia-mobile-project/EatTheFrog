package com.metropolia.eatthefrog.activities

import android.content.Context
import android.content.SharedPreferences
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
import com.metropolia.eatthefrog.constants.DARK_MODE_KEY
import com.metropolia.eatthefrog.constants.SHARED_PREF_KEY
import com.metropolia.eatthefrog.constants.USERNAME_KEY
import com.metropolia.eatthefrog.screens.MainScreen
import com.metropolia.eatthefrog.screens.WelcomeScreen
import com.metropolia.eatthefrog.ui.theme.EatTheFrogTheme

open class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        val sharedPreferences = this.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(USERNAME_KEY, null)
        var darkmode = mutableStateOf(sharedPreferences.getBoolean(DARK_MODE_KEY, false))

        sharedPreferences.registerOnSharedPreferenceChangeListener { prefs, key ->
            if (key == DARK_MODE_KEY) darkmode.value = prefs.getBoolean(DARK_MODE_KEY, false)
        }
        
        setContent {
            ProvideWindowInsets {
                EatTheFrogTheme(settingsDarkMode = darkmode.value) {
                    if (username == null) {
                        WelcomeScreen(application, this)
                    } else MainScreen(username, application)
                }
            }
        }
    }
}