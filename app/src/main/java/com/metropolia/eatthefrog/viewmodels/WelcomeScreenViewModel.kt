package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.SHARED_PREF_KEY
import com.metropolia.eatthefrog.constants.USERNAME_KEY
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.TaskType
import kotlinx.coroutines.launch

/**
 * ViewModel for the Welcome screen
 */

class WelcomeScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
    private val database = InitialDB.get(application)
    var endReached = mutableStateOf(false)
    private val taskTypeDao = database.taskTypeDao()

    fun savePreferences(username: String) {
        with (sharedPreferences.edit()) {
            putString(USERNAME_KEY, username)
            apply()
        }
    }

    fun toggleEndReached() {
        endReached.value = true
    }

    fun saveInitialTypes() {
        val meeting     = TaskType(name = getApplication<Application>().resources.getString(R.string.meeting), icon = R.drawable.ic_meeting)
        val planning    = TaskType(name = getApplication<Application>().resources.getString(R.string.planning), icon = R.drawable.ic_planning)
        val development = TaskType(name = getApplication<Application>().resources.getString(R.string.development), icon = R.drawable.ic_development)
        viewModelScope.launch {
            taskTypeDao.insertTaskType(meeting)
            taskTypeDao.insertTaskType(planning)
            taskTypeDao.insertTaskType(development)
        }
    }
}