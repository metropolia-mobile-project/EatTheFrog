package com.metropolia.eatthefrog.navigation

import com.metropolia.eatthefrog.R

sealed class NavigationItem(var route: String, var icon: Int, var title: Int) {
    object Home : NavigationItem("home", R.drawable.ic_home, R.string.home)
    object AddTask : NavigationItem("add_task/0/false/''/''/''/''/''", R.drawable.ic_add_task, R.string.add_task)
    object Profile : NavigationItem("profile", R.drawable.ic_profile, R.string.profile)
}
