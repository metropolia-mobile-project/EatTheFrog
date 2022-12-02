package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.MutableLiveData
import com.metropolia.eatthefrog.R
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
    private val stringGetter = getApplication<Application>().resources
    fun getCertainTask(id: Long) = database.taskDao().getTest(id)

    private val sharedPreferences: SharedPreferences = application
        .getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
    private val dtf = DateTimeFormatter.ofPattern(DATE_FORMAT)

    val tomorrow: String = dtf.format(LocalDateTime.now().plusDays(1))
    var deadlineValue = MutableLiveData(0)

    val listItems = listOf(
        stringGetter.getString(R.string.notif_at_deadline),
        stringGetter.getString(R.string.notif_5before),
        stringGetter.getString(R.string.notif_10before),
        stringGetter.getString(R.string.notif_30before),
        stringGetter.getString(R.string.notif_hour_before)
    )

    val minutes = mapOf(
        stringGetter.getString(R.string.notif_5before) to 5,
        stringGetter.getString(R.string.notif_10before) to 10,
        stringGetter.getString(R.string.notif_30before) to 30
    )

    val hours = mapOf(
        stringGetter.getString(R.string.notif_hour_before) to 1
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
