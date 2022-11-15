package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.database.TaskType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class AddTaskScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val database = InitialDB.get(application)



    var subTaskList = MutableLiveData<List<Subtask>>(listOf())


    fun getSelectedTask(id: Long) = database.taskDao().getSpecificTask(id)

    fun updateSubTaskList(list: List<Subtask>) {
        val oldList = subTaskList.value
        subTaskList.value = oldList!! + list
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

    fun getLastTask(): LiveData<Task> = database.taskDao().getLastTask()

}


