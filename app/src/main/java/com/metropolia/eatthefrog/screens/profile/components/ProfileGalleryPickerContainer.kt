package com.metropolia.eatthefrog.screens.profile.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.ProfileScreenViewModel

/**
 * Creates placeholder image for profile picture and a button that opens gallery
 * so user can choose their own profile picture.
 * @param vm: ProfileScreenViewModel of the parent composable.
 * @param username: name of the user.
 */
@Composable
fun ProfileGalleryPickerContainer(vm: ProfileScreenViewModel, username: String) {

    var imageUri by remember { mutableStateOf<Uri?>(vm.loadProfilePicture()?.toUri()) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    //Launches cropping feature after wanted picture is picked from gallery
    //cropped picture is saved to internal storage as profile pic
    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
        } else {
            val exception = result.error
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        val cropOptions = CropImageContractOptions(uri, CropImageOptions())
        imageCropLauncher.launch(cropOptions)
    }
    vm.saveFileToInternalStorage(imageUri)



    Card(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 25.dp,
    ) {

        Column(
            Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (imageUri === null) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant),
                        contentDescription = "Circle Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(3.dp, MaterialTheme.colors.primaryVariant, CircleShape)
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
                            contentScale = ContentScale.Crop,
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
                    .width(50.dp)
                    .height(50.dp)
                    .align(Alignment.TopEnd),
                    onClick = { launcher.launch("image/*") }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_image),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant),
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
            Text(
                text = username,
                modifier = Modifier
                    .padding(15.dp),
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
            )

        }
    }

}