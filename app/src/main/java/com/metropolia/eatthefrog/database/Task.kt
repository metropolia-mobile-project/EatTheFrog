package com.metropolia.eatthefrog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    @ColumnInfo(name = "task_name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "task_type_id") val taskTypeId: Long,
    @ColumnInfo(name = "deadline") val deadline: String,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "completed") val completed: Boolean = false,
    @ColumnInfo(name = "frog") val isFrog: Boolean
) : Serializable

@Entity(
    tableName = "subtask",
    foreignKeys = [ForeignKey(
        entity = Task::class,
        childColumns = ["task_id"],
        parentColumns = ["uid"]
    )]
)
data class Subtask(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    @ColumnInfo(name = "task_id") val taskId: Long,
    @ColumnInfo(name = "subtask_name") val name: String,
    @ColumnInfo(name = "completed") val completed: Boolean
)

enum class TaskTypeOld {
    MEETING,
    PLANNING,
    DEVELOPMENT
}