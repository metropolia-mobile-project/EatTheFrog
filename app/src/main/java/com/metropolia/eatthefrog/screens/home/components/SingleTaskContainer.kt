package com.metropolia.eatthefrog.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.activities.MainActivity
import com.metropolia.eatthefrog.placeholder_data.PlaceholderTask
import com.metropolia.eatthefrog.placeholder_data.PlaceholderTasks

/**
 * Container for a singular task in the Home screen list
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SingleTaskContainer(task: PlaceholderTask) {
    val activity = LocalContext.current as MainActivity
    val backgroundColor = if (task.isFrog) MaterialTheme.colors.primaryVariant else Color.White
    val taskNameTextColor = if (task.isFrog) Color.White else Color.Black
    val subtaskTextColor = if (task.isFrog) MaterialTheme.colors.secondary else MaterialTheme.colors.primary

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = backgroundColor,
        elevation = 5.dp,
        onClick = { activity.popupVisible.value = !activity.popupVisible.value }
    ) {
        Row(Modifier.padding(horizontal = 10.dp, vertical = 20.dp)) {
            Column() {
                Image(
                    painter = painterResource(id = R.drawable.ic_chart),
                    contentDescription = "Chart icon",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                    modifier = Modifier.size(50.dp)
                )
                if (task.isFrog) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_frog_cropped),
                        contentDescription = "Chart icon",
                        modifier = Modifier.padding(top = 20.dp).size(50.dp)
                    )
                }
            }

            Column(Modifier.padding(horizontal = 10.dp)) {
                Text(text = task.name, color = taskNameTextColor, fontSize = 24.sp)
                Text(text = "${task.subtasks.count()} ${stringResource(id = R.string.subtasks)}", color = subtaskTextColor)
            }
        }
    }
}

@Preview
@Composable
fun SingleTaskContainerPreview() {
    SingleTaskContainer(PlaceholderTasks.tasks[0])
}