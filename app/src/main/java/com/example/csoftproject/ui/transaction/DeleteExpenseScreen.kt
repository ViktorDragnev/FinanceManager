package com.example.csoftproject.ui.transaction

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.csoftproject.viewModel.EditExpenseViewModel

@Composable
fun DeleteExpenseDialog(
    expenseId: Int,
    navController: NavController,
    editExpenseViewModel: EditExpenseViewModel
) {
    AlertDialog(
        onDismissRequest = {  },
        confirmButton = {
            TextButton(
                onClick = {
                    editExpenseViewModel.deleteExpense(expenseId)
                }
            ) {
                Text("Delete")
            }
        },

        dismissButton = {
            TextButton(
                onClick = { navController.popBackStack() }
            ) {
                Text("Cancel")
            }
        },
        title = { Text("Delete category") },
        text = { Text("Are you sure you want to delete this category?") }
    )
}
