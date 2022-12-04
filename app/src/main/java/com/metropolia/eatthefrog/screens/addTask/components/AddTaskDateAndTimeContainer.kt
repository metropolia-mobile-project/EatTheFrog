package com.metropolia.eatthefrog.screens.addTask.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

/**
 * UI and functionality to set deadlines (date and time) to task
 */
@Composable
fun AddTaskDateAndTimeContainer(
    onDateChange: (String) -> Unit,
    onTimeChange: (String) -> Unit,
    isEditMode: Boolean,
    dateDeadline: String,
    timeDeadline: String,
    sDate: String
) {


    val context = LocalContext.current
    val sCalendar = Calendar.getInstance()
    val mYear = sCalendar.get(Calendar.YEAR)
    val mMonth = sCalendar.get(Calendar.MONTH)
    val mDay = sCalendar.get(Calendar.DAY_OF_MONTH)
    val mHour = sCalendar.get(Calendar.HOUR_OF_DAY)
    val mMinute = sCalendar.get(Calendar.MINUTE)

    val date = remember {
        mutableStateOf(
            if (isEditMode) {
                dateDeadline
            } else {
                sDate
            }
        )
    }
    val time = remember {
        mutableStateOf(
            if (isEditMode) {
                timeDeadline
            } else {
                "16.00"
            }
        )
    }


    val sDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            date.value = "$mDayOfMonth.${mMonth + 1}.$mYear"
        }, mYear, mMonth, mDay
    )
    val mTimePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            time.value = String.format("%02d:%02d", hour, minute)
        }, mHour, mMinute, true
    )



    onDateChange(date.value)
    onTimeChange(time.value)

    Column(Modifier.padding(10.dp)) {

        Text(
            text = stringResource(id = R.string.task_deadline),
            Modifier.padding(bottom = 10.dp),
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Column(Modifier.clickable { sDatePickerDialog.show() }) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = null,
                        modifier = Modifier
                            .width(25.dp)
                            .height(25.dp)
                            .padding(0.dp, 0.dp, 4.dp, 0.dp)
                    )
                    Text(
                        text = date.value
                    )

                }
                Divider(
                    color = Color.Black, thickness = 1.dp, modifier = Modifier
                        .width(125.dp)
                )
            }

            Column(
                modifier = Modifier
                    .clickable { mTimePickerDialog.show() }
            ) {

                Row(
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 0.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_add_time),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable { mTimePickerDialog.show() }
                            .size(25.dp)
                            .padding(end = 4.dp)
                            .width(25.dp)
                            .height(25.dp)
                    )
                    Text(
                        text = time.value,
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    )
                }
                Divider(
                    color = Color.Black, thickness = 1.dp, modifier = Modifier
                        .width(125.dp)
                )
            }
        }
    }
}