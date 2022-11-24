package com.metropolia.eatthefrog.screens.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                vm.darkmode) { vm.toggleDarkMode() }

            SwitchRow(stringResource(R.string.deadline_rem),
                R.drawable.ic_deadline_on, R.drawable.ic_deadline_off, enabled = vm.showDeadline,
                toggleState = { vm.toggleDeadline() })

            SwitchRow(stringResource(R.string.confirm_window),
                R.drawable.ic_confirm_on, R.drawable.ic_confirm_off, enabled = vm.showConfirmWindow,
                toggleState = { vm.toggleConfirmWindow() })
        }
    }
}

@Composable
fun SwitchRow(desc: String,
              enabledIcon: Int,
              disabledIcon: Int? = enabledIcon,
              enabled: MutableLiveData<Boolean>, toggleState: () -> Unit) {

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
                        .padding(10.dp)) {
                    Image(
                        painter = painterResource(
                            if (enabled.value == true) enabledIcon
                            else disabledIcon ?: enabledIcon
                        ),
                        contentDescription = "darkmode_lightmode")
                }

            }
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = desc,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                fontSize = 14.sp
            )
        }

        Switch(
            checked = checkedState.value ?: false,
            onCheckedChange = { toggleState() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primaryVariant
            )
        )
    }
}
