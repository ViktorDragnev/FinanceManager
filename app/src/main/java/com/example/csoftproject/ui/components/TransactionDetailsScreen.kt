package com.example.csoftproject.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csoftproject.R
import com.example.csoftproject.domain.models.Expense
import com.example.csoftproject.viewModel.DashboardViewModel

@Composable
fun TransactionDetailsScreen(
    expenseId: Int?,
    navController: NavController,
    dashboardViewModel: DashboardViewModel
) {
    val expenses by dashboardViewModel.expenses.collectAsState()
    val expense = expenses.firstOrNull { it.id == expenseId }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackButton(
                navController = navController,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = {
                    navController.navigate("editExpense/${expense?.id}")
                }
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(R.drawable.draw),
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp)
                        .fillMaxSize()
                )
            }
        }

        TransactionDetails(
            expense = expense,
            dashboardViewModel = dashboardViewModel,
            navController = navController
        )
    }
}

@Composable
fun TransactionDetails(
    expense: Expense?,
    dashboardViewModel: DashboardViewModel,
    navController: NavController
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TransactionDetailsCard(
            expense = expense,
            dashboardViewModel = dashboardViewModel,
        )
    }
}

@Composable
fun TransactionDetailsCard(
    expense: Expense?,
    dashboardViewModel: DashboardViewModel,
) {
    val category = expense?.let {
        dashboardViewModel.findCategoryById(it.category.id)
    }

    if(category == null) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transaction Details",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "€${expense.transactionValue}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            DetailRow(label = "Category", value = category.name)
            DetailRow(label = "Description", value = expense.transactionDescription)
            DetailRow(label = "Date", value = expense.transactionDate.toString())
            DetailRow(label = "Payment Method", value = expense.paymentMethod.name)
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

