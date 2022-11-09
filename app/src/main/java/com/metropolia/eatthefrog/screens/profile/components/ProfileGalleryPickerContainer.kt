package com.metropolia.eatthefrog.screens.profile.components

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R

/**
 * Creates placeholder image for profile picture and a button that opens gallery
 * so user can choose their own profile picture.
 */
@Composable
fun ProfileGalleryPickerContainer() {

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