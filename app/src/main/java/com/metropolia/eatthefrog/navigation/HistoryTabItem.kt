package com.metropolia.eatthefrog.navigation

import androidx.compose.runtime.Composable

typealias ComposableFun = @Composable () -> Unit

/**
 * Used for creating the HistoryTab navigation items.
 * @param icon: ID of the icon.
 * @param title: text to be displayed on the tab.
 * @param screen: composable to be displayed on the screen.
 */
class HistoryTabItem(var icon: Int, var title: String, var screen: ComposableFun)