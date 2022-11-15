package com.metropolia.eatthefrog.screens.profile.components

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.ProfileScreenViewModel

/**
 * Creates placeholder image for profile picture and a button that opens gallery
 * so user can choose their own profile picture.
 */
@Composable
fun ProfileGalleryPickerContainer(vm: ProfileScreenViewModel) {

    var imageUri by remember { mutableStateOf<Uri?>(vm.loadProfilePicture()?.toUri()) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = vm.saveFileToInternalStorage(uri)
    }

    Box(
        modifier = Modifier
            .width(250.dp)
            .height(170.dp)
    ) {
        if (imageUri === null) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "Circle Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(5.dp, Color.White, CircleShape)
                    .align(Alignment.Center)
                    .padding(10.dp)
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
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(5.dp, Color.White, CircleShape)
                        .align(Alignment.Center)
                )
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