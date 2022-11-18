package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.services.APIService
import es.dmoral.toasty.Toasty
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
class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val database = InitialDB.get(application)
    private val service = APIService.service

    private val sdf = SimpleDateFormat(DATE_FORMAT)
    val today: String = sdf.format(Date())

    val selectedFilter = MutableLiveData(DateFilter.TODAY)
    var popupVisible = MutableLiveData(false)
    var searchVisible = MutableLiveData(false)
    var highlightedTaskId = mutableStateOf(0L)
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
    fun getSubtasksAmount(id: Long) = database.subtaskDao().getSubtasksAmount(id)

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

    fun openFrogCompletedScreen() {
        showFrogCompletedScreen.value = true
    }

    fun showPopup() {
        popupVisible.value = true
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

    fun updateHighlightedTask(t: Task) {
        this.highlightedTaskId.value = t.uid
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
}