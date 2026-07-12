package com.example.csoftproject.navigation

fun getBottomItemList(): List<BottomNavItem> {
    return listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Transactions,
        BottomNavItem.Catalog,
        BottomNavItem.Statistics
    )
}