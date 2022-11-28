package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.constants.PROFILE_IMAGE_KEY
import com.metropolia.eatthefrog.constants.SHARED_PREF_KEY
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTaskScreenViewModel(application: Application) : AndroidViewModel(application) {

    val app = application
    private val database = InitialDB.get(application)

    var subTaskList = MutableLiveData<List<Subtask>>(listOf())
    var editedSubTaskList = MutableLiveData<List<Subtask>>(listOf())

    fun updateEditSubTaskList(list: List<Subtask>) {
        val oldList = editedSubTaskList.value
        editedSubTaskList.value = oldList!! + list
    }

    fun toggleFrogsFalse(deadLine: String){
        viewModelScope.launch {
            database.taskDao().toggleFrogsFalse(deadLine)
        }
    }

    fun deleteEditedSubTask(ind: Int) {
        val newList: MutableList<Subtask> = editedSubTaskList.value!!.toMutableList()
        newList.removeAt(ind)
        editedSubTaskList.value = newList
    }

    fun deleteSubTaskFromDatabase(task_id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            database.subtaskDao().deleteSubTask(task_id)
        }
    }

    fun loadProfilePicture(): String? {
        val sharedPreferences = app.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PROFILE_IMAGE_KEY, null)

    }

    fun getHighlightedSubtasks(id: Long) = database.subtaskDao().getSubtasks(id)

    fun updateTask(task: Task) {
        viewModelScope.launch {
            database.taskDao().update(task)
        }
    }

    fun updateSubTaskList(list: List<Subtask>) {
        val oldList = subTaskList.value
        subTaskList.value = oldList!! + list
    }

    fun clearEditSubtaskList() {
        editedSubTaskList.value = emptyList<Subtask>()
    }

    fun clearSubTaskList() {
        subTaskList.value = emptyList<Subtask>()
    }

    fun deleteSubTask(ind: Int) {
        val newList: MutableList<Subtask> = subTaskList.value!!.toMutableList()
        newList.removeAt(ind)
        subTaskList.value = newList
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            database.taskDao().insert(task)
        }
    }

    fun insertSubTask() {
        viewModelScope.launch {
            val subTaskList = subTaskList.value ?: emptyList()
            for (subTask in subTaskList) {
                database.subtaskDao().insertSubtask(subTask)
            }
        }
    }

    fun insertEditedTasks() {
        viewModelScope.launch {
            val subTaskList = editedSubTaskList.value ?: emptyList()
            for (subTask in subTaskList) {
                database.subtaskDao().insertSubtask(subTask)
            }
        }
    }

    fun getLastTask(): LiveData<Task> = database.taskDao().getLastTask()
}




