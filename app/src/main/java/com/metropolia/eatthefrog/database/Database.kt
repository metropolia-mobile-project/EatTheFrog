package com.metropolia.eatthefrog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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