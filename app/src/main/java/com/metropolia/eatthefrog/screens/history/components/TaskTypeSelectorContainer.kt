package com.metropolia.eatthefrog.screens.history.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.HistoryScreenViewModel
import java.util.*

@Composable
fun TaskTypeSelectorContainer(vm: HistoryScreenViewModel) {

    var selectedTypes = vm.selectedTypes.observeAsState()

    @Composable
    fun TypeRow(type: String) {

        var name = type
        name = name.lowercase(Locale.getDefault())
        name = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        Row(
            Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (selectedTypes.value?.contains(type) == true) MaterialTheme.colors.primary
                    else MaterialTheme.colors.primaryVariant
                )
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .clickable {
                    vm.toggleSelectedType(type)
                },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = name,
                color = Color.White
            )
            if (selectedTypes.value?.contains(type) == true) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_close_24),
                    contentDescription = "close",
                    modifier = Modifier.size(15.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
    }

    LazyRow(Modifier.fillMaxWidth()) {
        if (vm.getAllTaskTypes().isNotEmpty()) item { TypeRow("All") }
        items(vm.getAllTaskTypes()) { TypeRow(it.name) }
    }
}