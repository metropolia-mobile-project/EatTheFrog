package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.constants.PROFILE_IMAGE_KEY
import com.metropolia.eatthefrog.constants.SHARED_PREF_KEY
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.database.TaskType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for AddTaskScreen.
 * @param application: Application context.
 */
class AddTaskScreenViewModel(application: Application) : AndroidViewModel(application) {

    val app = application
    private val database = InitialDB.get(application)

    var subTaskList = MutableLiveData<List<Subtask>>(listOf())
    var editedSubTaskList = MutableLiveData<List<Subtask>>(listOf())
    val typeDialogVisible = MutableLiveData(false)
    val selectedTaskType = MutableLiveData<TaskType>()
    val initialTaskSaved = MutableLiveData(false)

    /**
     * Fetches all the TaskTypes from the TaskType table from Room db.
     * @return List of all the TaskTypes.
     */
    fun getTaskTypes() = database.taskTypeDao().getTaskTypes()

    /**
     * Fetches the given TaskType row according to the ID.
     * @param id: ID of the TaskType to be fetched.
     */
    fun getTaskType(id: Long) = database.taskTypeDao().getTaskType(id)

    /**
     * Fetches the first TaskType row from Room db.
     */
    fun getFirstTaskType() = database.taskTypeDao().getFirstTaskType()

    /**
     * Updates the Editable SubTaskList.
     * @param list: List of Subtasks to be added to the editedSubTaskList.
     */
    fun updateEditSubTaskList(list: List<Subtask>) {
        val oldList = editedSubTaskList.value
        editedSubTaskList.value = oldList!! + list
    }

    /**
     * Sets isFrog status to false for all Task table rows which contain the given deadline.
     * @param deadLine: selected deadline of Task objects to be modified.
     */
    fun toggleFrogsFalse(deadLine: String){
        viewModelScope.launch {
            database.taskDao().toggleFrogsFalse(deadLine)
        }
    }

    /**
     * Deletes the selected subtask from the editedSubTaskList.
     * @param ind: index of the subtask to be deleted.
     */
    fun deleteEditedSubTask(ind: Int) {
        val newList: MutableList<Subtask> = editedSubTaskList.value!!.toMutableList()
        newList.removeAt(ind)
        editedSubTaskList.value = newList
    }

    /**
     * Deletes a subtask from the database according to the given ID.
     * @param task_id: ID of the subtask to be deleted.
     */
    fun deleteSubTaskFromDatabase(task_id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            database.subtaskDao().deleteSubTask(task_id)
        }
    }

    /**
     * Fetches all Subtasks which belong to the highlighted Task object.
     * @param id: ID of the highlighted Task
     * @return List of subtasks wrapped in a LiveData object.
     */
    fun getHighlightedSubtasks(id: Long) = database.subtaskDao().getSubtasks(id)

    /**
     * Updates the Task object within the Room db.
     * @param task: Task object to be updated.
     */
    fun updateTask(task: Task) {
        viewModelScope.launch {
            database.taskDao().update(task)
        }
    }

    /**
     * Adds the given list to the Subtask list.
     * @param list: List to be added to the current Subtask list.
     */
    fun updateSubTaskList(list: List<Subtask>) {
        val oldList = subTaskList.value
        subTaskList.value = oldList!! + list
    }

    /**
     * Empties the current editedSubTaskList.
     */
    fun clearEditSubtaskList() {
        editedSubTaskList.value = emptyList<Subtask>()
    }

    /**
     * Empties the current subTaskList.
     */
    fun clearSubTaskList() {
        subTaskList.value = emptyList<Subtask>()
    }

    /**
     * Deletes the selected subtask from the subTaskList.
     * @param ind: index of the subtask to be deleted.
     */
    fun deleteSubTask(ind: Int) {
        val newList: MutableList<Subtask> = subTaskList.value!!.toMutableList()
        newList.removeAt(ind)
        subTaskList.value = newList
    }

    /**
     * Adds a new Task row to the Task table in Room db.
     * @param task: Task to be added to Room.
     */
    suspend fun insertTask(task: Task) {
        database.taskDao().insert(task)
    }

    /**
     * Adds all subtasks from the subTaskList to the Subtask table in Room db.
     */
    suspend fun insertSubTask() {
        val subTaskList = subTaskList.value ?: emptyList()
        val lastTaskId = database.taskDao().getLastTaskNoLiveData().uid
        for (subTask in subTaskList) {
            subTask.taskId = lastTaskId
            Log.d("SUBTASK_DEBUG", subTask.taskId.toString())
            database.subtaskDao().insertSubtask(subTask)
        }
    }

    /**
     * Adds all subtasks from the editedSubTaskList to the Subtask table in Room db.
     */
    fun insertEditedTasks() {
        viewModelScope.launch {
            val subTaskList = editedSubTaskList.value ?: emptyList()
            for (subTask in subTaskList) {
                database.subtaskDao().insertSubtask(subTask)
            }
        }
    }

    /**
     * Gets the last Task added to the Task table from Room db.
     * @return the last Task which was added.
     */
    fun getLastTask(): LiveData<Task> = database.taskDao().getLastTask()

    /**
     * Adds the given TaskType object to the TaskType table in Room db.
     * @param taskType: TaskType to be added to Room.
     */
    fun insertTaskType(taskType: TaskType) {
        viewModelScope.launch {
            database.taskTypeDao().insertTaskType(taskType)
        }
    }

    /**
     * Deletes the given TaskType object from the TaskType table in Room db.
     * @param id: ID of the TaskType to be deleted.
     */
    fun deleteTaskType(id: Long) {
        viewModelScope.launch {
            database.taskTypeDao().deleteTasktype(id)
        }
    }

    /**
     * Saves the Task and Subtasks to the Task and Subtask tables in Room db.
     * @param task: Task to be saved.
     */
    fun saveTask(task: Task) {
        viewModelScope.launch {
            insertTask(task)
            insertSubTask()
            clearSubTaskList()
        }
    }
}




