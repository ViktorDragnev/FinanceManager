package com.example.csoftproject.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csoftproject.navigation.BottomNavItem
import com.example.csoftproject.ui.components.BackButton
import com.example.csoftproject.ui.components.CATEGORY_ICONS
import com.example.csoftproject.ui.components.ColorPicker
import com.example.csoftproject.ui.components.ErrorDialog
import com.example.csoftproject.viewModel.EditCategoryViewModel

@Composable
fun EditCategoryScreen(
    categoryId: Int,
    navController: NavController,
    editCategoryViewModel: EditCategoryViewModel
) {

    val categories by editCategoryViewModel.categories.collectAsState()
    val name by editCategoryViewModel.name.collectAsState()
    val validationResult by editCategoryViewModel.validationResult.collectAsState()
    val context = LocalContext.current
    val errorOccurred by editCategoryViewModel.errorOccurred.collectAsState()

    val category = categories.find { it.id == categoryId }
    val openAlertDialog = remember { mutableStateOf(false) }

    if (category == null) {
        navController.navigate(BottomNavItem.Catalog.route)
        return
    }

    var state by remember(category.id) {
        editCategoryViewModel.setName(category.name)
        editCategoryViewModel.validateName(category.name)
        mutableStateOf(category)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.CenterStart
        ) {

            BackButton(
                navController = navController,
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.CenterStart)
            )

            Text(
                text = "Edit Category",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        state = state.copy(name = it)
                        editCategoryViewModel.setName(it)
                        editCategoryViewModel.validateName(it)
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
                Text("Choose Icon", style = MaterialTheme.typography.titleMedium)
            }

            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .height(200.dp)
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

            item {
                Spacer(Modifier.height(16.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            openAlertDialog.value = true
                        },
                        modifier = Modifier.padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete")
                    }

                    Button(
                        onClick = {
                            editCategoryViewModel.updateCategory(
                                category = category.copy(
                                    id = state.id,
                                    name = state.name,
                                    icon = state.icon,
                                    color = state.color,
                                ),
                                context = context,
                                onSuccess = {
                                    navController.popBackStack()
                                }
                            )
                        },
                        enabled = validationResult.isValid,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }

    when {
        openAlertDialog.value -> {
            DeleteCategoryDialog(
                categoryId = categoryId,
                navController = navController,
                editCategoryViewModel = editCategoryViewModel
            )
        }
    }

    if (errorOccurred) {
        ErrorDialog(
            onRetry = {
                editCategoryViewModel.updateCategory(
                    category = category.copy(
                        id = state.id,
                        name = state.name,
                        icon = state.icon,
                        color = state.color,
                    ),
                    context,
                    navController::popBackStack
                )
            }
        )
        return
    }
}

@Composable
fun IconOption(
    icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(64.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                else MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}