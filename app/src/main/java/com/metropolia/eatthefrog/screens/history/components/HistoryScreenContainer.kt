package com.metropolia.eatthefrog.screens.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.navigation.HistoryTabItem
import com.metropolia.eatthefrog.viewmodels.HistoryScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HistoryScreenContainer(vm: HistoryScreenViewModel) {


    val completedTab = HistoryTabItem(R.drawable.ic_baseline_task_alt_24, stringResource(R.string.complete)) {
        CompletedTasksContainer(vm)
    }
    val incompleteTab = HistoryTabItem(R.drawable.ic_baseline_incomplete_circle_24,stringResource(R.string.incomplete)) {
            IncompleteTasksContainer(vm)
        }


    val tabs = listOf(
        completedTab,
        incompleteTab,
    )
    val pagerState = rememberPagerState()

    Scaffold(
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primaryVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                HistorySearchContainer(vm)
            }
        },
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            HistoryTabs(tabs, pagerState)
            HistoryTabContent(tabs, pagerState)
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun HistoryTabs(tabs: List<HistoryTabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }) {
        tabs.forEachIndexed { index, tab ->
            LeadingIconTab(
                icon = { Icon(painter = painterResource(tab.icon), contentDescription = "") },
                text = { Text(tab.title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HistoryTabContent(tabs: List<HistoryTabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState, count = tabs.size, modifier = Modifier.background(MaterialTheme.colors.secondary)) { page ->
        tabs[page].screen()
    }
}