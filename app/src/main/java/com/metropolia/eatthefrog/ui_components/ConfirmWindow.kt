package com.metropolia.eatthefrog.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable


/**
 * A confirmation window.
 * @param confirmCallback: Called whenever the user clicks the confirm button.
 * @param dismissCallback: Called whenever the user clicks outside the popup or the dismiss button.
 * @param description: The text displayed on the window.
 */
@Composable
fun ConfirmWindow(confirmCallback: () -> Any, dismissCallback: () -> Any, description: String) {
    AlertDialog(onDismissRequest = { dismissCallback() },
        dismissButton = {
            Button(onClick = { dismissCallback() }) {
                Text("Cancel")
            }
        },
        confirmButton = {
            Button(onClick = { confirmCallback() }) {
                Text("Confirm")
            }
        },
        text = {
            Column {
                Text(description)
            }
    })
}