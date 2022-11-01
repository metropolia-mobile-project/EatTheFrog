package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

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

    fun selectDateFilter(dateFilter: DateFilter) {
        selectedFilter.postValue(dateFilter)
    }
}