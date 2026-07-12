package com.example.csoftproject.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csoftproject.domain.enums.SortingExpenseOptions
import com.example.csoftproject.domain.enums.FilterForTimeFrame
import com.example.csoftproject.domain.services.CategoryDataService
import com.example.csoftproject.domain.services.ExpenseDataService
import com.example.csoftproject.domain.models.Category
import com.example.csoftproject.domain.utils.filterExpensesByCategory
import com.example.csoftproject.domain.utils.filterExpensesByTimeFrame
import com.example.csoftproject.domain.utils.simulateError
import com.example.csoftproject.domain.utils.sortExpensesByValueOrDate
import com.example.csoftproject.ui.state.ExpenseUiState
import com.example.csoftproject.viewModel.vm_util_interfaces.FiltrationByTimeFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class TransactionsViewModel(
    private val expenseDataService : ExpenseDataService,
    private val categoryDataService: CategoryDataService
): ViewModel(), FiltrationByTimeFrame {

    private val _sortByDateOrValue = MutableStateFlow(SortingExpenseOptions.DATE_DESCENDING)
    private val _filterByCategory = MutableStateFlow<Int?>(null)
    private val _filterByFilterForTimeFrame = MutableStateFlow(FilterForTimeFrame.THIS_YEAR)
    private val _customDateRange = MutableStateFlow<Pair<LocalDate?, LocalDate?>>(null to null)


    val categories = categoryDataService.getCategoryFlow()
    private val _expenses = expenseDataService.getExpenseFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _errorOccurred = MutableStateFlow(false)

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _expenseUiState = MutableStateFlow<ExpenseUiState>(ExpenseUiState.Loading)
    val uiState = _expenseUiState.asStateFlow()

    val filteredExpenses =
        combine(
            _expenses,
            _filterByCategory,
            _sortByDateOrValue,
            _filterByFilterForTimeFrame,
            _customDateRange
        ) { expenses, filter, sort, timeFrame, dateRange ->

            val (startDate, endDate) = dateRange

            expenses
                .let { filterExpensesByCategory(it, filter) }
                .let { sortExpensesByValueOrDate(it, sort) }
                .let { filterExpensesByTimeFrame(timeFrame, it, startDate, endDate) }
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun changeFilterByCategory(categoryId: Int?) {
        _filterByCategory.value = categoryId
    }

    fun changeSortType(type: SortingExpenseOptions) {
        _sortByDateOrValue.value = type
    }

    override fun changeFilterByTimeFrame(filterForTimeFrame: FilterForTimeFrame) {
        _filterByFilterForTimeFrame.value = filterForTimeFrame
    }

    override fun setCustomDateRange(start: LocalDate?, end: LocalDate?) {
        _customDateRange.value = start to end
    }

    init {
        viewModelScope.launch {
            try {
                combine(
                    filteredExpenses,
                    errorMessage
                ) { expenses, error ->
                    when {
                        error != null -> ExpenseUiState.Error(error)
                        expenses.isEmpty() -> ExpenseUiState.EmptyList("No transactions found")
                        else -> ExpenseUiState.Success(expenses)
                    }
                }.collect { _expenseUiState.value = it }

            }catch (_: Exception){
                _expenseUiState.value = ExpenseUiState.Error("Could not load expenses")
            }
        }
    }

    fun loadTransactionHistory() {
        viewModelScope.launch {
            try {
                _errorOccurred.value = false

                _isRefreshing.value = true

                _isRefreshing.value = false

                _expenseUiState.value = ExpenseUiState.Loading

                delay(1000)

                if (simulateError()) {
                    throw Exception("Simulated error")
                }

                val list = _expenses.value

                _expenseUiState.value = if (list.isEmpty()) {
                    ExpenseUiState.EmptyList("No categories found")
                } else {
                    ExpenseUiState.Success(list)
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