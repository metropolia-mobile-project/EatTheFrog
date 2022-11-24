package com.metropolia.eatthefrog.screens.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.ProfileScreenViewModel

@Composable
fun ProfileGraphContainer(vm: ProfileScreenViewModel) {

    Card(
        modifier = Modifier
            .padding(top = 10.dp)
            .height(300.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 5.dp,
    ) {
        /*TODO: Add graph */
        Column(Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
            GraphFilterButtons(vm)
        }
    }

}

@Composable
fun GraphFilterButtons(vm: ProfileScreenViewModel) {

    var selectedTypes = vm.selectedTypes.observeAsState()

    @Composable
    fun GraphButton(filter: String, left: Boolean, center: Boolean) {
        Row(
            Modifier
                .clip(
                    RoundedCornerShape(
                        bottomStart = if (left && !center) 20.dp else 0.dp,
                        topStart = if (left && !center) 20.dp else 0.dp,
                        bottomEnd = if (!left && !center) 20.dp else 0.dp,
                        topEnd = if (!left && !center) 20.dp else 0.dp,
                    )
                )
                .width(100.dp)
                .background(
                    if (selectedTypes.value?.contains(filter) == true) MaterialTheme.colors.primary
                    else MaterialTheme.colors.primaryVariant
                )
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .clickable { vm.toggleSelectedType(filter) },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = filter,
                color = Color.White
            )
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        GraphButton(filter = stringResource(R.string.all), left = true, center = false)
        GraphButton(filter = stringResource(R.string.tasks), left = false, center = true)
        GraphButton(filter = stringResource(R.string.frogs), left = false, center = false)
    }


}