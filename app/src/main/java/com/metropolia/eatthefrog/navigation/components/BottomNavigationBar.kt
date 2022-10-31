package com.metropolia.eatthefrog.navigation.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.navigation.NavigationItem

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.AddTask,
        NavigationItem.Profile
    )
    BottomNavigation(backgroundColor = MaterialTheme.colors.surface) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item -> 
            BottomNavigationItem(
                icon =  { Icon(painterResource(id = item.icon), contentDescription = stringResource(id = item.title))},
                label = { Text(text = stringResource(id = item.title))},
                selectedContentColor = MaterialTheme.colors.primaryVariant,
                unselectedContentColor = MaterialTheme.colors.secondary,
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    // BottomNavigationBar()
}

