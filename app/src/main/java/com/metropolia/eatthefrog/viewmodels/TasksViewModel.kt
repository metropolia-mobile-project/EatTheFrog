package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
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

open class TasksViewModel(application: Application) : AndroidViewModel(application) {

    val app = application
    private val database = InitialDB.get(application)

    var popupVisible = MutableLiveData(false)
    val selectedFilter = MutableLiveData(DateFilter.TODAY)
    var highlightedTaskId = mutableStateOf(0L)
    var showTaskDeleteConfirmWindow = mutableStateOf(false)

    fun getSubtasksAmount(id: Long) = database.subtaskDao().getSubtasksAmount(id)
    fun getSubtasks(id: Long) = database.subtaskDao().getSubtasks(id)
    fun getTaskType(id: Long) = database.taskTypeDao().getTaskType(id)
    fun getFirstTaskType() = database.taskTypeDao().getFirstTaskType()

    fun updateHighlightedTask(t: Task) {
        this.highlightedTaskId.value = t.uid
    }

    fun showPopup() {
        popupVisible.value = true
    }

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

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            database.taskDao().deleteTask(id)
        }
    }
}