package com.metropolia.eatthefrog.notification

import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.constants.DATE_TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    /**
     * The function takes a time (for example 11:30) as a parameter.
     * Forms a date timestamp from current date and given time.
     */
    fun toTimestamp(time: String): Date {
        val sdfDate = SimpleDateFormat(DATE_FORMAT)
        val sdfDateTime = SimpleDateFormat(DATE_TIME_FORMAT)
        val today: String = sdfDate.format(Date())
        return sdfDateTime.parse(today + " " + time)
    }
}