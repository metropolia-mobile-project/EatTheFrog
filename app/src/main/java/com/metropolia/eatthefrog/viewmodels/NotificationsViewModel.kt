package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.database.InitialDB
import java.text.SimpleDateFormat


class NotificationsViewModel(application: Application) : HomeScreenViewModel(application) {
    private val database = InitialDB.get(application)
    //private val sdf = SimpleDateFormat(DATE_FORMAT)

    // HOX: LocalDate.now().plusDays(1).toString() cannot be formatted to a Date
    //val tomorrow: String = sdf.format(LocalDate.now().plusDays(1).toString())

    fun getCertainTask(id: Long) = database.taskDao().getTest(id)
}
