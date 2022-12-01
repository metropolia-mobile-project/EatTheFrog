package com.metropolia.eatthefrog.ui_components

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.CONFIRM_WINDOW_KEY
import com.metropolia.eatthefrog.constants.SHARED_PREF_KEY


/**
 * A confirmation window.
 * @param confirmCallback: Called whenever the user clicks the confirm button.
 * @param dismissCallback: Called whenever the user clicks outside the popup or the dismiss button.
 * @param description: The text displayed on the window.
 */
@Composable
fun ConfirmWindow(confirmCallback: (() -> Any?)?, dismissCallback: (() -> Any?)?,
                  description: String,
                  modifier: Modifier = Modifier, application: Application) {

    var disabledCheckbox by remember { mutableStateOf(false) }

    fun disableConfirmWindow() {
        val sharedPreferences: SharedPreferences = application.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putBoolean(CONFIRM_WINDOW_KEY, false)
            apply()
        }
    }

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
                    .height(40.dp)
                    .background(Color.Blue),
                onClick = {
                if (confirmCallback != null) {
                    if (disabledCheckbox) disableConfirmWindow()
                    confirmCallback()
                }
            }) {
                Text(stringResource(R.string.confirm))
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.SpaceAround) {
                Text(description)
                Spacer(Modifier.height(50.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.dont_ask_again))
                    Switch(
                        colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primaryVariant, uncheckedThumbColor = MaterialTheme.colors.primary),
                        checked = disabledCheckbox,
                        onCheckedChange = {disabledCheckbox = it})
                }
            }
    })
}