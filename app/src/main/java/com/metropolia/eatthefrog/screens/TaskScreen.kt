package com.metropolia.eatthefrog.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.metropolia.eatthefrog.PopupView

/**
 * Popup window which displays the selected Task object and its data.
 */

// TODO: Add Task as a parameter when Room has been implemented.
@ExperimentalMaterialApi
@Composable
fun TaskScreen() {

    // Example use of PopupView. headerContent can be added, as displayed below.
    PopupView/*(
        headerContent = {
            Column(
                Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.Blue),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Example header")
            }
        }
    )*/ {
        Column() {

        }

        Text("testing popup window")
    }
}