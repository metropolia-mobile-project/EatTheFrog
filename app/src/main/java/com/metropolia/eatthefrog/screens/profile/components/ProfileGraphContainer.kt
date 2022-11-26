package com.metropolia.eatthefrog.screens.profile.components

import android.text.format.DateFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.TaskEntry
import com.metropolia.eatthefrog.viewmodels.ProfileScreenViewModel
import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.startAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.column.columnChart
import com.patrykandpatryk.vico.compose.component.shape.lineComponent
import com.patrykandpatryk.vico.core.component.shape.Shapes

import com.patrykandpatryk.vico.core.entry.composed.plus
import java.util.*

@Composable
fun ProfileGraphContainer(vm: ProfileScreenViewModel) {

    val types = vm.selectedTypes.observeAsState()

    Card(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .height(370.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = colors.background,
        elevation = 5.dp,
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {

            GraphInfo()
            GraphFilterButtons(vm)

            if (types.value?.contains("All") == true || (types.value?.contains("Frogs") == true && types.value?.contains("Tasks") == true)) {
                ActivityGraph(vm, "All")
            } else if (types.value?.contains("Frogs") == true) {
                ActivityGraph(vm, "Frogs")
            }else {
                ActivityGraph(vm, "Tasks")
            }
        }
    }

}

@Composable
fun GraphInfo() {
    Row(Modifier.padding(10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {

        Text(stringResource(R.string.activity))
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                Text(stringResource(R.string.frogs_completed), modifier = Modifier.padding(end = 10.dp))
                Box(
                    Modifier
                        .width(10.dp)
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.green)))
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                Text(stringResource(R.string.tasks_completed), modifier = Modifier.padding(end = 10.dp))
                Box(
                    Modifier
                        .width(10.dp)
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(colors.primaryVariant))
            }
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
                    if (selectedTypes.value?.contains(filter) == true) colors.primary
                    else colors.primaryVariant
                )
                .clickable { vm.toggleSelectedType(filter) }
                .padding(horizontal = 10.dp, vertical = 5.dp),
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

@Composable
fun ActivityGraph(vm: ProfileScreenViewModel, type: String) {

    Log.d("selectedGraphs updated", "")

    val bottomAxis = bottomAxis(
        valueFormatter = { value, chartValues ->
            (chartValues.chartEntryModel.entries[0].getOrNull(value.toInt()) as TaskEntry?)
                ?.run { "${getDay(date)}/${getMonth(date)}" }
                .orEmpty()
        },
    )

    val startAxis = startAxis(
        maxLabelCount = 4,
        valueFormatter = { value, chartValues ->
            (chartValues.chartEntryModel.entries[0].getOrNull(value.toInt()) as TaskEntry?)
                ?.run { "$taskCompletedAmount" }
                .orEmpty()
        },
    )

    Chart(
        modifier = Modifier.fillMaxSize(),
        chart = columnChart(
            spacing = 40.dp,
            columns =
            if (type == "Frogs") listOf(lineComponent(
                thickness = 10.dp, color = colorResource(R.color.green),
                shape = Shapes.pillShape))
            else listOf(lineComponent(
                thickness = 20.dp, color = colors.primaryVariant,
                shape = Shapes.pillShape),
                lineComponent(
                thickness = 10.dp, color = colorResource(R.color.green),
                shape = Shapes.pillShape))),
        chartModelProducer = when (type) {
            "All" -> vm.getTasksCompletedEntries() + vm.getFrogsCompletedEntries()
            "Frogs" -> vm.getFrogsCompletedEntries()
            else -> vm.getTasksCompletedEntries()
        },
            startAxis = startAxis,
            bottomAxis = bottomAxis)
}

fun getDay(date: Date): CharSequence = DateFormat.format("dd", date)
fun getMonth(date: Date): CharSequence = DateFormat.format("MM", date)
