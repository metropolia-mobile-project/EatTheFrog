package com.metropolia.eatthefrog.screens

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Paint.Align
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.database.Subtask
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.database.TaskType
import com.metropolia.eatthefrog.screens.home.components.SingleTaskContainer
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.time.format.TextStyle
import java.util.*


lateinit var addTaskScreenViewModel: AddTaskScreenViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddTaskScreen(application: Application) {
    val keyboardController = LocalSoftwareKeyboardController.current
    addTaskScreenViewModel = AddTaskScreenViewModel(application)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.secondary)
            .clickable { keyboardController?.hide() }

    ) {

        AddTaskScreenC(addTaskScreenViewModel)

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddTaskScreenC(viewModel: AddTaskScreenViewModel) {


    val context = LocalContext.current

    //Variables for time and date
    val sdf = SimpleDateFormat("dd.MM.yyyy")
    val stf = SimpleDateFormat("HH:mm")
    val currentDate = sdf.format(Date())
    val currentTime = stf.format(Date())
    val sCalendar = Calendar.getInstance()
    val mYear = sCalendar.get(Calendar.YEAR)
    val mMonth = sCalendar.get(Calendar.MONTH)
    val mDay = sCalendar.get(Calendar.DAY_OF_MONTH)
    val mHour = sCalendar.get(Calendar.HOUR_OF_DAY)
    val mMinute = sCalendar.get(Calendar.MINUTE)


    //Variables for dropdown menu
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Planning", "Meeting", "Development")
    var selectedIndex by remember { mutableStateOf(0) }
    val disabledValue = ""

    val taskTypeList = listOf(TaskType.PLANNING, TaskType.MEETING, TaskType.DEVELOPMENT)

    //Variables for creating task object
    var description by remember { mutableStateOf("") }
    var taskTitle by remember { mutableStateOf("") }
    var taskType: TaskType by remember { mutableStateOf(taskTypeList[0]) }
    val sDate = remember { mutableStateOf(currentDate) }
    val sTime = remember { mutableStateOf(currentTime) }
    val newTask =
        Task(0, taskTitle, description, taskType, sDate.value, sTime.value, false)

    val lastTask = viewModel.getLastTask().observeAsState()


    val subList = viewModel.subTaskList.observeAsState()


    //Variables for creates subtask
    var subTaskTitle by remember { mutableStateOf("") }
    var subTaskId: Long by remember { mutableStateOf(0) }
    val subTaskDone by remember { mutableStateOf(false) }





    sCalendar.time = Date()

    val sDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            sDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )
    val mTimePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            sTime.value = "$hour:$minute"
        }, mHour, mMinute, false
    )


    /**
     * Profile picture, task name and text-field for task name
     */
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
                text = stringResource(id = R.string.task_name)
            )
            Box(
                modifier = Modifier
                    .padding(0.dp),
                contentAlignment = Alignment.CenterStart,
            )
            {

                TextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
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
    }
    /**
     * Description title and TextField
     * Dropdown menu for picking task type
     */
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(40.dp, 30.dp, 0.dp, 0.dp),

        ) {
        Row() {
            Text(
                text = stringResource(id = R.string.description)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopStart)
                    .padding(start = 110.dp, end = 30.dp)
            ) {
                Text(
                    items[selectedIndex],
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { expanded = true })
                        .background(
                            Color.Transparent
                        )
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .wrapContentWidth()
                        .background(
                            MaterialTheme.colors.primary
                        )
                ) {
                    items.forEachIndexed { index, s ->
                        DropdownMenuItem(onClick = {
                            selectedIndex = index
                            expanded = false
                            taskType = taskTypeList[selectedIndex]
                        }) {
                            val disabledText = if (s == disabledValue) {
                                " (Disabled)"
                            } else {
                                ""
                            }
                            Text(text = s + disabledText)
                        }
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_dropdownarrow),
                    contentDescription = "",
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                        .align(Alignment.CenterEnd)
                )
            }
        }

        TextField(
            value = description,
            onValueChange = { description = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 30.dp, 15.dp)
        )
    }
    /**
     * UI for picking task date and time
     */
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
                text = stringResource(id = R.string.starting_date)
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

        Column(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(30.dp, 0.dp, 0.dp, 0.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.task_deadline)
                )
                Row(
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 0.dp)
                ) {
                    Text(
                        text = sTime.value,
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
    }

    Column(
        Modifier.heightIn(0.dp, 200.dp)
    ) {
        Text("Subtasks",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(30.dp, 0.dp, 0.dp, 0.dp))

        LazyColumn(
            Modifier
                .wrapContentHeight()
                .padding(30.dp, 0.dp, 0.dp, 0.dp)
        ){
            itemsIndexed(subList.value!!.toList()) { index, sub ->
                Text(text = (index + 1).toString() + ". " + sub.name, modifier = Modifier
                    .padding(0.dp, 3.dp))
            }
        }
    }
    



    /**
     * Sub-task text title and sub-task TextField
     */
    Column(
        modifier = Modifier
            .padding(30.dp, 30.dp, 0.dp, 0.dp)
            .wrapContentWidth()
    ) {
        Text(
            text = stringResource(id = R.string.sub_task_title)
        )
        Row() {
            TextField(
                value = subTaskTitle,
                onValueChange = { subTaskTitle = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier
                    .width(250.dp)
                    .padding(0.dp, 0.dp, 30.dp, 15.dp),
                trailingIcon = {
                    Icon(Icons.Default.Add,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                subTaskId = if (lastTask.value == null) {
                                    1
                                } else lastTask.value!!.uid + 1
                                val list = listOf(Subtask(0, subTaskId, subTaskTitle, false))
                                viewModel.updateSubTaskList(list)
                                subTaskTitle = ""
                            })
                }
            )
        }
    }
    /**
     * "Create Task" button
     */
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Button(
            onClick = {
                Log.d("Testing", newTask.toString())
                Log.d("Testing dropdown", selectedIndex.toString())
                viewModel.insertTask(newTask)
                Log.d("Last Task", subTaskId.toString())
                Log.d("Observable", subList.value.toString())
                viewModel.insertSubTask()
                taskTitle = ""
                description = ""
                viewModel.clearSubTaskList()
            }, modifier = Modifier
                .width(200.dp)
                .padding(top = 50.dp)
        ) {
            Text(text = stringResource(id = R.string.create_task))
        }
    }
}








