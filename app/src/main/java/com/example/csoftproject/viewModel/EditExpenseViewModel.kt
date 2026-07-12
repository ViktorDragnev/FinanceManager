package com.example.csoftproject.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csoftproject.domain.enums.PaymentMethod
import com.example.csoftproject.domain.models.Expense
import com.example.csoftproject.domain.services.CategoryDataService
import com.example.csoftproject.domain.services.ExpenseDataService
import com.example.csoftproject.domain.utils.simulateError
import com.example.csoftproject.domain.validation.expense_validators.utils_validators.AmountValidator
import com.example.csoftproject.domain.validation.expense_validators.utils_validators.CategoryValidator
import com.example.csoftproject.domain.validation.expense_validators.utils_validators.DateValidator
import com.example.csoftproject.domain.validation.expense_validators.utils_validators.DescriptionValidator
import com.example.csoftproject.domain.validation.expense_validators.utils_validators.PaymentMethodValidator
import com.example.csoftproject.ui.state.CategoryUiState
import com.example.csoftproject.ui.transaction.ExpenseFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class EditExpenseViewModel(
    private val expenseDataService: ExpenseDataService,
    categoryDataService: CategoryDataService
) : ViewModel() {

    private val _errorOccurred = MutableStateFlow(false)
    val errorOccurred = _errorOccurred.asStateFlow()

    val expenses = expenseDataService.getExpenseFlow()
    val categories = categoryDataService.getCategoryFlow()

    private val _formState = MutableStateFlow(ExpenseFormState())
    val formState = _formState.asStateFlow()

    private val _categoryUiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)

    fun loadExpense(expense: Expense) {
        if (_formState.value.id == expense.id) return
        _formState.value = ExpenseFormState(
            id = expense.id,
            value = expense.transactionValue.toString(),
            category = expense.category.id,
            date = expense.transactionDate,
            description = expense.transactionDescription,
            paymentMethod = expense.paymentMethod,
            updateCount = expense.updateCount
        )
    }

    fun onValueChanged(value: String) {
        _formState.value = _formState.value.copy(
            value = value,
            valueError = AmountValidator.validate(value)
        )
    }

    fun onCategoryChanged(categoryId: Int) {
        _formState.value = _formState.value.copy(
            category = categoryId,
            categoryError = CategoryValidator.validate(categoryId)
        )
    }

    fun onDateChanged(date: LocalDate) {
        _formState.value = _formState.value.copy(
            date = date,
            dateError = DateValidator.validate(date)
        )
    }

    fun onDescriptionChanged(description: String) {
        _formState.value = _formState.value.copy(
            description = description,
            descriptionError = DescriptionValidator.validate(description)
        )
    }

    fun onPaymentMethodChanged(method: PaymentMethod) {
        _formState.value = _formState.value.copy(
            paymentMethod = method,
            paymentMethodError = PaymentMethodValidator.validate(method)
        )
    }

    fun onCategoryExpandChanged(expanded: Boolean) {
        _formState.value = _formState.value.copy(isCategoryExpanded = expanded)
    }

    fun onPaymentExpandChanged(expanded: Boolean) {
        _formState.value = _formState.value.copy(isPaymentExpanded = expanded)
    }

    fun updateExpense(onSuccess: () -> Unit) {
        val current = _formState.value

        val validated = validateForm(current)
        _formState.value = validated

        if (validated.hasErrors()) return

        val expense = validated.toUpdatedExpenseOrNull() ?: return

        viewModelScope.launch {
            try {
                saveExpense(expense, onSuccess)
            }catch (_: Exception){
                _categoryUiState.value = CategoryUiState.Error("Category could not be updated")
            }
        }
    }

    fun deleteExpense(expenseId: Int) {
        viewModelScope.launch {
            expenseDataService.deleteExpense(expenseId)
        }
    }

    private fun validateForm(current: ExpenseFormState): ExpenseFormState {
        return current.copy(
            valueError = AmountValidator.validate(current.value),
            categoryError = CategoryValidator.validate(current.category),
            dateError = DateValidator.validate(current.date),
            paymentMethodError = PaymentMethodValidator.validate(current.paymentMethod),
            descriptionError = DescriptionValidator.validate(current.description)
        )
    }

    private fun ExpenseFormState.hasErrors(): Boolean {
        return listOfNotNull(
            valueError,
            categoryError,
            dateError,
            paymentMethodError,
            descriptionError
        ).isNotEmpty()
    }

    private fun ExpenseFormState.toUpdatedExpenseOrNull(): Expense? {
        val id = this.id ?: return null
        val category = categories.value.find { it.id == this.category } ?: return null
        val date = this.date ?: return null
        val paymentMethod = this.paymentMethod ?: return null
        val value = this.value.toDoubleOrNull() ?: return null
        val updateCount = this.updateCount ?: return null

        return Expense(
            id = id,
            transactionValue = value,
            category = category,
            transactionDate = date,
            transactionDescription = this.description,
            paymentMethod = paymentMethod,
            updateCount = updateCount
        )
    }

    private suspend fun saveExpense(expense: Expense, onSuccess: () -> Unit) {

        _errorOccurred.value = false

        if (simulateError()) {
            throw Exception()
        }

        expenseDataService.updateExpense(expense)
        onSuccess()
    }
}