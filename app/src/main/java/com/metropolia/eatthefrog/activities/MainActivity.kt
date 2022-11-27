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
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import com.google.accompanist.insets.ProvideWindowInsets
import com.metropolia.eatthefrog.constants.DARK_MODE_KEY
import com.metropolia.eatthefrog.constants.SHARED_PREF_KEY
import com.metropolia.eatthefrog.constants.USERNAME_KEY
import com.metropolia.eatthefrog.screens.MainScreen
import com.metropolia.eatthefrog.screens.WelcomeScreen
import com.metropolia.eatthefrog.ui.theme.EatTheFrogTheme

open class MainActivity : ComponentActivity() {

    var darkmode = MutableLiveData(false)

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val sharedPreferences = this.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(USERNAME_KEY, null)
        darkmode.value = sharedPreferences.getBoolean(DARK_MODE_KEY, false)
        
        setContent {
            val dm = darkmode.observeAsState()
            ProvideWindowInsets {
                EatTheFrogTheme(settingsDarkMode = dm.value ?: false) {
                    if (username == null) {
                        WelcomeScreen(application, this)
                    } else MainScreen(username, application)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = this.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener { a, s ->
            if (s == DARK_MODE_KEY) {
                darkmode.value = a.getBoolean(DARK_MODE_KEY, false)
            }
            Log.d("DARKMODE_CHANGED", darkmode.value.toString())
        }
    }
}