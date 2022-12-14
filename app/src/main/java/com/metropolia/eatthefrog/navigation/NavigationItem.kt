package com.metropolia.eatthefrog.navigation

import com.metropolia.eatthefrog.R

/**
 * Sealed class for all the navigation items of the different screens.
 * @param route: route used for navigation.
 * @param icon: Icon to be displayed on the navigation button.
 * @param title: Title to be dispalyed on the navigation button.
 */
sealed class NavigationItem(var route: String, var icon: Int, var title: Int) {
    object Home : NavigationItem("home", R.drawable.ic_home, R.string.home)
    object AddTask : NavigationItem("add_task/0/false/''/''/''/''/1/false", R.drawable.ic_add_task, R.string.add_task)
    object Profile : NavigationItem("profile", R.drawable.ic_profile, R.string.profile)
    object History : NavigationItem("history", R.drawable.ic_baseline_history_24, R.string.history)
}
