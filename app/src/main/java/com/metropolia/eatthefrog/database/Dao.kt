package com.metropolia.eatthefrog.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    // Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Task): Long

    // Update
    @Update
    suspend fun update(item: Task)

    @Query("UPDATE task SET frog = (CASE WHEN frog = 0 AND uid = :id THEN 1 ELSE 0 END) WHERE task.deadline = :deadline")
    suspend fun toggleFrog(deadline: String, id: Long)

    @Query("UPDATE task SET frog = 0 WHERE task.deadline = :deadline")
    suspend fun toggleFrogsFalse(deadline: String)

    @Query("UPDATE task SET completed = (CASE WHEN completed = 0 THEN 1 ELSE 0 END) WHERE task.uid = :id")
    suspend fun toggleTask(id: Long)

    // Test
    @Query("SELECT * FROM task WHERE task.uid = :id")
    fun getTest(id: Long): Task

    // Query
    @Query("SELECT * FROM task WHERE task_name LIKE '%' || :pattern || '%' ORDER BY frog DESC")
    fun getAllTasks(pattern: String): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE completed = 1 AND task_name LIKE '%' || :pattern || '%' ORDER BY deadline DESC")
    fun getAllCompletedTasksOrderedByDate(pattern: String): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE completed = 1 ORDER BY deadline ASC")
    suspend fun getAllCompletedTasksOrderedByDate(): List<Task>

    @Query("SELECT * FROM task WHERE completed = 0 AND task_name LIKE '%' || :pattern || '%' ORDER BY deadline DESC")
    fun getAllIncompleteTasksOrderedByDate(pattern: String): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE task.uid = :id")
    fun getSpecificTask(id: Long): LiveData<Task>

    @Query("SELECT * FROM task ORDER BY uid DESC LIMIT 1")
    fun getLastTask(): LiveData<Task>

    @Query("SELECT COUNT(uid) FROM task WHERE task.deadline = :date")
    fun getDateTaskCount(date: String): LiveData<Int>

    @Query("SELECT COUNT(uid) FROM task WHERE task.completed = 1")
    fun getClosedTasks(): LiveData<Int>

    @Query("SELECT COUNT(uid) FROM task WHERE task.completed = 1 AND task.frog = 1")
    fun getFrogsEaten(): LiveData<Int>

    @Query("SELECT COUNT(uid) FROM task WHERE task.completed = 0")
    fun getActiveTasks(): LiveData<Int>

    @Query("SELECT COUNT(uid) FROM task")
    fun getTotalTaskCount(): LiveData<Int>

    // Delete
    @Query("DELETE FROM task WHERE task.uid = :id")
    suspend fun deleteTask(id: Long)
}

@Dao
interface SubtaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubtask(subtask: Subtask): Long

    @Query("UPDATE subtask SET completed = :s WHERE uid = :id")
    suspend fun updateSubtaskCompletedStatus(id: Long, s: Boolean)

    @Query("SELECT COUNT(*) FROM subtask WHERE subtask.task_id = :id")
    fun getSubtasksAmount(id: Long): LiveData<Int>

    @Query("SELECT * FROM subtask WHERE subtask.task_id = :id")
    fun getSubtasks(id: Long): LiveData<List<Subtask>>

    @Query("DELETE FROM subtask WHERE subtask.uid = :id")
    suspend fun deleteSubTask(id: Long)

}

@Dao
interface TaskTypeDao {
    // Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTaskType(taskType: TaskType): Long

    // Query
    @Query("SELECT * FROM tasktype")
    fun getTaskTypes(): LiveData<List<TaskType>>

    @Query("SELECT * FROM tasktype WHERE tasktype.uid = :id")
    fun getTaskType(id: Long): LiveData<TaskType>

    @Query("SELECT * FROM tasktype LIMIT 1")
    fun getFirstTaskType(): LiveData<TaskType>

    // Delete
    @Query("DELETE FROM tasktype WHERE tasktype.uid = :id")
    suspend fun deleteTasktype(id: Long)
}
