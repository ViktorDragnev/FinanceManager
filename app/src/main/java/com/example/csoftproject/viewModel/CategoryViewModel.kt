package com.example.csoftproject.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csoftproject.domain.services.CategoryDataService
import com.example.csoftproject.domain.services.ExpenseDataService
import com.example.csoftproject.domain.utils.simulateError
import com.example.csoftproject.ui.state.CategoryUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CategoryViewModel(
    categoryDataService: CategoryDataService,
    expenseDataService: ExpenseDataService
): ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _errorOccurred = MutableStateFlow(false)
    val errorMessage = _errorOccurred.asStateFlow()

    private val _uiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _categories = categoryDataService.getCategoryFlow()
    val categories = _categories
    
    val expenses = expenseDataService.getExpenseFlow()

    init {
        viewModelScope.launch {
            categories.collect { list ->
                if (_uiState.value !is CategoryUiState.Error) {
                    _uiState.value = if (list.isEmpty()) {
                        CategoryUiState.EmptyList("No categories found")
                    } else {
                        CategoryUiState.Success(list)
                    }
                }
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                _errorOccurred.value = false

                _isRefreshing.value = true

                _isRefreshing.value = false

                _uiState.value = CategoryUiState.Loading

                delay(1000)

                if (simulateError()) {
                    throw Exception("Simulated error")
                }

                val list = categories.value

                _uiState.value = if (list.isEmpty()) {
                    CategoryUiState.EmptyList("No categories found")
                } else {
                    CategoryUiState.Success(list)
                }

            } catch (_: Exception) {
                _uiState.value = CategoryUiState.Error("An error occurred")
                _errorOccurred.value = true
            }
        }
    }
}