package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.database.InitialDB
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NotificationsViewModel(application: Application) : HomeScreenViewModel(application) {
    private val database = InitialDB.get(application)
    private val dtf = DateTimeFormatter.ofPattern(DATE_FORMAT)

    // HOX: LocalDate.now().plusDays(1).toString() cannot be formatted to a Date
    val tomorrow: String = dtf.format(LocalDateTime.now().plusDays(1))

    fun getCertainTask(id: Long) = database.taskDao().getTest(id)
}
