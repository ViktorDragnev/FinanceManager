package com.example.csoftproject.ui.category

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csoftproject.R
import com.example.csoftproject.domain.models.Category
import com.example.csoftproject.ui.components.ButtonToNavigateToAddForms
import com.example.csoftproject.ui.components.ErrorScreen
import com.example.csoftproject.ui.components.LoadingBar
import com.example.csoftproject.ui.state.CategoryUiState
import com.example.csoftproject.ui.theme.BorderMedium
import com.example.csoftproject.ui.theme.ExtraLargePadding
import com.example.csoftproject.ui.theme.IconSizeMedium
import com.example.csoftproject.ui.theme.MediumCardSize
import com.example.csoftproject.viewModel.CategoryViewModel

@Composable
fun CategoryListScreen(
    navController: NavController,
    categoryViewModel: CategoryViewModel
) {
    val uiState by categoryViewModel.uiState.collectAsState()
    val categories by categoryViewModel.categories.collectAsState()
    val expenses by categoryViewModel.expenses.collectAsState()
    val isRefreshing by categoryViewModel.isRefreshing.collectAsState()
    val errorOccurred by categoryViewModel.errorMessage.collectAsState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { categoryViewModel.loadCategories() },
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .padding(top = ExtraLargePadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Category Section",
                Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Box(Modifier.fillMaxSize()) {
                when (uiState) {
                    is CategoryUiState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                        LoadingBar()
                        }
                    }

                    is CategoryUiState.Success -> {
                        CategoryVerticalGrid(categories, expenses, navController)
                    }

                    is CategoryUiState.Error -> {
                        if (errorOccurred) {
                            ErrorScreen (
                                onRetry = { categoryViewModel.loadCategories() }
                            )
                        }
                    }

                    is CategoryUiState.EmptyList -> {
                        EmptyCategoryListState()
                    }
                }
                ButtonToNavigateToAddForms(navController, "addCategory")
            }
        }
    }
}

@Composable
fun EmptyCategoryListState() {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ){
        Text(
            text = stringResource(R.string.empty_category_state),
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun CategoryVerticalGrid(categories: List<Category>, expenses: List<com.example.csoftproject.domain.models.Expense>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(ExtraLargePadding)
    ) {
        items(categories) { category ->
            val spentAmount = expenses.filter { it.category.id == category.id }.sumOf { it.transactionValue }
            CategoryCard(
                category = category,
                spentAmount = spentAmount,
                modifier = Modifier.clickable {
                    navController.navigate("editCategory/${category.id}")
                }
            )
        }
    }
}

@Composable
fun CategoryCard(modifier: Modifier = Modifier, category: Category, spentAmount: Double) {
    val budgetLimit = category.budgetLimit
    val isOverBudget = budgetLimit != null && spentAmount > budgetLimit
    val isNearBudget = budgetLimit != null && spentAmount > (budgetLimit * 0.8)

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(ExtraLargePadding)
            .size(MediumCardSize)
            .border(
                width = BorderMedium,
                color = if (isOverBudget) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimaryContainer,
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(
            containerColor = category.color
        ),

    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Icon(
                painter = painterResource(id = category.icon),
                contentDescription = "",
                Modifier.size(IconSizeMedium)
            )
            
            if (budgetLimit != null) {
                Spacer(modifier = Modifier.height(4.dp))
                val progress = (spentAmount / budgetLimit).toFloat()
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = if (isOverBudget) Color.Red else if (isNearBudget) Color.Yellow else Color.Green,
                    drawStopIndicator = {}
                )
                Text(
                    text = "${spentAmount.toInt()}/${budgetLimit.toInt()}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
