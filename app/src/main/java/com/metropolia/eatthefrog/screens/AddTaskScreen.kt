package com.metropolia.eatthefrog.screens

import android.app.DatePickerDialog
import android.icu.text.CaseMap.Upper
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddTaskScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary)
    ) {
        UpperPart()
        Description()
        DatePickingSection()

    }
}

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
            contentDescription  = null,
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
}


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
    Column() {
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


@Composable
fun StartingDatePicker() {

    val context = LocalContext.current
    val sYear: Int
    val sMonth: Int
    val sDay: Int

    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val currentDate = sdf.format(Date())

    val sCalendar = Calendar.getInstance()

    sYear = sCalendar.get(Calendar.YEAR)
    sMonth = sCalendar.get(Calendar.MONTH)
    sDay = sCalendar.get(Calendar.DAY_OF_MONTH)

    sCalendar.time = Date()

    val sDate = remember { mutableStateOf(currentDate) }

    val sDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, sYear: Int, sMonth: Int, sDayOfMonth: Int ->
            sDate.value = "$sDayOfMonth/${sMonth + 1}/$sYear"
        }, sYear, sMonth, sDay
    )
    Column() {
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