package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.metropolia.eatthefrog.activities.SHARED_PREF_KEY
import com.metropolia.eatthefrog.activities.USERNAME_KEY

class WelcomeScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)

    fun savePreferences(username: String) {
        with (sharedPreferences.edit()) {
            putString(USERNAME_KEY, username)
            apply()
        }
    }
}