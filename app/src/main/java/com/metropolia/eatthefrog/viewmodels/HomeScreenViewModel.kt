package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.metropolia.eatthefrog.database.InitialDB
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.services.APIService
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

enum class DateFilter {
    TODAY,
    WEEK,
    MONTH
}

/**
 * ViewModel for the Home screen
 */
class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val database = InitialDB.get(application)
    private val service = APIService.service

    val selectedFilter = MutableLiveData(DateFilter.TODAY)
    var popupVisible = MutableLiveData(false)
    var highlightedTaskId = mutableStateOf(0L)
    var showTaskDoneConfirmWindow = mutableStateOf(false)
    var showFrogConfirmWindow = mutableStateOf(false)
    var showQuoteToast = mutableStateOf(false)
    val dailyFrogSelected = MutableLiveData(false)
    private var quote = APIService.Result("", "", "")

    fun getTasks() = database.taskDao().getAllTasks()
    fun getSelectedTask() = database.taskDao().getSpecificTask(highlightedTaskId.value)
    fun getDateTaskCount(date: String) = database.taskDao().getDateTaskCount(date)
    fun getHighlightedSubtasks() = database.subtaskDao().getSubtasks(highlightedTaskId.value)
    fun getSubtasksAmount(id: Long) = database.subtaskDao().getSubtasksAmount(id)

    init {
        if (quote.q.isEmpty()) {
            viewModelScope.launch {
                quote = service.getRandomMotivationalQuote()[0]
            }
        }
    }

    fun selectDateFilter(dateFilter: DateFilter) {
        selectedFilter.postValue(dateFilter)
    }

    fun showPopup() {
        popupVisible.value = true
    }

    fun resetPopupStatus() {
        popupVisible.value = false
    }

    fun updateHighlightedTask(t: Task) {
        this.highlightedTaskId.value = t.uid
    }

    fun updateSubTask(st: Subtask, status: Boolean) {
        viewModelScope.launch {
            database.subtaskDao().updateSubtaskCompletedStatus(st.uid, status)
        }
    }

    fun closeTaskConfirmWindow() {
        showTaskDoneConfirmWindow.value = false
    }

    fun openTaskConfirmWindow() {
        showTaskDoneConfirmWindow.value = true
    }

    fun toggleTaskCompleted(task: Task?) {
        viewModelScope.launch {
            database.taskDao().toggleTask(highlightedTaskId.value)
            closeTaskConfirmWindow()

            if ((task?.isFrog == true && !showQuoteToast.value) && !task.completed) {
                Log.d("TOASTING", "NOW")
                Toasty.success(getApplication(), "\"${quote.q}\"\n\n-${quote.a}", Toast.LENGTH_LONG).show()
                showQuoteToast.value = true
            }
        }
    }

    fun openFrogConfirmWindow() {
        showFrogConfirmWindow.value = true
    }

    fun closeFrogConfirmWindow() {
        showFrogConfirmWindow.value = false
    }

    fun toggleTaskFrog() {
        viewModelScope.launch {
            database.taskDao().toggleFrog(highlightedTaskId.value)
            closeFrogConfirmWindow()
        }
    }
}