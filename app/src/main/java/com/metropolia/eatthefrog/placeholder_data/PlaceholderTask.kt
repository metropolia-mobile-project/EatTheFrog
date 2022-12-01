package com.metropolia.eatthefrog.placeholder_data

import com.metropolia.eatthefrog.database.TaskTypeOld
import java.text.SimpleDateFormat

data class PlaceholderTask(
    val name: String,
    val description: String,
    var subtasks: List<PlaceholderSubtask>,
    val taskTypeOld: TaskTypeOld,
    val deadline: String,
    var isFrog: Boolean
)

data class PlaceHolderTaskNew(
    val name: String,
    val description: String,
    val taskTypeOld: TaskTypeOld,
    val startingDate: String,
    val deadline: String,
    val isFrog: Boolean,
)

data class PlaceholderSubtask(
    val name: String,
    var completed: Boolean = false,
)

/*enum class TaskType {
    MEETING,
    PLANNING,
    DEVELOPMENT
}*/

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
            "Placeholder description 1",
            subtasks,
            TaskTypeOld.MEETING,
            //sdf.parse("01-11-2022"),
            "",
            true
        ),
        PlaceholderTask(
            "Task 2",
            "Placeholder description 2",
            subtasks,
            TaskTypeOld.PLANNING,
            //sdf.parse("02-11-2022"),
            "",
            false
        ),
        PlaceholderTask(
            "Task 3",
            "Placeholder description 3",
            subtasks,
            TaskTypeOld.DEVELOPMENT,
            //sdf.parse("03-11-2022"),
            "",
            false
        )
    )
}