package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Task

open class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val database = InitialDB.get(application)

    var popupVisible = MutableLiveData(false)
    val selectedFilter = MutableLiveData(DateFilter.TODAY)
    var highlightedTaskId = mutableStateOf(0L)

    fun getSubtasksAmount(id: Long) = database.subtaskDao().getSubtasksAmount(id)
    fun getSubtasks(id: Long) = database.subtaskDao().getSubtasks(id)

    fun updateHighlightedTask(t: Task) {
        this.highlightedTaskId.value = t.uid
    }

    fun showPopup() {
        popupVisible.value = true
    }

}