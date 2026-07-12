package com.example.csoftproject.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csoftproject.ui.components.ErrorScreen
import com.example.csoftproject.viewModel.DashboardViewModel
import com.example.csoftproject.ui.state.ExpenseUiState

@Composable
fun DashboardScreen(dashboardViewModel: DashboardViewModel, modifier: Modifier = Modifier, navController: NavController) {

    val state by dashboardViewModel.uiState.collectAsState()

    Column() {
        Box(
            modifier = modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "Dashboard",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            when (state) {
                is ExpenseUiState.Loading -> item { LoadingBar() }
                is ExpenseUiState.Error -> item { ErrorScreen(
                    onRetry = { dashboardViewModel.loadDashboardData() }
                ) }
                is ExpenseUiState.EmptyList -> item { EmptyListView() }
                is ExpenseUiState.Success -> item {
                    DashboardComponentsColumn(
                        modifier,
                        navController,
                        dashboardViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardComponentsColumn(
    modifier: Modifier,
    navController: NavController,
    dashboardViewModel: DashboardViewModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SectionCard {
            TotalSection(dashboardViewModel = dashboardViewModel)
        }

        SectionCard {
            TransactionSection(navController = navController, dashboardViewModel = dashboardViewModel)
        }

        SectionCard {
            CategorySection(dashboardViewModel = dashboardViewModel)
        }
    }
}

@Composable
fun SectionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun EmptyListView() {
    Text(
        text = "No records found"
    )
}

@Composable
fun LoadingBar(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
    }
}
