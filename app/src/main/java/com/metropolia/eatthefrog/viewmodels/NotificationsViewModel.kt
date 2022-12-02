package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.constants.NOTIFICATION_KEY
import com.metropolia.eatthefrog.constants.SHARED_PREF_KEY
import com.metropolia.eatthefrog.constants.TIME_FORMAT
import com.metropolia.eatthefrog.database.InitialDB
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NotificationsViewModel(application: Application) : HomeScreenViewModel(application) {
    private val database = InitialDB.get(application)
    fun getCertainTask(id: Long) = database.taskDao().getTest(id)

    private val sharedPreferences: SharedPreferences = application
        .getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
    private val dtf = DateTimeFormatter.ofPattern(DATE_FORMAT)

    val tomorrow: String = dtf.format(LocalDateTime.now().plusDays(1))
    var deadlineValue = MutableLiveData(0)

    val listItems = listOf(
        "At deadline",
        "5 mins before",
        "10 mins before",
        "30 min before",
        "1 hour before"
    )

    val minutes = mapOf(
        "At deadline" to 0,
        "5 mins before" to 5,
        "10 mins before" to 10,
        "30 min before" to 30
    )

    val hours = mapOf(
        "1 hour before" to 1
    )

    init {
        deadlineValue.value = getDeadlineFromPreferences(NOTIFICATION_KEY, 0)
    }

    private fun getDeadlineFromPreferences(key: String, default: Int): Int {
        val prefs = app.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        return prefs.getInt(key, default)
    }

    fun saveDeadlineValue() {
        with (sharedPreferences.edit()) {
            putInt(NOTIFICATION_KEY, deadlineValue.value!!)
            apply()
        }
    }
}
