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

    @Query("UPDATE task SET completed = (CASE WHEN completed = 0 THEN 1 ELSE 0 END) WHERE task.uid = :id")
    suspend fun toggleTask(id: Long)

    // Query
    @Query("SELECT * FROM task ORDER BY frog DESC")
    fun getAllTasks(): LiveData<List<Task>>

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
}
