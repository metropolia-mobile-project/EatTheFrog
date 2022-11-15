package com.metropolia.eatthefrog.screens.addTask.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel

/**
 * UI for showing profile picture
 * UI and functionality to add title for new task
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddTaskTitleContainer(
    viewModel: AddTaskScreenViewModel,
    taskTitle: String,
    onNameChange: (String) -> Unit,
    isEditMode: Boolean,
) {

    Log.d("TESTING ISEDITMODE AT TITLECONTAINER", isEditMode.toString())
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


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
                .size(75.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Black, CircleShape)
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
                    onValueChange = onNameChange,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide(); focusManager.clearFocus()
                    }),
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
}