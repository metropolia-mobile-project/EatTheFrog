package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.constants.PROFILE_IMAGE_KEY
import com.metropolia.eatthefrog.constants.SHARED_PREF_KEY
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.services.APIService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class DateFilter {
    TODAY,
    WEEK,
    MONTH
}

/**
 * ViewModel for the Home screen
 */
class HomeScreenViewModel(application: Application) : TasksViewModel(application) {

    private val database = InitialDB.get(application)
    private val service = APIService.service

    private val sdf = SimpleDateFormat(DATE_FORMAT)
    val today: String = sdf.format(Date())


    var searchVisible = MutableLiveData(false)

    var showTaskDoneConfirmWindow = mutableStateOf(false)
    var showFrogConfirmWindow = mutableStateOf(false)
    var showQuoteToast = mutableStateOf(false)
    val dailyFrogSelected = MutableLiveData(false)
    var showFrogCompletedScreen = MutableLiveData(false)
    var searchInput = mutableStateOf("")
    var quote = APIService.Result("", "", "")

    fun getTasks() = database.taskDao().getAllTasks("%${searchInput.value}")
    fun getSelectedTask() = database.taskDao().getSpecificTask(highlightedTaskId.value)
    fun getDateTaskCount(date: String) = database.taskDao().getDateTaskCount(date)
    fun getHighlightedSubtasks() = database.subtaskDao().getSubtasks(highlightedTaskId.value)

    init {
        if (quote.q.isEmpty()) {
            viewModelScope.launch {
                quote = try {
                    service.getRandomMotivationalQuote()[0]
                } catch (e: Exception) {
                    Log.d("API fetch failed", e.message.toString())
                    APIService.Result("JUST DO IT!", "Shia LaBeouf", "")
                }
            }
        }
    }

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

    fun toggleTaskCompleted(task: Task?) {
        viewModelScope.launch {
            database.taskDao().toggleTask(highlightedTaskId.value)
            closeTaskConfirmWindow()

            if ((task?.isFrog == true && !showQuoteToast.value) && !task.completed) {
                popupVisible.value = false
                openFrogCompletedScreen()
                showQuoteToast.value = true
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

    fun loadProfilePicture() : String? {
        val sharedPreferences = app.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PROFILE_IMAGE_KEY, null)
    }
}