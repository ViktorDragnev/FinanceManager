package com.example.csoftproject.ui.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csoftproject.domain.models.dtos.AddCategoryDto
import com.example.csoftproject.ui.components.BackButton
import com.example.csoftproject.ui.components.CATEGORY_ICONS
import com.example.csoftproject.ui.components.ColorPicker
import com.example.csoftproject.ui.components.ErrorDialog
import com.example.csoftproject.ui.theme.ExtraLargePadding
import com.example.csoftproject.ui.theme.LargePadding
import com.example.csoftproject.ui.theme.LazyGridSize
import com.example.csoftproject.viewModel.AddCategoryViewModel

@Composable
fun AddCategoryScreen(
    navController: NavController,
    addCategoryViewModel: AddCategoryViewModel
) {
    val name by addCategoryViewModel.name.collectAsState()
    val validationResult by addCategoryViewModel.validationResult.collectAsState()
    val context = LocalContext.current
    val errorOccurred by addCategoryViewModel.errorOccurred.collectAsState()

    var state by remember { mutableStateOf(CategoryStateForm(
        name = "",
        icon = CATEGORY_ICONS.first(),
    )) }

    val addCategoryDto = AddCategoryDto(
        name = state.name,
        icon = state.icon,
        color = state.color,
        budgetLimit = state.budgetLimit.toDoubleOrNull()
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LargePadding),
            contentAlignment = Alignment.CenterStart
        ) {

            BackButton(
                navController = navController
            )

            Text(
                text = "Add Category",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(ExtraLargePadding)
                    .align(Alignment.Center)
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(ExtraLargePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ExtraLargePadding)
        ) {
            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { newName ->
                        addCategoryViewModel.setName(newName)
                        addCategoryViewModel.validateName(newName)
                        state = state.copy(name = newName)
                    },
                    label = { Text("Category name") },
                    isError = validationResult.nameError != null,
                    supportingText = {
                        validationResult.nameError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = state.budgetLimit,
                    onValueChange = { state = state.copy(budgetLimit = it) },
                    label = { Text("Budget Limit (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text("Choose Icon", style = MaterialTheme.typography.titleMedium)
            }

            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .height(LazyGridSize)
                        .fillMaxWidth()
                ) {
                    items(CATEGORY_ICONS) { iconRes ->
                        IconOption(
                            icon = iconRes,
                            isSelected = state.icon == iconRes,
                            onClick = { state = state.copy(icon = iconRes) }
                        )
                    }
                }
            }

            item {
                Text("Choose Color", style = MaterialTheme.typography.titleMedium)
            }

            item {
                ColorPicker(
                    selectedColor = state.color,
                    onColorSelected = { color ->
                        state = state.copy(color = color)
                    }
                )
            }

            item{
                Button(
                    onClick = {
                        addCategoryViewModel.addCategory(addCategoryDto, context, navController::popBackStack)
                    },
                    enabled = validationResult.isValid,
                    modifier = Modifier.padding(ExtraLargePadding)
                ) {
                    Text("Save")
                }
            }
        }
    }

    if (errorOccurred) {
        ErrorDialog(
            onRetry = {
                addCategoryViewModel.addCategory(addCategoryDto, context, navController::popBackStack)
            }
        )
        return
    }
}