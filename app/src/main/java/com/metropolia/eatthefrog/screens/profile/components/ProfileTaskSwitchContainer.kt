package com.metropolia.eatthefrog.screens.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R

/**
 * SwitchContainer Function creates the bottom of the profile screen where
 * option switches are shown
 */

@Composable
fun ProfileTaskSwitchContainer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp, 20.dp, 0.dp, 0.dp),
        horizontalAlignment = Alignment.Start
    ) {
        SwitchRow(stringResource(id = R.string.dark_mode))
        SwitchRow(stringResource(id = R.string.deadline_rem))
        SwitchRow(stringResource(R.string.confirm_window))
    }
}

@Composable
fun SwitchRow(desc: String) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileSwitches()
        Text(
            text = desc,
            fontWeight = FontWeight.Light,
            color = Color.Black,
            fontSize = 14.sp
        )
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

