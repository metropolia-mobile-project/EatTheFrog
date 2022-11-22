package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import kotlinx.coroutines.launch


/**
 * ViewModel for HistoryScreen
 */
class HistoryScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val database = InitialDB.get(application)

    val selectedFilter = MutableLiveData(DateFilter.TODAY)
    var popupVisible = MutableLiveData(false)
    var highlightedTaskId = mutableStateOf(0L)
    var showTaskDoneConfirmWindow = mutableStateOf(false)
    var searchInput = mutableStateOf("")

    fun getCompletedTasks() = database.taskDao().getAllCompletedTasksOrderedByDate("%${searchInput.value}")
    fun getIncompleteTasks() = database.taskDao().getAllIncompleteTasksOrderedByDate("%${searchInput.value}")
    fun getSelectedTask() = database.taskDao().getSpecificTask(highlightedTaskId.value)
    fun getHighlightedSubtasks() = database.subtaskDao().getSubtasks(highlightedTaskId.value)
    fun getSubtasksAmount(id: Long) = database.subtaskDao().getSubtasksAmount(id)

    fun updateSearchInput(string: String) {
        searchInput.value = string
    }

    fun clearInput() {
        searchInput.value = ""
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
}