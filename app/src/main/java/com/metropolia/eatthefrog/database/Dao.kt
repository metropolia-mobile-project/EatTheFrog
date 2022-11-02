package com.metropolia.eatthefrog.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Task): Long

    @Update
    suspend fun update(item: Task)

    @Query("SELECT * FROM task")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT task_name FROM task WHERE task.uid = :id")
    fun getSpecificTask(id: Long): LiveData<String>
}

// SubtaskDao?