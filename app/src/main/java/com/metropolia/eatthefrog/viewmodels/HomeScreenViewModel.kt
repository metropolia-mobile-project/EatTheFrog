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

/**
 * Enums used for filtering displayed Tasks according to current day, week, or month.
 */
enum class DateFilter {
    TODAY,
    WEEK,
    MONTH
}


/**
 * ViewModel for the HomeScreen.
 * @param application: Application context.
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

    /**
     * Fetch all Task objects from Room db which contain the searchInput value.
     * @return List of Task objects wrapped within a LiveData object.
     */
    fun getTasks() = database.taskDao().getAllTasks("%${searchInput.value}")

    /**
     * Fetch the highlighted Task from Room db.
     * @return Task object wrapped within a LiveData object.
     */
    fun getSelectedTask() = database.taskDao().getSpecificTask(highlightedTaskId.value)

    /**
     * Fetches the amount of Tasks the given date contains.
     * @return number of Tasks (Int) wrapped within a LiveData object.
     */
    fun getDateTaskCount(date: String) = database.taskDao().getDateTaskCount(date)

    /**
     * Fetch the highlighted Subtasks from Room db.
     * @return List of Subtask objects wrapped within a LiveData object.
     */
    fun getHighlightedSubtasks() = database.subtaskDao().getSubtasks(highlightedTaskId.value)

    /**
     * Sets the given DateFilter object to the selecteFilter LiveData object.
     * @param dateFilter: DateFilter object to be set.
     */
    fun selectDateFilter(dateFilter: DateFilter) {
        selectedFilter.postValue(dateFilter)
    }

    /**
     * Closes FrogCompletedScreen.
     */
    fun closeFrogCompletedScreen() {
        showFrogCompletedScreen.value = false
    }

    /**
     * Open FrogCompletedScreen.
     */
    private fun openFrogCompletedScreen() {
        showFrogCompletedScreen.value = true
    }

    /**
     * Hide PopupScreen.
     */
    fun resetPopupStatus() {
        popupVisible.value = false
    }

    /**
     * Display Search TextField.
     */
    fun showSearch() {
        searchVisible.value = true
    }

    /**
     * Hide Search TextField.
     */
    fun closeSearch() {
        searchVisible.value = false
        searchInput.value = ""
    }

    /**
     * Updates the searchInput value.
     * @param input: String value to be set to searchInput.
     */
    fun updateSearchInput(input: String) {
        searchInput.value = input
    }

    /**
     * Updates the given Subtask within Room db.
     * @param st: Subtask to be updated
     * @param status: status of the Subtask to be set in Room db.
     */
    fun updateSubTask(st: Subtask, status: Boolean) {
        viewModelScope.launch {
            database.subtaskDao().updateSubtaskCompletedStatus(st.uid, status)
        }
    }

    /**
     * Close TaskConfirmWindow.
     */
    fun closeTaskConfirmWindow() {
        showTaskDoneConfirmWindow.value = false
    }

    /**
     * Open TaskConfirmWindow.
     */
    fun openTaskConfirmWindow() {
        showTaskDoneConfirmWindow.value = true
    }

    /**
     * Toggles the completion status of the Task object.
     * @param task: Task to be modified.
     * @param context: Context of the application.
     */
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

    /**
     * Open FrogConfirmWindow.
     */
    fun openFrogConfirmWindow() {
        showFrogConfirmWindow.value = true
    }

    /**
     * Close FrogConfirmWindow.
     */
    fun closeFrogConfirmWindow() {
        showFrogConfirmWindow.value = false
    }

    /**
     * Toggles the isFrog status of the highlighted Task.
     */
    fun toggleTaskFrog() {
        viewModelScope.launch {
            database.taskDao().toggleFrog(today, highlightedTaskId.value)
            closeFrogConfirmWindow()
        }
    }

    /**
     * Fetches the profile image URI from SharedPreferences.
     * @return URI where the Image is stored, in String format.
     */
    fun loadProfilePicture(): String? {
        return sharedPreferences.getString(PROFILE_IMAGE_KEY, null)
    }

    /**
     * Fetches the currentStreak value from SharedPreferences and sets it to the currentStreak value.
     */
    private fun setCurrentStreak() {
        currentStreak.value = sharedPreferences.getInt(CURRENT_STREAK_KEY, 0)
    }

    /**
     * Fetches the longestStreak value from SharedPreferences and sets it to the longestStreak value.
     */
    private fun setLongestStreak() {
        longestStreak.value = sharedPreferences.getInt(LONGEST_STREAK_KEY, 0)
    }

    /**
     * Saves the status of the Streak.
     */
    private fun saveStreakStatus() {
        checkIfStreakContinues()
        setCurrentStreak()
        setLongestStreak()
    }

    /**
     * Check if the currentStreak continues.
     */
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

    /**
     * Increment currentStreak by 1 and save it to sharedPreferences.
     */
    private fun advanceStreak() {
        val currentStreak = sharedPreferences.getInt(CURRENT_STREAK_KEY, 0)
        with(sharedPreferences.edit()) {
            putString(LATEST_EATEN_FROG_KEY, dtf.format(LocalDate.now()))
            putInt(CURRENT_STREAK_KEY, currentStreak + 1)
            apply()
        }
    }

    /**
     * Sets the currentStreak to 1 and save it to SharedPreferences.
     */
    private fun resetStreak() {
        with(sharedPreferences.edit()) {
            putInt(CURRENT_STREAK_KEY, 1)
            putString(LATEST_EATEN_FROG_KEY, dtf.format(LocalDate.now()))
            apply()
        }
    }

    /**
     * Updates the longestStreak and save it to SharedPreferences.
     */
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