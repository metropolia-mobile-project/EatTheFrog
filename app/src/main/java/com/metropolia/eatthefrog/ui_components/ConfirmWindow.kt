package com.metropolia.eatthefrog.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/**
 * A confirmation window.
 * @param confirmCallback: Called whenever the user clicks the confirm button.
 * @param dismissCallback: Called whenever the user clicks outside the popup or the dismiss button.
 * @param description: The text displayed on the window.
 */
@Composable
fun ConfirmWindow(confirmCallback: (() -> Any?)?, dismissCallback: (() -> Any?)?,
                  description: String,
                  modifier: Modifier = Modifier) {
    AlertDialog(onDismissRequest = {
        if (dismissCallback != null) {
            dismissCallback()
        }
    }, modifier = modifier,
        dismissButton = {
            Button(onClick = {
                if (dismissCallback != null) {
                    dismissCallback()
                }
            }) {
                Text("Cancel")
            }
        },
        confirmButton = {
            Button(onClick = {
                if (confirmCallback != null) {
                    confirmCallback()
                }
            }) {
                Text("Confirm")
            }
        },
        text = {
            Column {
                Text(description)
            }
    })
}