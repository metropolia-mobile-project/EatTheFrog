package com.metropolia.eatthefrog.screens.addTask.components

import android.util.Log
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.database.Task
import com.metropolia.eatthefrog.viewmodels.AddTaskScreenViewModel

/**
 * UI for showing profile picture
 * UI and functionality to add title for new task
 * in edit mode title is already given which is current task title and it can be changed writing over it
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddTaskTitleContainer(
    vm: AddTaskScreenViewModel,
    taskTitle: String,
    onNameChange: (String) -> Unit,
) {


    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val imageUri by remember { mutableStateOf<Uri?>(vm.loadProfilePicture()?.toUri()) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .padding(20.dp, 30.dp, 0.dp, 0.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        //Loads either profile picture placeholder image or if user have already
        //picked their profile picture it will be showed here
        if (imageUri === null) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Circle Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colors.secondary, CircleShape)
                    .padding(5.dp)
            )
        } else {
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(data = imageUri)
                            .build()
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(75.dp)
                        .shadow(elevation = 10.dp, shape = CircleShape, clip = true)
                        .clip(CircleShape)
                )
            }
        }
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