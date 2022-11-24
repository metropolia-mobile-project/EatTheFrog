package com.metropolia.eatthefrog.screens


import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.screens.profile.components.ProfileGalleryPickerContainer
import com.metropolia.eatthefrog.screens.profile.components.ProfileGraphContainer
import com.metropolia.eatthefrog.screens.profile.components.ProfileTaskDetailsContainer
import com.metropolia.eatthefrog.screens.profile.components.ProfileTaskSwitchContainer
import com.metropolia.eatthefrog.viewmodels.ProfileScreenViewModel

@Composable
fun ProfileScreen(username: String, application: Application) {
    val profileScreenViewModel = ProfileScreenViewModel(application)

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary)
            .padding(10.dp, 45.dp, 10.dp, 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            ProfileGalleryPickerContainer(profileScreenViewModel)
        }

        item {
            Text(
                text = username,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(15.dp),
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
            )

            ProfileTaskSwitchContainer(profileScreenViewModel)
            ProfileTaskDetailsContainer(profileScreenViewModel)
            ProfileGraphContainer(profileScreenViewModel)
        }


    }





}


