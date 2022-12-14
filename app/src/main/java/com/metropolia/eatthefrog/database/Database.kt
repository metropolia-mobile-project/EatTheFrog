package com.metropolia.eatthefrog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Singleton class used for creating and accessing the Room DB.
 * @property Task: Task table, contains all Task objects.
 * @property Subtask: Subtask table, contains all Subtask objects.
 * @property TaskType: TaskType table, contains all TaskType objects.
 */
@Database(entities = [(Task::class), (Subtask::class), (TaskType::class)], version = 1)
abstract class InitialDB : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subtaskDao(): SubtaskDao
    abstract fun taskTypeDao(): TaskTypeDao

    companion object {
        private var INSTANCE: InitialDB? = null

        @Synchronized
        fun get(context: Context): InitialDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    InitialDB::class.java, "initialdb"
                ).build()
            }
            return INSTANCE!!
        }
    }
}