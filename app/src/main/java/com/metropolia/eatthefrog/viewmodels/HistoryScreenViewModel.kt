package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.database.*
import kotlinx.coroutines.launch


/**
 * ViewModel for HistoryScreen
 */
class HistoryScreenViewModel(application: Application) : TasksViewModel(application) {

    private val database = InitialDB.get(application)
    private val all = application.getString(R.string.all)
    var selectedTypes = MutableLiveData(listOf(all))
    var showTaskDoneConfirmWindow = mutableStateOf(false)
    var searchInput = mutableStateOf("")

    fun getCompletedTasks() = database.taskDao().getAllCompletedTasksOrderedByDate("%${searchInput.value}")
    fun getIncompleteTasks() = database.taskDao().getAllIncompleteTasksOrderedByDate("%${searchInput.value}")
    fun getSelectedTask() = database.taskDao().getSpecificTask(highlightedTaskId.value)
    fun getHighlightedSubtasks() = database.subtaskDao().getSubtasks(highlightedTaskId.value)

    // TODO: Change implementation to fetch from db/wherever the custom TaskTypes are stored.
    fun getAllTaskTypes() = TaskType.values()

    fun updateSearchInput(string: String) {
        searchInput.value = string
    }

    fun clearInput() {
        searchInput.value = ""
    }

    fun toggleSelectedType(type: String) {

        var newList : MutableList<String> = selectedTypes.value?.toMutableList() ?: mutableListOf()

        if (type == all) {
            selectedTypes.value = listOf(all)
            return
        } else {
            if (selectedTypes.value?.contains(type) == true) {
                newList.remove(type)
            } else {
                newList.add(type)
            }
        }

        if (newList.isEmpty()) newList.add(all)
        else newList.remove(all)

        selectedTypes.value = newList.toList()
    }

    fun resetPopupStatus() {
        popupVisible.value = false
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