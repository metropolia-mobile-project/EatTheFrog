package com.metropolia.eatthefrog.placeholder_data

import java.text.SimpleDateFormat
import java.util.*

data class PlaceholderTask(
    val name: String,
    val description: String,
    val subtasks: List<PlaceholderSubtask>,
    val taskType: TaskType,
    val deadline: Date?,
    var isFrog: Boolean
)

data class PlaceholderSubtask(
    val name: String,
    var completed: Boolean = false,
)

enum class TaskType {
    MEETING,
    PLANNING,
    DEVELOPMENT
}

object PlaceholderTasks {
    private val subtasks = listOf(
        PlaceholderSubtask("Subtask 1"),
        PlaceholderSubtask("Subtask 2"),
        PlaceholderSubtask("Subtask 3"),
        PlaceholderSubtask("Subtask 4"),
    )

    private val sdf = SimpleDateFormat("dd-MM-yyyy")

    val tasks = listOf(
        PlaceholderTask(
            "Task 1",
            "Placeholder description 1",
            subtasks,
            TaskType.MEETING,
            sdf.parse("01-11-2022"),
            true
        ),
        PlaceholderTask(
            "Task 2",
            "Placeholder description 2",
            subtasks,
            TaskType.PLANNING,
            sdf.parse("02-11-2022"),
            false
        ),
        PlaceholderTask(
            "Task 3",
            "Placeholder description 3",
            subtasks,
            TaskType.DEVELOPMENT,
            sdf.parse("03-11-2022"),
            false
        )
    )
}