    package com.example.csoftproject.ui.category

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.csoftproject.viewModel.EditCategoryViewModel

@Composable
fun DeleteCategoryDialog(
    categoryId: Int,
    navController: NavController,
    editCategoryViewModel: EditCategoryViewModel
) {
    AlertDialog(
        onDismissRequest = {  },
        confirmButton = {
            TextButton(
                onClick = {
                    editCategoryViewModel.deleteCategory(categoryId)
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

