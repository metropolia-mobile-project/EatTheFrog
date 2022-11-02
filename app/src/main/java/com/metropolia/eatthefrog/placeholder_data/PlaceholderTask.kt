package com.metropolia.eatthefrog.placeholder_data

import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.database.TaskType
import java.text.SimpleDateFormat
import java.util.*

/*data class PlaceholderTask(
    val name: String,
    val subtasks: List<PlaceholderSubtask>,
    val taskType: TaskType,
    val deadline: Date?,
    val isFrog: Boolean
)

data class PlaceholderSubtask(
    val name: String,
)

enum class TaskType {
    MEETING,
    PLANNING,
    DEVELOPMENT
}*/

object PlaceholderTasks {
    private val subtasks = listOf(
        Subtask("Subtask 1", false),
        Subtask("Subtask 2", false),
        Subtask("Subtask 3", false),
        Subtask("Subtask 4", false),
    )

    private val sdf = SimpleDateFormat("dd-MM-yyyy")
    private val stf = SimpleDateFormat("HH:mm")

    val tasks = listOf(
        Task(
            0,
            "Task 1",
            //subtasks,
            TaskType.MEETING,
            //sdf.parse("01-11-2022"),
            //stf.parse("12:00"),
            true
        ),
        Task(
            0,
            "Task 1",
            //subtasks,
            TaskType.PLANNING,
            //sdf.parse("02-11-2022"),
            //stf.parse("09:00"),
            false
        ),
        Task(
            0,
            "Task 1",
            //subtasks,
            TaskType.DEVELOPMENT,
            //sdf.parse("03-11-2022"),
            //stf.parse("13:13"),
            false
        )
    )
}