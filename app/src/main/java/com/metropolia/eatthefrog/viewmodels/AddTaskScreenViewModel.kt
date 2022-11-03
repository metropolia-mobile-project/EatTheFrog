package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import kotlinx.coroutines.launch

class AddTaskScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val database = InitialDB.get(application)

    fun insertTask(task: Task) {
        viewModelScope.launch {
            database.taskDao().insert(task)
        }
    }
    fun insertSubTask(subTask: Subtask) {
        viewModelScope.launch {
            database.subtaskDao().insertSubtask(subTask)
        }
    }

    fun getLastTask(): LiveData<Task> = database.taskDao().getLastTask()

}
