package com.metropolia.eatthefrog.screens


import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R


@Composable
fun ProfileScreen(username: String) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary)
            .padding(0.dp, 45.dp, 0.dp, 0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        PickImageFromGallery()

        Text(
            text = username,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(15.dp),
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
        )
        TaskDetails()
        SwitchPart()

    }
}


/**
 * Creates basic switches for changing dark mode and deadline options
 */
@Composable
fun ProfileSwitches() {
    val checkedState = remember { mutableStateOf(false) }
    Switch(
        checked = checkedState.value,
        onCheckedChange = { checkedState.value = it },
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colors.primaryVariant
        )

    )
}


/**
 * Creates placeholder image for profile picture and a button that opens gallery
 * so user can choose their own profile picture.
 */
@Composable
fun PickImageFromGallery() {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Box(
        modifier = Modifier
            .width(250.dp)
            .height(170.dp)
    ) {
        if (imageUri === null) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Circle Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(5.dp, Color.White, CircleShape)
                    .align(Alignment.Center)
            )
        } else {

            imageUri?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images
                        .Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }

                bitmap.value?.let { btm ->
                    Image(
                        bitmap = btm.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(5.dp, Color.White, CircleShape)
                            .align(Alignment.Center)
                    )
                }
            }
        }
        TextButton(modifier = Modifier
            .width(60.dp)
            .height(60.dp)
            .align(Alignment.BottomEnd),
            onClick = { launcher.launch("image/*") }) {
            Image(
                painterResource(id = R.drawable.ic_image),
                contentDescription = null,
                Modifier
                    .width(60.dp)
                    .height(60.dp),
            )
        }
    }
}

/**
 * Creates Text units for numbers under detail texts
 */
@Composable
fun TaskNumberText(number: String) {
    Text(
        text = number,
        fontWeight = FontWeight.Medium,
        color = Color.Yellow,
        fontSize = 18.sp
    )
}

/**
 * Creates Texts for task titles
 */
@Composable
fun TaskText(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Medium,
        color = Color.Black,
        fontSize = 18.sp,
        textDecoration = TextDecoration.Underline
    )
}
/**
 * TaskDetails function creates the middle part of the profile screen
 */
@Composable
fun TaskDetails() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(50.dp, 30.dp, 50.dp, 0.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .width(130.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TaskText(text = stringResource(id = R.string.closed_tasks))
            TaskNumberText(number = "1")
        }
        Column(
            modifier = Modifier
                .width(130.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            TaskText(text = stringResource(id = R.string.frogs_eaten))
            TaskNumberText(number = "1")
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(50.dp, 30.dp, 50.dp, 0.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Column(
            modifier = Modifier
                .width(130.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            TaskText(text = stringResource(id = R.string.active_tasks))
            TaskNumberText(number = "1")
        }

        Column(
            modifier = Modifier
                .width(130.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            TaskText(text = stringResource(id = R.string.total_tasks))
            TaskNumberText(number = "1")
        }
    }
}


/**
 * SwitchPart Function creates the bottom of the profile screen where
 * option switches are shown
 */
@Composable
fun SwitchPart() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(20.dp, 20.dp, 0.dp, 0.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.End
        ) {
            ProfileSwitches()
            Text(
                text = stringResource(id = R.string.dark_mode),
                fontWeight = FontWeight.Light,
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(0.dp, 14.dp, 0.dp, 0.dp)

            )
        }
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.End
        ) {
            ProfileSwitches()
            Text(
                text = stringResource(id = R.string.deadline_rem),
                fontWeight = FontWeight.Light,
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(0.dp, 6.dp, 0.dp, 0.dp)
            )
        }
    }
}


@Preview
@Composable
fun profileScreenPreview() {

}