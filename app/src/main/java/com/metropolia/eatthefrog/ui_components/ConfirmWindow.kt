package com.metropolia.eatthefrog.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R


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
            TextButton(
                modifier = Modifier
                    .height(40.dp),
                onClick = {
                if (dismissCallback != null) {
                    dismissCallback()
                }
            }) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .height(40.dp),
                onClick = {
                if (confirmCallback != null) {
                    confirmCallback()
                }
            }) {
                Text(stringResource(R.string.confirm))
            }
        },
        text = {
            Column {
                Text(description)
            }
    })
}