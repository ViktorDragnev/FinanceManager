package com.example.csoftproject.ui.state

import com.example.csoftproject.domain.models.Expense

sealed class ExpenseUiState {

    object Loading : ExpenseUiState()
    data class Success(val data: List<Expense>) : ExpenseUiState()
    data class EmptyList(val message: String) : ExpenseUiState()
    data class Error(val message: String) : ExpenseUiState()
}
