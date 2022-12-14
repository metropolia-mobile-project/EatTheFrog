package com.metropolia.eatthefrog.screens


import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.screens.profile.components.ProfileGalleryPickerContainer
import com.metropolia.eatthefrog.screens.profile.components.ProfileGraphContainer
import com.metropolia.eatthefrog.screens.profile.components.ProfileTaskDetailsContainer
import com.metropolia.eatthefrog.screens.profile.components.ProfileTaskSwitchContainer
import com.metropolia.eatthefrog.viewmodels.NotificationsViewModel
import com.metropolia.eatthefrog.viewmodels.ProfileScreenViewModel

/**
 * Displays information of the user, statistics and settings.
 * @param username: Name of the user
 * @param application: Context of the whole application.
 */
@Composable
fun ProfileScreen(username: String, application: Application) {
    val profileScreenViewModel = ProfileScreenViewModel(application)
    val notificationViewModel = NotificationsViewModel(application)

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary)
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            ProfileGalleryPickerContainer(profileScreenViewModel, username)
            ProfileTaskSwitchContainer(profileScreenViewModel, notificationViewModel)
            ProfileTaskDetailsContainer(profileScreenViewModel)
            ProfileGraphContainer(profileScreenViewModel)
        }
    }
}


