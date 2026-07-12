package com.example.csoftproject.ui.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csoftproject.domain.enums.PaymentMethod
import com.example.csoftproject.domain.models.Category
import com.example.csoftproject.ui.components.BackButton
import com.example.csoftproject.ui.components.ErrorDialog
import com.example.csoftproject.viewModel.AddExpenseViewModel
import java.time.Instant
import java.time.ZoneId

@Composable
fun AddExpenseFormScreen(
    navController: NavController,
    addExpenseViewModel: AddExpenseViewModel
) {
    val errorOccurred = addExpenseViewModel.errorOccurred.collectAsState().value

    val categories by addExpenseViewModel.categories.collectAsState()
    val state by addExpenseViewModel.formState.collectAsState()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(millis: Long) = millis <= System.currentTimeMillis()
        }
    )

    val selectedDate = datePickerState.selectedDateMillis?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    if (selectedDate != null && selectedDate != state.date) {
        addExpenseViewModel.onDateChanged(selectedDate)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {

                    BackButton(
                        navController = navController,
                        modifier = Modifier
                            .padding(24.dp)
                            .align(Alignment.CenterStart)
                    )

                    Text(
                        text = "Add Expense",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }

                TextField(
                    value = state.value,
                    onValueChange = { addExpenseViewModel.onValueChanged(it) },
                    isError = state.valueError != null,
                    supportingText = { state.valueError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Value") }
                )

                CategorySelector(
                    state = state,
                    categories = categories,
                    onCategorySelected = { addExpenseViewModel.onCategoryChanged(it.id) },
                    onExpandChange = { addExpenseViewModel.onCategoryExpandChanged(it) }
                )
                state.categoryError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                DatePicker(state = datePickerState)

                TextField(
                    value = state.description,
                    onValueChange = { addExpenseViewModel.onDescriptionChanged(it) },
                    isError = state.descriptionError != null,
                    supportingText = { state.descriptionError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Description") }
                )

                PaymentSelector(
                    state = state,
                    onPaymentSelected = {
                        addExpenseViewModel.onPaymentMethodChanged(it)
                                        },
                    onExpandChange = { addExpenseViewModel.onPaymentExpandChanged(it) },
                )
                state.paymentMethodError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                Button(
                    onClick = {
                        addExpenseViewModel.addExpense(
                            onSuccess = { navController.popBackStack() }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Expense")
                }
            }
        }
    }

    if (errorOccurred) {
        ErrorDialog(
            onRetry = {
                addExpenseViewModel.addExpense(
                    onSuccess = { navController.popBackStack() }
                )
            }
        )
        return
    }
}

@Composable
fun CategorySelector(
    state: ExpenseFormState,
    categories: List<Category>,
    onCategorySelected: (Category) -> Unit,
    onExpandChange: (Boolean) -> Unit

) {
    Column {
        Button(
            onClick = { onExpandChange(true) },
            modifier = Modifier.fillMaxWidth()
        ) {
            val selectedCategoryName = categories
                .firstOrNull { it.id == state.category }
                ?.name ?: "Select Category"

            Text(selectedCategoryName)
        }

        DropdownMenu(
            expanded = state.isCategoryExpanded,
            onDismissRequest = { onExpandChange(false) }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        onCategorySelected(category)
                        onExpandChange(false)
                    }
                )
            }
        }
    }
}

@Composable
fun PaymentSelector(
    state: ExpenseFormState,
    onPaymentSelected: (PaymentMethod) -> Unit,
    onExpandChange: (Boolean) -> Unit,
) {

    var isClicked by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = {
                onExpandChange(true)
                isClicked = true
                      },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(state.paymentMethod?.name ?: "Select Payment Method")
        }

        DropdownMenu(
            expanded = state.isPaymentExpanded,
            onDismissRequest = { onExpandChange(false) }
        ) {
            PaymentMethod.entries.forEach { method ->
                DropdownMenuItem(
                    text = { Text(method.name) },
                    onClick = {
                        onPaymentSelected(method)
                        onExpandChange(false)
                    }
                )
            }
        }
    }
}

