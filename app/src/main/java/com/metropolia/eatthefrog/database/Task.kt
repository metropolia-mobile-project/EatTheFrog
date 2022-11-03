package com.metropolia.eatthefrog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    @ColumnInfo(name = "task_name") val name: String,
    @ColumnInfo(name = "task_type") val taskType: TaskType,
    @ColumnInfo(name = "deadline") val deadline: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "frog") val isFrog: Boolean
)

@Entity(
    tableName = "subtask",
    foreignKeys = [ForeignKey(
        entity = Task::class,
        childColumns = ["subtask_id"],
        parentColumns = ["uid"]
    )]
)
data class Subtask(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "task_id") val taskId: Long,
    @ColumnInfo(name = "subtask_name") val name: String,
    @ColumnInfo(name = "done") val isDone: Boolean
)

enum class TaskType {
    MEETING,
    PLANNING,
    DEVELOPMENT
}