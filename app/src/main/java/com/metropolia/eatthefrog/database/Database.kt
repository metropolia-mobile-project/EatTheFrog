package com.metropolia.eatthefrog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(Task::class)], version = 1)
abstract class InitialDB : RoomDatabase() {
    abstract fun taskDao(): TaskDao

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