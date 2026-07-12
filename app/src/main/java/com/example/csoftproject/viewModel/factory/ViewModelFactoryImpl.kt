package com.example.csoftproject.viewModel.factory

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.csoftproject.data.database.DatabaseImpl
import com.example.csoftproject.domain.services.room.RoomCategoryDataService
import com.example.csoftproject.domain.services.room.RoomExpenseDataService
import com.example.csoftproject.viewModel.AddCategoryViewModel
import com.example.csoftproject.viewModel.AddExpenseViewModel
import com.example.csoftproject.viewModel.CategoryViewModel
import com.example.csoftproject.viewModel.DashboardViewModel
import com.example.csoftproject.viewModel.EditCategoryViewModel
import com.example.csoftproject.viewModel.EditExpenseViewModel
import com.example.csoftproject.viewModel.StatisticsViewModel
import com.example.csoftproject.viewModel.TransactionsViewModel

fun createAppViewModelFactory(context: Context) : ViewModelProvider.Factory {
    val db = DatabaseImpl.getInstance(context.applicationContext)
    val categoryService = RoomCategoryDataService(db.categoryDao)
    val expenseService = RoomExpenseDataService(db.expenseDao, db.categoryDao)

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
                categoryService
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
    }
}