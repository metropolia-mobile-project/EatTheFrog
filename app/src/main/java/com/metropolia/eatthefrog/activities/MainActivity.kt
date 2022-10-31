package com.metropolia.eatthefrog.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.ProvideWindowInsets
import com.metropolia.eatthefrog.screens.MainScreen
import com.metropolia.eatthefrog.screens.WelcomeScreen
import com.metropolia.eatthefrog.ui.theme.EatTheFrogTheme

const val SHARED_PREF_KEY = "PREFERENCES_KEY"
const val USERNAME_KEY = "USERNAME_KEY"

class MainActivity : ComponentActivity() {
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EatTheFrogTheme {
        MainScreen("John Doe")
    }
}