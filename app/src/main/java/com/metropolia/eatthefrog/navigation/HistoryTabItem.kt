package com.metropolia.eatthefrog.navigation

import androidx.compose.runtime.Composable

typealias ComposableFun = @Composable () -> Unit

class HistoryTabItem(var icon: Int, var title: String, var screen: ComposableFun)