package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.metropolia.eatthefrog.placeholder_data.PlaceholderTask
import com.metropolia.eatthefrog.placeholder_data.TaskType

enum class DateFilter {
    TODAY,
    WEEK,
    MONTH
}

/**
 * ViewModel for the Home screen
 */

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    val selectedFilter = MutableLiveData(DateFilter.TODAY)
    var popupVisible = mutableStateOf(false)
    var highlightedTask = mutableStateOf(PlaceholderTask("", "", emptyList(), TaskType.DEVELOPMENT, null, false))

    fun selectDateFilter(dateFilter: DateFilter) {
        selectedFilter.postValue(dateFilter)
    }

    fun showPopup() {
        popupVisible.value = true
    }

    fun resetPopupStatus() {
        popupVisible.value = false
    }

    fun updateHighlightedTask(t: PlaceholderTask) {
        highlightedTask.value = t
    }

    fun setTaskAsDailyFrog(v: Boolean) {
        highlightedTask.value.isFrog = v
        // TODO: Disable other frogs
    }
}