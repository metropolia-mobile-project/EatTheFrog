package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
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

    val selectedFilter = MutableLiveData(DateFilter.TODAY)
    var popupVisible = mutableStateOf(false)
    var highlightedTaskId = mutableStateOf(0L)
    var showTaskDoneConfirmWindow = mutableStateOf(false)
    var showFrogConfirmWindow = mutableStateOf(false)
    val dailyFrogSelected = MutableLiveData(false)

    fun getTasks() = database.taskDao().getAllTasks()
    fun getSelectedTask() = database.taskDao().getSpecificTask(highlightedTaskId.value)
    fun getDateTaskCount(date: String) = database.taskDao().getDateTaskCount(date)
    fun getHighlightedSubtasks() = database.subtaskDao().getSubtasks(highlightedTaskId.value)
    fun getSubtasksAmount(id: Long) = database.subtaskDao().getSubtasksAmount(id)

    fun selectDateFilter(dateFilter: DateFilter) {
        selectedFilter.postValue(dateFilter)
    }

    fun showPopup() {
        popupVisible.value = true
    }

    fun resetPopupStatus() {
        popupVisible.value = false
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

    fun toggleTaskCompleted() {
        viewModelScope.launch {
            database.taskDao().toggleTask(highlightedTaskId.value)
            closeTaskConfirmWindow()
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
            database.taskDao().toggleFrog(highlightedTaskId.value)
            closeFrogConfirmWindow()
        }
    }

}