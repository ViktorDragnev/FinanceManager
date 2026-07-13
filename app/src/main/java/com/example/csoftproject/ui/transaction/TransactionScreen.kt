package com.example.csoftproject.ui.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csoftproject.R
import com.example.csoftproject.domain.enums.SortingExpenseOptions
import com.example.csoftproject.domain.models.Expense
import com.example.csoftproject.ui.components.ButtonToNavigateToAddForms
import com.example.csoftproject.ui.components.ErrorScreen
import com.example.csoftproject.ui.components.LoadingBar
import com.example.csoftproject.ui.components.TimeFrameFilterDropDown
import com.example.csoftproject.ui.state.ExpenseUiState
import com.example.csoftproject.ui.theme.ElevationLarge
import com.example.csoftproject.ui.theme.ExtraLargePadding
import com.example.csoftproject.ui.theme.IconSizeSmall
import com.example.csoftproject.ui.theme.LargePadding
import com.example.csoftproject.viewModel.TransactionsViewModel

@Composable
fun TransactionScreen(
    transactionsViewModel: TransactionsViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val uiState by transactionsViewModel.uiState.collectAsState()
    val isRefreshing by transactionsViewModel.isRefreshing.collectAsState()
    val filteredExpenses by transactionsViewModel.filteredExpenses.collectAsState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { transactionsViewModel.loadTransactionHistory() },
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Transaction Section",
                Modifier.padding(ExtraLargePadding),
                style = MaterialTheme.typography.headlineSmall
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ExtraLargePadding, vertical = LargePadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SortingDropDown(
                    transactionsViewModel = transactionsViewModel
                )

                FilterDropDown(
                    transactionsViewModel = transactionsViewModel
                )

                TimeFrameFilterDropDown(
                    modifier = Modifier.width(90.dp),
                    filtrationByTimeFrame = transactionsViewModel
                )
            }

            when (uiState) {

                is ExpenseUiState.Loading -> {
                    LoadingBar()
                }

                is ExpenseUiState.Success -> {
                    ExpenseList(
                        expense = filteredExpenses,
                        onClick = { id -> navController.navigate("transaction/$id") },
                        transactionsViewModel = transactionsViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is ExpenseUiState.Error -> {
                    ErrorScreen(
                        onRetry = { transactionsViewModel.loadTransactionHistory() }
                    )
                }

                is ExpenseUiState.EmptyList -> { EmptyTransactionsList() }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        ButtonToNavigateToAddForms(navController,"addExpense")
    }
}

@Composable
fun EmptyTransactionsList() {
    Text(
        text = "No transactions found",
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
fun RowScope.FilterDropDown(
    transactionsViewModel: TransactionsViewModel
) {
    val categories = transactionsViewModel.categories.collectAsState().value
    var filterExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(LargePadding)
            .weight(1f)
            .clickable { filterExpanded = true },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.filter),
            contentDescription = "Filter",
            modifier = Modifier.size(IconSizeSmall)
        )

        DropdownMenu(
            expanded = filterExpanded,
            onDismissRequest = { filterExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("All") },
                onClick = {
                    transactionsViewModel.changeFilterByCategory(null)
                    filterExpanded = false
                }
            )

            categories.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = {
                        transactionsViewModel.changeFilterByCategory(item.id)
                        filterExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun RowScope.SortingDropDown(
    transactionsViewModel: TransactionsViewModel
) {

    var sortExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(LargePadding)
            .weight(1f)
            .clickable { sortExpanded = true },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.sort),
            contentDescription = "Filter",
            modifier = Modifier.size(IconSizeSmall)
        )

        DropdownMenu(
            expanded = sortExpanded,
            onDismissRequest = { sortExpanded = false }
        ) {

            DropdownMenuItem(
                text = { Text("AMOUNT DES") },
                onClick = {
                    transactionsViewModel.changeSortType(SortingExpenseOptions.AMOUNT_DESCENDING)
                    sortExpanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("AMOUNT ASC") },
                onClick = {
                    transactionsViewModel.changeSortType(SortingExpenseOptions.AMOUNT_ASCENDING)
                    sortExpanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("DATE DES") },
                onClick = {
                    transactionsViewModel.changeSortType(SortingExpenseOptions.DATE_DESCENDING)
                    sortExpanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("DATE ASC") },
                onClick = {
                    transactionsViewModel.changeSortType(SortingExpenseOptions.DATE_ASCENDING)
                    sortExpanded = false
                }
            )
        }
    }
}

@Composable
fun ColumnScope.ExpenseList(
    expense: List<Expense>,
    transactionsViewModel: TransactionsViewModel,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    val grouped = expense.groupBy { it.transactionDate }

    LazyColumn(
        modifier = modifier
            .weight(1f)
    ) {
        grouped.forEach { (date, items) ->

            item {
                Text(
                    text = date.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(LargePadding)
                )
            }

            items(
                items,
                key = { it.id }
            ) { expense ->
                ExpenseRow(expense, onClick, transactionsViewModel)
            }
        }
    }
}

@Composable
fun ExpenseRow(
    expense: Expense,
    onClick: (Int) -> Unit,
    transactionsViewModel: TransactionsViewModel,
) {
    val category = transactionsViewModel.findCategoryById(expense.category.id)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LargePadding, vertical = LargePadding)
            .clickable { onClick(expense.id) },
        elevation = CardDefaults.cardElevation(ElevationLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryFixedDim
        )
    ) {
        Row(
            modifier = Modifier
                .padding(ExtraLargePadding)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ExtraLargePadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(expense.transactionDescription)
                Text("€${expense.transactionValue}")
                Text(category?.name ?: "Unknown Category")
            }

            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "magnifying glass",
                modifier = Modifier.size(IconSizeSmall)
            )
        }
    }
}
