package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTaskScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val database = InitialDB.get(application)

    var subTaskList = MutableLiveData<List<Subtask>>(listOf())
    var editedSubTaskList = MutableLiveData<List<Subtask>>(listOf())

    fun updateEditSubTaskList(list: List<Subtask>) {
        val oldList = editedSubTaskList.value
        editedSubTaskList.value = oldList!! + list
    }
    fun deleteEditedSubTask(ind: Int) {
        val newList: MutableList<Subtask> = editedSubTaskList.value!!.toMutableList()
        newList.removeAt(ind)
        editedSubTaskList.value = newList
    }
    fun deleteSubTaskFromDatabase(task_id: Long){
        CoroutineScope(Dispatchers.IO).launch {
        database.subtaskDao().deleteSubTask(task_id)
        }
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

    fun deleteSubTask(ind : Int) {
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




