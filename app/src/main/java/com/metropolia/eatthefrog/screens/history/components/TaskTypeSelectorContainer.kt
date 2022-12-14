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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.database.TaskType
import com.metropolia.eatthefrog.viewmodels.HistoryScreenViewModel

/**
 * Contains a LazyRow, which displays all TaskTypes found in the database. Clicking on the TaskTypes filters the displayed
 * Tasks within the CompleteTasks and IncompleteTasks screens.
 * @param vm: HistoryScreenViewModel of the parent composable.
 */
@Composable
fun TaskTypeSelectorContainer(vm: HistoryScreenViewModel) {

    val taskTypes = vm.getAllTaskTypes().observeAsState(listOf(TaskType(name = stringResource(id = R.string.loading), icon = null)))
    val selectedTypes = vm.selectedTypes.observeAsState()

    /**
     * Single TaskType item for the LazyRow.
     */
    @Composable
    fun TypeRow(type: TaskType) {

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
                text = type.name,
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
        if (taskTypes.value.isNotEmpty()) item { TypeRow(vm.all) }
        items(taskTypes.value) { TypeRow(it) }
    }
}