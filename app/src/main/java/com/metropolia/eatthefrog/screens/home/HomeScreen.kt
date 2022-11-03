package com.metropolia.eatthefrog.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.PopupView
import com.metropolia.eatthefrog.activities.MainActivity
import com.metropolia.eatthefrog.screens.home.components.ProfileContainer
import com.metropolia.eatthefrog.screens.home.components.TasksContainer
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel

@ExperimentalMaterialApi
@Composable
fun HomeScreen(username: String, application: Application) {
    val homeScreenViewModel = HomeScreenViewModel(application)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileContainer(username)
        TasksContainer(homeScreenViewModel)
    }
    TaskScreen(homeScreenViewModel)
}