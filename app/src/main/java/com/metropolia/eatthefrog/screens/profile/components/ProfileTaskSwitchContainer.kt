package com.metropolia.eatthefrog.screens.profile.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.MutableLiveData
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.ProfileScreenViewModel

/**
 * SwitchContainer Function creates the bottom of the profile screen where
 * option switches are shown
 */

@Composable
fun ProfileTaskSwitchContainer(vm: ProfileScreenViewModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 5.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(stringResource(R.string.settings), modifier = Modifier.padding(bottom = 20.dp))
            SwitchRow(stringResource(R.string.dark_mode),
                R.drawable.ic_darkmode, R.drawable.ic_lightmode,
                vm.darkmode
            ) { vm.toggleDarkMode() }

            SwitchRow(stringResource(R.string.confirm_window),
                R.drawable.ic_confirm_on, R.drawable.ic_confirm_off,
                enabled = vm.showConfirmWindow,
                toggleState = { vm.toggleConfirmWindow() })

            SwitchRow(stringResource(R.string.deadline_rem),
                R.drawable.ic_deadline_on, R.drawable.ic_deadline_off,
                enabled = vm.showDeadline,
                toggleState = { vm.toggleDeadline() })

            val deadlineState = vm.showDeadline.observeAsState()
            if (deadlineState.value == true) {
                NotificationDropdown(vm)
            }
        }
    }
}

@Composable
fun SwitchRow(
    desc: String,
    enabledIcon: Int,
    disabledIcon: Int? = enabledIcon,
    enabled: MutableLiveData<Boolean>,
    toggleState: () -> Unit
) {

    var checkedState = enabled.observeAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {


        Row(verticalAlignment = Alignment.CenterVertically) {

            Card(
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .background(MaterialTheme.colors.background),
                shape = CircleShape,
                elevation = 5.dp,
            ) {

                Column(
                    Modifier
                        .padding(10.dp)
                ) {
                    Image(
                        painter = painterResource(
                            if (enabled.value == true) enabledIcon
                            else disabledIcon ?: enabledIcon
                        ),
                        contentDescription = "darkmode_lightmode"
                    )
                }

            }
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = desc,
                fontWeight = FontWeight.Light,
                fontSize = 13.sp
            )
        }

        /*if (checkedState.value == true) {
            NotificationDropdown()
        }*/

        Switch(
            checked = checkedState.value ?: false,
            onCheckedChange = { toggleState() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primaryVariant
            )
        )
    }
}

@Composable
fun NotificationDropdown(viewModel: ProfileScreenViewModel) {
    val listItems = listOf(
        "At deadline",
        "5 mins before",
        "10 mins before",
        "30 min before",
        "1 hour before"
    )

    val indexValue = viewModel.deadlineValue.observeAsState()
    Log.d("FUCK ", indexValue.value.toString())
    val disabledValue = ""
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(indexValue.value!!) }

    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(
            listItems[selectedIndex],
            fontWeight = FontWeight.Light,
            fontSize = 13.sp,
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier
                .clickable(onClick = { expanded = true })
                .background(Color.Transparent)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .wrapContentWidth()
                .background(MaterialTheme.colors.primaryVariant)
        ) {
            listItems.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    viewModel.deadlineValue.value = index
                    viewModel.saveDeadlineValue()
                    expanded = false
                }) {
                    val disabledText = if (s == disabledValue) {
                        " (Disabled)"
                    } else {
                        ""
                    }
                    Text(text = s + disabledText)
                }
            }
        }
    }
}