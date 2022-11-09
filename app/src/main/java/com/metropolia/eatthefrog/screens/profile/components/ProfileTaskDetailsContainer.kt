package com.metropolia.eatthefrog.screens.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R

/**
 * ProfileTaskDetailsContainer function creates the middle part of the profile screen
 */
@Composable
fun ProfileTaskDetailsContainer() {
    ProfileTaskDetailsRow(stringResource(id = R.string.closed_tasks), 1, stringResource(id = R.string.frogs_eaten), 1)
    ProfileTaskDetailsRow(stringResource(id = R.string.active_tasks), 1, stringResource(id = R.string.total_tasks), 1)
}

@Composable
fun ProfileTaskDetailsRow(firstTaskTitle: String, firstTaskAmount: Int, secondTaskTitle: String, secondTaskAmount: Int) {
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
            TaskText(text = firstTaskTitle)
            TaskNumberText(number = firstTaskAmount.toString())
        }
        Column(
            modifier = Modifier
                .width(130.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            TaskText(text = secondTaskTitle)
            TaskNumberText(number = secondTaskAmount.toString())
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
