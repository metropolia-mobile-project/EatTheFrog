package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.database.TaskType
import com.metropolia.eatthefrog.placeholder_data.PlaceholderTask

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

    fun getHighlightedSubTasks() : LiveData<List<Subtask>> = database.subtaskDao().getSubtasks(this.highlightedTaskId.value)

    fun getTasks() = database.taskDao().getAllTasks()
    fun getSelectedTask() = database.taskDao().getSpecificTask(highlightedTaskId.value)

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
//        this.highlightedTask.value.subtasks = t.subtasks
    }

    fun setTaskAsDailyFrog(v: Boolean) {
//        this.highlightedTask.value.isFrog = v
        // TODO: Disable other frogs
    }

    // TODO: Change this implementation to Update the status of the subtask in Room db
    fun updateSubTask(st: Subtask, status: Boolean) {}
}