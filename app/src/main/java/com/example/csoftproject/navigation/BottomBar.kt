package com.example.csoftproject.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer
            ),
    ) {
        getBottomItemList().forEach { item ->
            NavigationItem(
                screen = item,
                isSelected = currentRoute == item.route,
                onItemClick = {
                    navController.navigateToBottomItem(item)
                }
            )
        }
    }
}

fun NavController.navigateToBottomItem(bottomNavItem: BottomNavItem) {
    navigate(bottomNavItem.route){
        popUpTo(graph.findStartDestination().id){
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun RowScope.NavigationItem(
    screen: BottomNavItem,
    isSelected: Boolean,
    onItemClick: () -> Unit
) {
    NavigationBarItem(
        icon = {
            Icon(
                painterResource(id = screen.icon),
                contentDescription = screen.title,
                Modifier.size(20.dp)
            )
        },
        selected = isSelected,
        alwaysShowLabel = true,
        onClick = onItemClick,
        colors = NavigationBarItemDefaults.colors()
    )
}

