package com.metropolia.eatthefrog.notification

import android.annotation.SuppressLint
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.constants.DATE_TIME_FORMAT
import com.metropolia.eatthefrog.constants.TIME_FORMAT
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*

class DateTimeConverter {

    /**
     * The function takes a time (for example 11:30) as a parameter.
     * Forms a date timestamp from current date and given time.
     */
    @SuppressLint("SimpleDateFormat")
    fun toTimestamp(time: String): Date {
        val sdfDate = SimpleDateFormat(DATE_FORMAT)
        val sdfDateTime = SimpleDateFormat(DATE_TIME_FORMAT)
        val today: String = sdfDate.format(Date())
        return sdfDateTime.parse("$today $time")
    }

    /**
     * The function takes a time (for example 11:30) and a amount of hours/minutes to subtract as a parameter.
     * Returns given time with subtracted minutes/hours.
     */
    fun modifyTime(time: String, minutes: Int = 0, hours: Int = 0): TemporalAccessor? {
        val sdf = DateTimeFormatter.ofPattern(TIME_FORMAT)
        val setTime: LocalTime = LocalTime.parse(time, sdf)
        if (minutes != 0 || hours != 0) {
            return if (minutes != 0) {
                setTime.minusMinutes(minutes.toLong())
            } else {
                setTime.minusHours(hours.toLong())
            }
        }
        return setTime.minusMinutes(5)
    }
}