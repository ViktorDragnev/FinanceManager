package com.example.csoftproject.viewModel.factory

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.csoftproject.data.database.DatabaseImpl
import com.example.csoftproject.domain.services.room.RoomCategoryDataService
import com.example.csoftproject.domain.services.room.RoomExpenseDataService
import com.example.csoftproject.domain.services.room.RoomSavingsGoalDataService
import com.example.csoftproject.viewModel.*

fun createAppViewModelFactory(context: Context) : ViewModelProvider.Factory {
    val db = DatabaseImpl.getInstance(context.applicationContext)
    val categoryService = RoomCategoryDataService(db.categoryDao)
    val expenseService = RoomExpenseDataService(db.expenseDao, db.categoryDao)
    val savingsGoalService = RoomSavingsGoalDataService(db.savingsGoalDao)

    return viewModelFactory {
        initializer {
            DashboardViewModel(
                categoryService,
                expenseService
            )
        }
        initializer {
            TransactionsViewModel(
            expenseService,
            categoryService
        ) }
        initializer {
            AddExpenseViewModel(
            expenseService,
            categoryService
        ) }
        initializer {
            EditExpenseViewModel(
            expenseService,
            categoryService
        ) }
        initializer {
            CategoryViewModel(
                categoryService,
                expenseService
            )
        }
        initializer {
            AddCategoryViewModel(
                categoryService
            )
        }
        initializer {
            EditCategoryViewModel(
                categoryService
            )
        }
        initializer {
            StatisticsViewModel(
                expenseService,
                categoryService
            )
        }
        initializer {
            SavingsGoalViewModel(
                savingsGoalService
            )
        }
    }
}
