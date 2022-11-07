package com.metropolia.eatthefrog.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable


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