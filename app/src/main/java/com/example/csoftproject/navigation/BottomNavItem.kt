package com.example.csoftproject.navigation

import com.example.csoftproject.R


sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Dashboard : BottomNavItem(
        route = "dashboard",
        title = "Dashboard",
        icon = R.drawable.dashboard,
    )

    object Transactions : BottomNavItem(
        route = "transactions",
        title = "Transactions",
        icon = R.drawable.transaction_14855_64
    )

    object Catalog : BottomNavItem(
        route = "catalog",
        title = "Catalog",
        icon = R.drawable.kindpng_4927144
    )

    object Statistics : BottomNavItem(
        route = "statistics",
        title = "Statistics",
        icon = R.drawable.trend
    )

    object Savings : BottomNavItem(
        route = "savingsGoals",
        title = "Savings",
        icon = R.drawable.home
    )
}
