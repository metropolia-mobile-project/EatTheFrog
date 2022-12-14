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
 * ViewModel for HistoryScreen.
 * @param application: Application context.
 */
class HistoryScreenViewModel(application: Application) : TasksViewModel(application) {

    private val database = InitialDB.get(application)
    val all = (TaskType(uid = ALL_UID, name = application.getString(R.string.all), icon = null))
    var selectedTypes = MutableLiveData(listOf(all))
    var showTaskDoneConfirmWindow = mutableStateOf(false)
    var searchInput = mutableStateOf("")

    /**
     * Fetch all completed tasks from Room db which contain the searchInput value.
     * @return List of Task objects wrapped within a LiveData object.
     */
    fun getCompletedTasks() = database.taskDao().getAllCompletedTasksOrderedByDate("%${searchInput.value}")

    /**
     * Fetch all incomplete tasks from Room db which contain the searchInput value.
     * @return List of Task objects wrapped within a LiveData object.
     */
    fun getIncompleteTasks() = database.taskDao().getAllIncompleteTasksOrderedByDate("%${searchInput.value}")

    /**
     * Fetch the highlighted Task from Room db.
     * @return Task object wrapped within a LiveData object.
     */
    fun getSelectedTask() = database.taskDao().getSpecificTask(highlightedTaskId.value)

    /**
     * Fetch the highlighted Subtasks from Room db.
     * @return List of Subtask objects wrapped within a LiveData object.
     */
    fun getHighlightedSubtasks() = database.subtaskDao().getSubtasks(highlightedTaskId.value)

    /**
     * Fetches all TaskTypes from Room db.
     * @return List of TaskType objects wrapped within a LiveData object.
     */
    fun getAllTaskTypes() = database.taskTypeDao().getTaskTypes()

    /**
     * Update searchInput with the given value.
     * @param string: value to be set to searchInput
     */
    fun updateSearchInput(string: String) {
        searchInput.value = string
    }

    /**
     * Attempts to convert the given string to a Date object. Returns default current date if an error is thrown.
     * @param string: Date in string format to be converted
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
     * @param type: TaskType to be handled.
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
     * @param st: Subtask to be updated in Room db.
     * @param status: completed state of the Subtask
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