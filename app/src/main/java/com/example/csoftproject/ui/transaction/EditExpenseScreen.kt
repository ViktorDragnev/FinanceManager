package com.example.csoftproject.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csoftproject.ui.components.BackButton
import com.example.csoftproject.ui.components.ErrorDialog
import com.example.csoftproject.viewModel.EditExpenseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Instant
import java.time.ZoneId

@Composable
fun EditExpenseScreen(
    expenseId: Int,
    editExpenseViewModel: EditExpenseViewModel,
    navController: NavController
) {
    val errorOccurred = editExpenseViewModel.errorOccurred.collectAsState().value

    val expenses by editExpenseViewModel.expenses.collectAsState()
    val categories by editExpenseViewModel.categories.collectAsState()
    val state by editExpenseViewModel.formState.collectAsState()

    val openAlertDialog = remember { mutableStateOf(false) }

    val expense = expenses.find { it.id == expenseId }

    if (expense == null) {
        navController.popBackStack()
        return
    }

    remember(expense.id) {
        editExpenseViewModel.loadExpense(expense)
        true
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.date
            ?.atStartOfDay(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
    )

    val selectedDate = datePickerState.selectedDateMillis?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    if (selectedDate != null && selectedDate != state.date) {
        editExpenseViewModel.onDateChanged(selectedDate)
    }

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
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
                    text = "Edit Expense",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
        }

        item {
            TextField(
                value = state.value,
                onValueChange = { editExpenseViewModel.onValueChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Amount (€)") },
                isError = state.valueError != null,
                supportingText = {
                    state.valueError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
        }

        item {
            CategorySelector(
                state = state,
                categories = categories,
                onCategorySelected = { editExpenseViewModel.onCategoryChanged(it.id) },
                onExpandChange = { editExpenseViewModel.onCategoryExpandChanged(it) }
            )
            state.categoryError?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }

        item {
            DatePicker(state = datePickerState)
            state.dateError?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }

        item {
            TextField(
                value = state.description,
                onValueChange = { editExpenseViewModel.onDescriptionChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Description") },
                isError = state.descriptionError != null,
                supportingText = {
                    state.descriptionError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
        }

        item {
            PaymentSelector(
                state = state,
                onPaymentSelected = { editExpenseViewModel.onPaymentMethodChanged(it) },
                onExpandChange = { editExpenseViewModel.onPaymentExpandChanged(it) }
            )
            state.paymentMethodError?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = { openAlertDialog.value = true},
                    modifier = Modifier
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = "Delete"
                    )
                }

                Button(
                    onClick = {
                        editExpenseViewModel.updateExpense (
                            onSuccess = { navController.popBackStack() }
                        )
                    },
                    modifier = Modifier.padding(16.dp),
                    ) {
                    Text("Save")
                }
            }
        }
    }

    when {
        openAlertDialog.value -> {
            DeleteExpenseDialog(
                expenseId = expenseId,
                navController = navController,
                editExpenseViewModel = editExpenseViewModel
            )
        }
    }

    if (errorOccurred) {
        ErrorDialog(
            onRetry = {
                editExpenseViewModel.updateExpense(
                    onSuccess = { navController.popBackStack() }
                )
            }
        )
        return
    }
}