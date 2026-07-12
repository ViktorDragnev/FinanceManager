package com.example.csoftproject.ui.category

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csoftproject.domain.models.Category
import com.example.csoftproject.ui.components.ButtonToNavigateToAddForms
import com.example.csoftproject.ui.components.ErrorDialog
import com.example.csoftproject.ui.components.ErrorScreen
import com.example.csoftproject.ui.dashboard.LoadingBar
import com.example.csoftproject.ui.state.CategoryUiState
import com.example.csoftproject.viewModel.CategoryViewModel

@Composable
fun CategoryListScreen(
    navController: NavController,
    categoryViewModel: CategoryViewModel
) {
    val uiState by categoryViewModel.uiState.collectAsState()
    val categories by categoryViewModel.categories.collectAsState()
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
                .padding(top = 16.dp),
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
                        CategoryVerticalGrid(categories, navController)
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
            text = "No categories found",
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun CategoryVerticalGrid(categories: List<Category>, navController: NavController, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(16.dp)
    ) {
        items(categories) { category ->
            CategoryCard(
                category = category,
                modifier = Modifier.clickable {
                    navController.navigate("editCategory/${category.id}")
                }
            )
        }
    }
}

@Composable
fun CategoryCard(modifier: Modifier = Modifier, category: Category) {

    Card(
        modifier
            .fillMaxSize()
            .padding(16.dp)
            .size(100.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(
            containerColor = category.color
        ),

    ) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        Icon(
            painter = painterResource(id = category.icon),
            contentDescription = "",
            Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}
