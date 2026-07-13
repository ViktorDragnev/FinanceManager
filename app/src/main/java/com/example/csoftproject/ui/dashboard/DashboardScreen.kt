package com.example.csoftproject.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.csoftproject.ui.components.ErrorScreen
import com.example.csoftproject.ui.components.LoadingBar
import com.example.csoftproject.ui.state.ExpenseUiState
import com.example.csoftproject.ui.theme.ElevationLarge
import com.example.csoftproject.ui.theme.SpaceMedium
import com.example.csoftproject.ui.theme.SpaceLarge
import com.example.csoftproject.viewModel.DashboardViewModel

@Composable
fun DashboardScreen(dashboardViewModel: DashboardViewModel, modifier: Modifier = Modifier, navController: NavController) {

    val state by dashboardViewModel.uiState.collectAsState()

    Column {
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
                .padding(SpaceMedium),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpaceMedium)
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
        verticalArrangement = Arrangement.spacedBy(SpaceLarge),
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
        elevation = CardDefaults.cardElevation(ElevationLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(SpaceMedium)) {
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