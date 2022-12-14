package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.constants.PROFILE_IMAGE_KEY
import com.metropolia.eatthefrog.constants.SHARED_PREF_KEY
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Task
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * An extendable ViewModel used for handling Task data.
 * @param application: Application context.
 */
open class TasksViewModel(application: Application) : AndroidViewModel(application) {

    val app = application
    private val database = InitialDB.get(application)

    var popupVisible = MutableLiveData(false)
    val selectedFilter = MutableLiveData(DateFilter.TODAY)
    var highlightedTaskId = mutableStateOf(0L)
    var showTaskDeleteConfirmWindow = mutableStateOf(false)

    /**
     * Fetch the the given Task's subtask count from Room db.
     * @param id: ID of the Task object.
     * @return amount of subtasks wrapped within a LiveData object.
     */
    fun getSubtasksAmount(id: Long) = database.subtaskDao().getSubtasksAmount(id)

    /**
     * Fetch the subtasks of the given Task's id from Room db.
     * @param id: ID of the Task object.
     * @return List of Subtask objects wrapped within a LiveData object.
     */
    fun getSubtasks(id: Long) = database.subtaskDao().getSubtasks(id)

    /**
     * Fetch the TaskType according to the ID from Room db.
     * @param id: ID of the TaskType object.
     * @return TaskType object wrapped within a LiveData object.
     */
    fun getTaskType(id: Long) = database.taskTypeDao().getTaskType(id)

    /**
     * Fetch the first TaskType from Room db.
     * @return TaskType object wrapped within a LiveData object.
     */
    fun getFirstTaskType() = database.taskTypeDao().getFirstTaskType()

    /**
     * Updates the highlightedTaskId with the given Task's id.
     * @param t: Task to be set.
     */
    fun updateHighlightedTask(t: Task) {
        this.highlightedTaskId.value = t.uid
    }

    /**
     * Open popup.
     */
    fun showPopup() {
        popupVisible.value = true
    }

    /**
     * Fetches boolean values from the SharedPreferences according to the given key.
     * @param key: Key/value pair to be fetched.
     * @param default: default to be set if nothing is found.
     * @return boolean value of the given key.
     */
    fun getBooleanFromPreferences(key: String, default: Boolean): Boolean {
        val prefs = app.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, default)
    }

    /**
     * Close ConfirmWindow.
     */
    fun closeTaskDeleteWindow() {
        showTaskDeleteConfirmWindow.value = false
    }

    /**
     * Open ConfirmWindow.
     */
    fun openTaskDeleteWindow() {
        showTaskDeleteConfirmWindow.value = true
    }

    /**
     * Delete a Task from Room db according to the given ID.
     * @param id: ID of the Task object to be deleted.
     */
    fun deleteTask(id: Long) {
        viewModelScope.launch {
            database.taskDao().deleteTask(id)
        }
    }
}