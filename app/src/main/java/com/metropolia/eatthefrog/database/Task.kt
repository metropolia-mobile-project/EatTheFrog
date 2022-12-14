package com.metropolia.eatthefrog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Task class, used for storing Tasks into Room DB.
 * @param uid: ID of the object, this is autogenerated.
 * @param name: name of the Task.
 * @param description: description of the task.
 * @param taskTypeId: id of the TaskType.
 * @param deadline: deadline of the task.
 * @param time: time of the deadline.
 * @param completed: completed status of the task.
 * @param isFrog: isFrog status of the task.
 */
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


/**
 * Subtask class, used for storing Subtasks into Room DB. Gets deleted when the related Task object is deleted.
 * @param uid: ID of the object, this is autogenerated.
 * @param taskId: ID of the Task object this is related to.
 * @param name: name of the subtask.
 * @param completed: completed status of the subtask.
 */
@Entity(
    tableName = "subtask",
    foreignKeys = [ForeignKey(
        onDelete = CASCADE,
        entity = Task::class,
        childColumns = ["task_id"],
        parentColumns = ["uid"]
    )]
)
data class Subtask(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    @ColumnInfo(name = "task_id") var taskId: Long,
    @ColumnInfo(name = "subtask_name") val name: String,
    @ColumnInfo(name = "completed") val completed: Boolean
)