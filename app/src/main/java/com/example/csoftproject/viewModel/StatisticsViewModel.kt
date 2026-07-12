package com.example.csoftproject.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csoftproject.domain.enums.FilterForTimeFrame
import com.example.csoftproject.domain.services.CategoryDataService
import com.example.csoftproject.domain.services.ExpenseDataService
import com.example.csoftproject.domain.utils.filterExpensesByTimeFrame
import com.example.csoftproject.domain.utils.simulateError
import com.example.csoftproject.ui.state.ExpenseUiState
import com.example.csoftproject.viewModel.vm_util_interfaces.FiltrationByTimeFrame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class StatisticsViewModel(
    expenseDataService: ExpenseDataService,
    categoryDataService: CategoryDataService
): ViewModel(), FiltrationByTimeFrame {

    private val _expenses = expenseDataService.getExpenseFlow()
    val expenses = _expenses

    private val _categories = categoryDataService.getCategoryFlow()
    val categories = _categories

    private val _filterByFilterForTimeFrame = MutableStateFlow(FilterForTimeFrame.THIS_YEAR)
    private val _customDateRange = MutableStateFlow<Pair<LocalDate?, LocalDate?>>(null to null)
    val customDateRange = _customDateRange.asStateFlow()
    private val _expenseUiState = MutableStateFlow<ExpenseUiState>(ExpenseUiState.Loading)
    val uiState = _expenseUiState.asStateFlow()
    private val _errorOccurred = MutableStateFlow(false)
    val errorOccurred = _errorOccurred.asStateFlow()


    override fun changeFilterByTimeFrame(filterForTimeFrame: FilterForTimeFrame) {
        _filterByFilterForTimeFrame.value = filterForTimeFrame
    }

    override fun setCustomDateRange(start: LocalDate?, end: LocalDate?) {
        _customDateRange.value = start to end
    }

    val filteredExpenses =
        combine(
            _expenses,
            _filterByFilterForTimeFrame,
            _customDateRange
        ){
            expenses, timeFrame, dateRange ->

            val (startDate, endDate) = dateRange

            expenses
                .let { filterExpensesByTimeFrame(timeFrame, it, startDate, endDate) }

        }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    init {
        loadStatistics()
    }

    fun loadStatistics(){
        viewModelScope.launch {
            try {

                _errorOccurred.value = false

                if (simulateError()) {
                    throw Exception()
                }

                _expenseUiState.value = ExpenseUiState.Success(expenses.value)
            }catch (_: Exception){
                _expenseUiState.value = ExpenseUiState.Error("Could not data!")
            }
        }
    }
}