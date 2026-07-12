package com.example.csoftproject.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.csoftproject.R
import com.example.csoftproject.domain.models.Expense
import com.example.csoftproject.domain.utils.getMostRecentTransactions
import com.example.csoftproject.viewModel.DashboardViewModel

@Composable
fun TransactionSection(
    modifier: Modifier = Modifier,
    navController: NavController,
    dashboardViewModel: DashboardViewModel
) {
    RecentTransactions(modifier, navController, dashboardViewModel)
}

@Composable
fun RecentTransactions(modifier: Modifier = Modifier, navController: NavController, dashboardViewModel: DashboardViewModel) {
    Column() {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(),
            text = "Recent Transactions",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp
        )
        TransactionList(modifier, navController, dashboardViewModel)
    }
}

@Composable
fun TransactionList(modifier: Modifier = Modifier, navController: NavController, dashboardViewModel: DashboardViewModel) {

    val expenses = dashboardViewModel.expenses.collectAsState().value

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    ) {
        items(
            items = getMostRecentTransactions(expenses),
            key = { it.id }
        ) { expense ->
            TransactionCard(
                expense = expense,
                modifier = modifier.clickable{ navController.navigate("transaction/${expense.id}") }
            )
        }
    }
}

@Composable
fun TransactionCard(expense: Expense, modifier: Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryFixedDim
        )
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = expense.transactionDescription,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = expense.transactionDate.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "€${expense.transactionValue}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "magnifying glass",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
