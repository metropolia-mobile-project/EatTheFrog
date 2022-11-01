package com.metropolia.eatthefrog.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddTaskScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.secondary)

    ) {
        UpperPart()
        Description()
        DatePickingSection()
        AddSubtasks()
        CreateTaskButton()
    }
}


@Composable
fun CreateTaskButton() {
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Button(onClick = {}, modifier = Modifier
            .width(200.dp)
            .padding(top = 50.dp)
        ) {
            Text(text = "Create Task")
        }
    }
}

@Composable
fun AddSubtasks() {

    var title by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(30.dp, 30.dp, 0.dp, 0.dp)
            .wrapContentWidth()
    ) {
        Text(
            text = "Sub-Task Title"
        )
        Row() {
            TextField(
                value = title,
                onValueChange = { title = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier
                    .padding(0.dp, 0.dp, 30.dp, 15.dp),
                )
        }
    }
}

/**
 * Functionality to pick deadline for the task
 * Creates UI for picking the time
 */
@Composable
fun TimePicker() {

    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val mHour = calendar[Calendar.HOUR_OF_DAY]
    val mMinute = calendar[Calendar.MINUTE]

    val sdf = SimpleDateFormat("HH:mm")
    val currentTime = sdf.format(Date())

    val time = remember { mutableStateOf(currentTime) }
    val mTimePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            time.value = "$hour:$minute"
        }, mHour, mMinute, false
    )


    Column(
        modifier = Modifier
            .padding(30.dp, 0.dp, 0.dp, 0.dp)
    ) {
        Text(
            text = "Task deadline"
        )
        Row(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Text(
                text = time.value,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 65.dp, 0.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_add_time),
                contentDescription = "",
                modifier = Modifier
                    .clickable { mTimePickerDialog.show() }
                    .width(25.dp)
                    .height(25.dp)

            )
        }
        Divider(
            color = Color.Black, thickness = 2.dp, modifier = Modifier
                .width(125.dp)
        )

    }

}

/**
 * Contains all parts of UI that is needed for picking and showing the dates
 */
@Composable
fun DatePickingSection() {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Text(
                text = "Starting date"
            )
            StartingDatePicker()
        }
        Image(
            painter = painterResource(id = R.drawable.ic_horizontal_rule),
            contentDescription = null,
            modifier = Modifier
                .height(20.dp)
                .width(20.dp)
        )
        Column(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Text(
                text = "Ending date"
            )
            EndingDatePicker()
        }
    }
    TimePicker()
}

/**
 * Functionality for picking ending date from calendar and showing current date in
 * text before new date is picked
 */
@Composable
fun EndingDatePicker() {

    val context = LocalContext.current
    val eYear: Int
    val eMonth: Int
    val eDay: Int

    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val currentDate = sdf.format(Date())

    val sCalendar = Calendar.getInstance()

    eYear = sCalendar.get(Calendar.YEAR)
    eMonth = sCalendar.get(Calendar.MONTH)
    eDay = sCalendar.get(Calendar.DAY_OF_MONTH)

    sCalendar.time = Date()

    val sDate = remember { mutableStateOf(currentDate) }

    val sDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, sYear: Int, sMonth: Int, sDayOfMonth: Int ->
            sDate.value = "$sDayOfMonth/${sMonth + 1}/$sYear"
        }, eYear, eMonth, eDay
    )
    Column {
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
                text = sDate.value,
            )
            Image(
                painter = painterResource(id = R.drawable.ic_dropdownarrow),
                contentDescription = "",
                modifier = Modifier
                    .clickable { sDatePickerDialog.show() }
                    .width(25.dp)
                    .height(25.dp)
            )
        }
        Divider(
            color = Color.Black, thickness = 2.dp, modifier = Modifier
                .width(125.dp)
        )
    }
}

/**
 * Functionality for picking starting date from calendar and showing current date in
 * text before new date is picked
 */
@Composable
fun StartingDatePicker() {

    val context = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val currentDate = sdf.format(Date())

    val sCalendar = Calendar.getInstance()

    mYear = sCalendar.get(Calendar.YEAR)
    mMonth = sCalendar.get(Calendar.MONTH)
    mDay = sCalendar.get(Calendar.DAY_OF_MONTH)

    sCalendar.time = Date()

    val sDate = remember { mutableStateOf(currentDate) }

    val sDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            sDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )
    Column {
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
                text = sDate.value,
            )
            Image(
                painter = painterResource(id = R.drawable.ic_dropdownarrow),
                contentDescription = "",
                modifier = Modifier
                    .clickable { sDatePickerDialog.show() }
                    .width(25.dp)
                    .height(25.dp)
            )

        }
        Divider(
            color = Color.Black, thickness = 2.dp, modifier = Modifier
                .width(125.dp)
        )
    }

}

/**
 * Contains textField and title for description
 */
@Composable
private fun Description() {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(40.dp, 30.dp, 0.dp, 0.dp),

        ) {
        Text(
            text = "Description"
        )
        DescriptionTextField()
    }
}

/**
 * TextField for description
 */
@Composable
private fun DescriptionTextField() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 30.dp, 15.dp)
    )
}

/**
 * TextField for Task name
 */
@Composable
private fun TaskNameTextField() {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .padding(0.dp),
        contentAlignment = Alignment.CenterStart,
    )
    {

        TextField(
            value = text,
            onValueChange = { text = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            ),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 30.dp, 15.dp),

            )
    }
}

/**
 * Upper part of the screen which contains profile picture and task name TextField
 */
@Composable
private fun UpperPart() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .padding(20.dp, 30.dp, 0.dp, 0.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "Circle Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(5.dp, Color.White, CircleShape)
        )
        Column(
            modifier = Modifier
                .padding(15.dp, 0.dp, 0.dp, 0.dp),
        ) {
            Text(
                text = "Task name"
            )
            TaskNameTextField()
        }
    }
}