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
import com.metropolia.eatthefrog.constants.ALL_UID
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.database.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


/**
 * ViewModel for HistoryScreen. Contains the logic to handle data required within the HistoryScreen.
 */
class HistoryScreenViewModel(application: Application) : TasksViewModel(application) {

    private val database = InitialDB.get(application)
    val all = (TaskType(uid = ALL_UID, name = application.getString(R.string.all), icon = null))
    var selectedTypes = MutableLiveData(listOf(all))
    var showTaskDoneConfirmWindow = mutableStateOf(false)
    var searchInput = mutableStateOf("")

    /**
     * Acquire data from Room db.
     */
    fun getCompletedTasks() = database.taskDao().getAllCompletedTasksOrderedByDate("%${searchInput.value}")
    fun getIncompleteTasks() = database.taskDao().getAllIncompleteTasksOrderedByDate("%${searchInput.value}")
    fun getSelectedTask() = database.taskDao().getSpecificTask(highlightedTaskId.value)
    fun getHighlightedSubtasks() = database.subtaskDao().getSubtasks(highlightedTaskId.value)
    fun getAllTaskTypes() = database.taskTypeDao().getTaskTypes()

    fun updateSearchInput(string: String) {
        searchInput.value = string
    }

    /**
     * Attempts to convert the given string to a Date object. Returns default current date if an error is thrown.
     */
    fun parseStringToDate(string: String): Date {
        var date = Date()
        try {
            date = SimpleDateFormat(DATE_FORMAT).parse(string)
        } catch (e: Exception) {
            Log.d("Failed to parse string to Date", e.message.toString())
        }
        return date
    }

    /**
     * Clears the HistorySearchContainer input.
     */
    fun clearInput() {
        searchInput.value = ""
    }

    /**
     * Adds or removes the given TaskType object from the selectedType -list. If it is empty, add a default "All" TaskType object.
     */
    fun toggleSelectedType(type: TaskType) {

        val newList : MutableList<TaskType> = selectedTypes.value?.toMutableList() ?: mutableListOf()

        if (type.name == all.name) {
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

    /**
     * Closes the HistoryScreenTaskPopup.
     */
    fun resetPopupStatus() {
        popupVisible.value = false
    }

    /**
     * Updates the SubTasks to be displayed in the HistoryScreenTaskPopup.
     */
    fun updateSubTask(st: Subtask, status: Boolean) {
        viewModelScope.launch {
            database.subtaskDao().updateSubtaskCompletedStatus(st.uid, status)
        }
    }

    /**
     * Close ConfirmWindow.
     */
    fun closeTaskConfirmWindow() {
        showTaskDoneConfirmWindow.value = false
    }

    /**
     * Open ConfirmWindow.
     */
    fun openTaskConfirmWindow() {
        showTaskDoneConfirmWindow.value = true
    }

    /**
     * Toggles the completion status of the selected task.
     */
    fun toggleTaskCompleted() {
        viewModelScope.launch {
            database.taskDao().toggleTask(highlightedTaskId.value)
            closeTaskConfirmWindow()
        }
    }
}