package com.example.csoftproject.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csoftproject.domain.services.CategoryDataService
import com.example.csoftproject.domain.services.ExpenseDataService
import com.example.csoftproject.domain.models.Category
import com.example.csoftproject.domain.utils.simulateError
import com.example.csoftproject.ui.state.ExpenseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val categoryDataService: CategoryDataService,
    private val expenseDataService: ExpenseDataService
) : ViewModel() {

    private val _expenses = expenseDataService.getExpenseFlow()
    val expenses = _expenses

    val categories = categoryDataService.getCategoryFlow()

    private val _expenseUiState = MutableStateFlow<ExpenseUiState>(ExpenseUiState.Loading)
    val uiState = _expenseUiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {

            try {

                _expenses.collect { list ->
                    _expenseUiState.value =
                        if (list.isEmpty()) ExpenseUiState.EmptyList("No expenses found")
                        else ExpenseUiState.Success(list)
                }

            }catch (_: Exception){
                _expenseUiState.value = ExpenseUiState.Error("Could not load expenses")
            }
        }
    }

    fun findCategoryById(id: Int): Category? {
        return categories.value.find { it.id == id }
    }
}

