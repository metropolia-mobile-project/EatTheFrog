package com.metropolia.eatthefrog.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.placeholder_data.PlaceholderTasks

/**
 * Top half of the Home screen, containing the profile picture and a greeting telling the user
 * how many tasks they have for today.
 */
@Composable
fun ProfileContainer(username: String) {
    Row(Modifier.padding(30.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "Profile picture",
            Modifier
                .align(Alignment.CenterVertically)
                .size(75.dp)
                .border(2.dp, Color.Black, CircleShape)
                .padding(5.dp)
        )
        Column(Modifier
            .align(Alignment.CenterVertically)
            .padding(15.dp)
        ) {
            Text(
                text = "${stringResource(id = R.string.hello)} $username!",
                fontSize = 15.sp
            )
            Text(
                text = "${stringResource(id = R.string.you_have)} ${PlaceholderTasks.tasks.count()} ${stringResource(id = R.string.tasks_today)}.",
                fontSize = 15.sp
            )
        }
    }
}

@Preview
@Composable
fun ProfileContainerPreview() {
    ProfileContainer("John Doe")
}