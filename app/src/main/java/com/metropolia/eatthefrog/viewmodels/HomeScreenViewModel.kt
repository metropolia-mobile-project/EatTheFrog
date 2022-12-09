package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.constants.*
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.notification.cancelAlarmForStreak
import com.metropolia.eatthefrog.notification.cancelAlarmForTask
import com.metropolia.eatthefrog.services.APIService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

enum class DateFilter {
    TODAY,
    WEEK,
    MONTH
}

/**
 * ViewModel for the Home screen
 */
open class HomeScreenViewModel(application: Application) : TasksViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        app.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)

    private val database = InitialDB.get(application)
    val service = APIService

    private val sdf = SimpleDateFormat(DATE_FORMAT)
    private val dtf = DateTimeFormatter.ofPattern(DATE_FORMAT)
    val today: String = sdf.format(Date())

    var searchVisible = MutableLiveData(false)

    var showTaskDoneConfirmWindow = mutableStateOf(false)
    var showFrogConfirmWindow = mutableStateOf(false)
    var showQuoteToast = mutableStateOf(false)
    val dailyFrogSelected = MutableLiveData(false)
    var showFrogCompletedScreen = MutableLiveData(false)
    var searchInput = mutableStateOf("")
    var currentStreak = MutableLiveData(sharedPreferences.getInt(CURRENT_STREAK_KEY, 0))
    var longestStreak = MutableLiveData(sharedPreferences.getInt(LONGEST_STREAK_KEY, 0))

    fun getTasks() = database.taskDao().getAllTasks("%${searchInput.value}")
    fun getSelectedTask() = database.taskDao().getSpecificTask(highlightedTaskId.value)
    fun getDateTaskCount(date: String) = database.taskDao().getDateTaskCount(date)
    fun getHighlightedSubtasks() = database.subtaskDao().getSubtasks(highlightedTaskId.value)

    fun selectDateFilter(dateFilter: DateFilter) {
        selectedFilter.postValue(dateFilter)
    }

    fun closeFrogCompletedScreen() {
        showFrogCompletedScreen.value = false
    }

    private fun openFrogCompletedScreen() {
        showFrogCompletedScreen.value = true
    }

    fun resetPopupStatus() {
        popupVisible.value = false
    }

    fun showSearch() {
        searchVisible.value = true
    }

    fun closeSearch() {
        searchVisible.value = false
        searchInput.value = ""
    }

    fun updateSearchInput(input: String) {
        searchInput.value = input
    }

    fun updateSubTask(st: Subtask, status: Boolean) {
        viewModelScope.launch {
            database.subtaskDao().updateSubtaskCompletedStatus(st.uid, status)
        }
    }

    fun closeTaskConfirmWindow() {
        showTaskDoneConfirmWindow.value = false
    }

    fun openTaskConfirmWindow() {
        showTaskDoneConfirmWindow.value = true
    }

    fun toggleTaskCompleted(task: Task?, context: Context) {
        viewModelScope.launch {
            database.taskDao().toggleTask(highlightedTaskId.value)
            closeTaskConfirmWindow()

            if ((task?.isFrog == true && !showQuoteToast.value) && !task.completed) {
                Log.d("STEAK currentStreak ", currentStreak.value.toString())
                currentStreak.value?.let { cancelAlarmForStreak(it-1, context) }
                saveStreakStatus()
                popupVisible.value = false
                openFrogCompletedScreen()
                showQuoteToast.value = true
            }

            if (task != null && !task.completed) {
                cancelAlarmForTask(task, context)
            }
        }
    }

    fun openFrogConfirmWindow() {
        showFrogConfirmWindow.value = true
    }

    fun closeFrogConfirmWindow() {
        showFrogConfirmWindow.value = false
    }

    fun toggleTaskFrog() {
        viewModelScope.launch {
            database.taskDao().toggleFrog(today, highlightedTaskId.value)
            closeFrogConfirmWindow()
        }
    }

    fun loadProfilePicture(): String? {
        return sharedPreferences.getString(PROFILE_IMAGE_KEY, null)
    }

    private fun setCurrentStreak() {
        currentStreak.value = sharedPreferences.getInt(CURRENT_STREAK_KEY, 0)
    }

    private fun setLongestStreak() {
        longestStreak.value = sharedPreferences.getInt(LONGEST_STREAK_KEY, 0)
    }

    private fun saveStreakStatus() {
        checkIfStreakContinues()
        setCurrentStreak()
        setLongestStreak()
    }

    private fun checkIfStreakContinues() {
        val latestEatenFrog = sharedPreferences.getString(LATEST_EATEN_FROG_KEY, null)
        if (latestEatenFrog == null) {
            advanceStreak()
            updateLongestStreak()
            return
        }

        val latestDate = LocalDate.parse(latestEatenFrog, dtf).atStartOfDay()
        val todayDate = LocalDate.now().atStartOfDay()

        val durationBetweenDates = Duration.between(latestDate, todayDate).toDays()
        if (durationBetweenDates.toInt() == 1) {
            advanceStreak()
            updateLongestStreak()
            return
        }

        // Check so you can't break the streak by changing frog during the same day
        if (durationBetweenDates.toInt() == 0) {
            return
        }

        resetStreak()
    }

    private fun advanceStreak() {
        val currentStreak = sharedPreferences.getInt(CURRENT_STREAK_KEY, 0)
        with(sharedPreferences.edit()) {
            putString(LATEST_EATEN_FROG_KEY, dtf.format(LocalDate.now()))
            putInt(CURRENT_STREAK_KEY, currentStreak + 1)
            apply()
        }
    }

    private fun resetStreak() {
        with(sharedPreferences.edit()) {
            putInt(CURRENT_STREAK_KEY, 1)
            putString(LATEST_EATEN_FROG_KEY, dtf.format(LocalDate.now()))
            apply()
        }
    }

    private fun updateLongestStreak() {
        val currentStreak = sharedPreferences.getInt(CURRENT_STREAK_KEY, 0)
        val longestStreak = sharedPreferences.getInt(LONGEST_STREAK_KEY, 0)
        if (currentStreak > longestStreak) {
            with(sharedPreferences.edit()) {
                putInt(LONGEST_STREAK_KEY, currentStreak)
                apply()
            }
        }
    }
}