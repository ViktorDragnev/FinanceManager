package com.example.csoftproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.csoftproject.navigation.BottomNavItem
import com.example.csoftproject.navigation.BottomNavigation
import com.example.csoftproject.ui.category.AddCategoryScreen
import com.example.csoftproject.ui.category.CategoryListScreen
import com.example.csoftproject.ui.category.EditCategoryScreen
import com.example.csoftproject.ui.components.TransactionDetailsScreen
import com.example.csoftproject.ui.dashboard.DashboardScreen
import com.example.csoftproject.ui.savings.SavingsGoalScreen
import com.example.csoftproject.ui.statistics.StatisticsScreen
import com.example.csoftproject.ui.theme.CSoftProjectTheme
import com.example.csoftproject.ui.transaction.AddExpenseFormScreen
import com.example.csoftproject.ui.transaction.EditExpenseScreen
import com.example.csoftproject.ui.transaction.TransactionScreen
import com.example.csoftproject.viewModel.AddCategoryViewModel
import com.example.csoftproject.viewModel.AddExpenseViewModel
import com.example.csoftproject.viewModel.CategoryViewModel
import com.example.csoftproject.viewModel.DashboardViewModel
import com.example.csoftproject.viewModel.EditCategoryViewModel
import com.example.csoftproject.viewModel.EditExpenseViewModel
import com.example.csoftproject.viewModel.SavingsGoalViewModel
import com.example.csoftproject.viewModel.StatisticsViewModel
import com.example.csoftproject.viewModel.TransactionsViewModel
import com.example.csoftproject.viewModel.factory.createAppViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val vmFactory by lazy { createAppViewModelFactory(applicationContext) }

        val dashboardViewModel: DashboardViewModel by viewModels { vmFactory }
        val transactionsViewModel: TransactionsViewModel by viewModels { vmFactory }
        val addExpenseViewModel: AddExpenseViewModel by viewModels { vmFactory }
        val editExpenseViewModel: EditExpenseViewModel by viewModels { vmFactory }
        val categoryViewModel: CategoryViewModel by viewModels { vmFactory }
        val addCategoryViewModel: AddCategoryViewModel by viewModels { vmFactory }
        val editCategoryViewModel: EditCategoryViewModel by viewModels { vmFactory }
        val statisticsViewModel: StatisticsViewModel by viewModels { vmFactory }
        val savingsGoalViewModel: SavingsGoalViewModel by viewModels { vmFactory }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSoftProjectTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomNavigation(navController = navController)
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Dashboard.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        composable(BottomNavItem.Dashboard.route) {
                            DashboardScreen(
                                dashboardViewModel = dashboardViewModel,
                                modifier = Modifier,
                                navController = navController
                            )
                        }

                        composable(
                            route = "transaction/{transactionId}"
                        ) { backStackEntry ->
                            val expenseId = backStackEntry.arguments?.getString("transactionId")?.toInt()

                            TransactionDetailsScreen(
                                expenseId = expenseId,
                                navController = navController,
                                dashboardViewModel = dashboardViewModel
                            )
                        }

                        composable(BottomNavItem.Transactions.route) {
                            TransactionScreen(
                                navController = navController,
                                transactionsViewModel = transactionsViewModel
                            )
                        }

                        composable("addExpense") {
                            AddExpenseFormScreen(
                                navController = navController,
                                addExpenseViewModel = addExpenseViewModel
                            )
                        }

                        composable("editExpense/{expenseId}") { backStackEntry ->
                            val expenseId = backStackEntry.arguments?.getString("expenseId")?.toInt()
                                ?: return@composable

                            EditExpenseScreen(
                                expenseId = expenseId,
                                editExpenseViewModel = editExpenseViewModel,
                                navController = navController
                            )
                        }

                        composable(BottomNavItem.Catalog.route) {
                            CategoryListScreen(
                                navController = navController,
                                categoryViewModel = categoryViewModel
                            )
                        }

                        composable("addCategory") {
                            AddCategoryScreen(
                                navController = navController,
                                addCategoryViewModel = addCategoryViewModel
                            )
                        }

                        composable("editCategory/{categoryId}") { backStackEntry ->
                            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toInt()
                                ?: return@composable

                            EditCategoryScreen(
                                categoryId = categoryId,
                                navController = navController,
                                editCategoryViewModel = editCategoryViewModel
                            )
                        }

                        composable(BottomNavItem.Statistics.route) {
                            StatisticsScreen(
                                statisticsViewModel = statisticsViewModel,
                                modifier = Modifier
                            )
                        }

                        composable("savingsGoals") {
                            SavingsGoalScreen(
                                navController = navController,
                                viewModel = savingsGoalViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}