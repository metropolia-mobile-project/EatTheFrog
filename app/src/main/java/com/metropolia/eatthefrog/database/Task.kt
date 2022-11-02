package com.metropolia.eatthefrog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    @ColumnInfo(name = "task_name") val name: String,
    //@ColumnInfo(name = "sub_tasks") val subtasks: List<Subtask>,
    @ColumnInfo(name = "task_type") val taskType: TaskType,
    @ColumnInfo(name = "deadline") val deadline: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "frog") val isFrog: Boolean
)

@Entity
data class Subtask(
    val name: String,
    val isDone: Boolean
)

enum class TaskType {
    MEETING,
    PLANNING,
    DEVELOPMENT
}