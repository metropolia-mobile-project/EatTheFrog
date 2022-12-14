package com.metropolia.eatthefrog.screens.profile.components

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.TaskEntry
import com.metropolia.eatthefrog.viewmodels.ProfileScreenViewModel
import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatryk.vico.compose.axis.horizontal.topAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.column.columnChart
import com.patrykandpatryk.vico.compose.component.shape.lineComponent
import com.patrykandpatryk.vico.compose.component.shape.textComponent
import com.patrykandpatryk.vico.core.component.shape.Shapes
import java.util.*

/**
 * Displays a graph which contains amount of closed tasks on the Y-axis and date on the X-axis.
 * Displays also a "*" character next to dates which have a completed frog within them.
 * @param vm: ProfileScreenViewModel of the parent composable.
 */
@Composable
fun ProfileGraphContainer(vm: ProfileScreenViewModel) {


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
            ActivityGraph(vm)
        }
    }
}

/**
 * Displays a help text how to read the Graph.
 */
@Composable
fun GraphInfo() {
    Row(
        Modifier
            .padding(10.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {

        Text(stringResource(R.string.activity))
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                Text(stringResource(R.string.tasks_completed), modifier = Modifier.padding(end = 10.dp))
                Box(
                    Modifier
                        .width(10.dp)
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(colors.primaryVariant))
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                Text(stringResource(R.string.frogs_completed), modifier = Modifier.padding(end = 10.dp))
                Box(
                    Modifier
                        .wrapContentSize()
                        .clip(CircleShape)) {
                    Text("*")
                }
            }
        }
    }
}

/**
 * Displays a Graph displaying the user's activity.
 * Displays how many tasks per day has been completed, as well as if the daily has been completed.
 * @param vm: ProfileScreenViewModel of the parent composable.
 */
@Composable
fun ActivityGraph(vm: ProfileScreenViewModel) {
    val bottomAxis = bottomAxis(

        label = textComponent(),

        valueFormatter = { value, chartValues ->
            var entries : TaskEntry? = try {
                chartValues.chartEntryModel.entries[0].getOrNull(value.toInt()) as TaskEntry?
            } catch (e: Exception) {
                TaskEntry(Date(), false, 0f, 0f)
            }

            entries?.run {
                if(y == 0f) ""
                else "${getDay(date)}/${getMonth(date)} ${if (frogCompleted) "*" else ""}"

            }?.orEmpty()
                ?: entries
                    ?.date
                    ?.run { "" }
                    .orEmpty()
        },
    )

    val topAxis = topAxis(
        label = textComponent(),

        valueFormatter = { value, chartValues ->
            var entries : TaskEntry? = try {
                chartValues.chartEntryModel.entries[0].getOrNull(value.toInt()) as TaskEntry?
            } catch (e: Exception) {
                TaskEntry(Date(), false, 0f, 0f)
            }

            entries?.run {
                if(y == 0f) ""
                else "${y.toInt()}"

            }?.orEmpty()
                ?: entries
                    ?.date
                    ?.run { "" }
                    .orEmpty()
        },
    )

    Chart(
        modifier = Modifier
            .fillMaxHeight(),
        chart = columnChart(
            spacing = 40.dp,
            columns = listOf(lineComponent(
                thickness = 20.dp, color = colors.primaryVariant,
                shape = Shapes.roundedCornerShape(topLeftPercent = 50)))),
        chartModelProducer = vm.getTasksCompletedEntries(),
            bottomAxis = bottomAxis,
            topAxis = topAxis)

}

/** Functions used to format the day and month of a Date object to a CharSequence. */
fun getDay(date: Date): CharSequence = DateFormat.format("dd", date)
fun getMonth(date: Date): CharSequence = DateFormat.format("MM", date)