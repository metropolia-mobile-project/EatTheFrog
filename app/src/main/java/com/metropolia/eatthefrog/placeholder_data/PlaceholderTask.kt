package com.metropolia.eatthefrog.placeholder_data

import java.text.SimpleDateFormat
import java.util.*

data class PlaceholderTask(
    val name: String,
    val subtasks: List<PlaceholderSubtask>,
    val taskType: TaskType,
    val deadline: String,
    val isFrog: Boolean
)

data class PlaceHolderTaskNew(
    val name: String,
    val description: String,
    val taskType: TaskType,
    val startingDate: String,
    val deadline: String,
    val isFrog: Boolean,
)

data class PlaceholderSubtask(
    val name: String,
)

enum class TaskType {
    MEETING,
    PLANNING,
    DEVELOPMENT
}

object PlaceholderTasks {
     val subtasks = listOf(
        PlaceholderSubtask("Subtask 1"),
        PlaceholderSubtask("Subtask 2"),
        PlaceholderSubtask("Subtask 3"),
        PlaceholderSubtask("Subtask 4"),
    )

    private val sdf = SimpleDateFormat("dd-MM-yyyy")

    val tasks = listOf(
        PlaceholderTask(
            "Task 1",
            subtasks,
            TaskType.MEETING,
            //sdf.parse("01-11-2022"),
            "",
            true
        ),
        PlaceholderTask(
            "Task 1",
            subtasks,
            TaskType.PLANNING,
            //sdf.parse("02-11-2022"),
            "",
            false
        ),
        PlaceholderTask(
            "Task 1",
            subtasks,
            TaskType.DEVELOPMENT,
            //sdf.parse("03-11-2022"),
            "",
            false
        )
    )
}