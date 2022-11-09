package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.metropolia.eatthefrog.database.InitialDB

class ProfileScreenViewModel(application: Application): AndroidViewModel(application) {

    private val database = InitialDB.get(application)

    fun getClosedTasks() = database.taskDao().getClosedTasks()
    fun getActiveTasks() = database.taskDao().getActiveTasks()
    fun getFrogsEaten() = database.taskDao().getFrogsEaten()
    fun getTotalTaskCount() = database.taskDao().getTotalTaskCount()
}