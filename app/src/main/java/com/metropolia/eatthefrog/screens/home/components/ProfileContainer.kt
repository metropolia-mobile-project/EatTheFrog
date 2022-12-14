package com.metropolia.eatthefrog.screens.home.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.DATE_FORMAT
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Top half of the Home screen, containing the profile picture and a greeting telling the user
 * how many tasks they have for today.
 * @param username: name of the user.
 * @param vm: HomeScreenViewModel of the parent composable.
 */
@Composable
fun ProfileContainer(username: String, vm: HomeScreenViewModel) {
    val sdf = SimpleDateFormat(DATE_FORMAT)
    val today = sdf.format(Date())
    val tasksToday = vm.getDateTaskCount(today).observeAsState(0)
    val imageUri by remember { mutableStateOf<Uri?>(vm.loadProfilePicture()?.toUri()) }
    val currentStreak = vm.currentStreak.observeAsState(0)
    val longestStreak = vm.longestStreak.observeAsState(0)

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 30.dp)
                .padding(bottom = 10.dp)) {
            if (imageUri === null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Circle Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(75.dp)
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
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(75.dp)
                            .shadow(elevation = 10.dp, shape = CircleShape, clip = true)
                            .clip(CircleShape)
                    )
                }
            }

            Column(
                Modifier
                    .align(Alignment.CenterVertically)
                    .padding(15.dp)
            ) {
                Text(
                    text = "${stringResource(id = R.string.hello)} $username!",
                    fontSize = 15.sp
                )
                Text(
                    text = "${stringResource(id = R.string.you_have)} ${tasksToday.value} ${stringResource(id = R.string.tasks_today)}.",
                    fontSize = 15.sp
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(elevation = 5.dp,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_streak),
                        contentDescription = stringResource(id = R.string.current_streak),
                        modifier = Modifier.padding(5.dp),
                        tint = colorResource(id = R.color.fire_red)
                    )
                    Text(text = "${stringResource(id = R.string.current_streak)}:", modifier = Modifier.padding(horizontal = 10.dp))
                    Text(text = "${currentStreak.value}", modifier = Modifier.padding(5.dp))
                }
            }
            Card(
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(id = R.drawable.ic_line_chart),
                        contentDescription = stringResource(id = R.string.longest_streak),
                        modifier = Modifier.padding(5.dp),
                        tint = colorResource(id = R.color.green)
                    )
                    Text(text = "${stringResource(id = R.string.longest_streak)}:", modifier = Modifier.padding(horizontal = 10.dp))
                    Text(text = "${longestStreak.value}", modifier = Modifier.padding(5.dp))
                }
            }
        }

    }
}