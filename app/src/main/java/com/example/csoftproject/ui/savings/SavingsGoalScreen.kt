package com.example.csoftproject.ui.savings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csoftproject.R
import com.example.csoftproject.domain.models.SavingsGoal
import com.example.csoftproject.ui.components.BackButton
import com.example.csoftproject.ui.theme.ExtraLargePadding
import com.example.csoftproject.ui.theme.FloatingActionButtonSize
import com.example.csoftproject.viewModel.SavingsGoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalScreen(
    navController: NavController,
    viewModel: SavingsGoalViewModel
) {
    val goals by viewModel.goals.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showAddFundsDialog by remember { mutableStateOf<SavingsGoal?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Savings Goals") },
                navigationIcon = { BackButton(navController = navController) }
            )
        },


    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(goals) { goal ->
                GoalCard(
                    goal = goal,
                    onAddFunds = { showAddFundsDialog = goal },
                    onDelete = { viewModel.deleteGoal(goal) }
                )
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(navController.navigate("addGoal")) },
            modifier = Modifier
                .size(FloatingActionButtonSize)
                .padding(ExtraLargePadding),
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.surfaceBright
        ) {
            Icon(
                painter = painterResource(R.drawable.plus),
                contentDescription = null
            )
        }
    }

    if (showAddDialog) {
        AddGoalDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, target ->
                viewModel.addGoal(title, target)
                showAddDialog = false
            }
        )
    }

    if (showAddFundsDialog != null) {
        AddFundsDialog(
            goal = showAddFundsDialog!!,
            onDismiss = { showAddFundsDialog = null },
            onConfirm = { amount ->
                viewModel.addToGoal(showAddFundsDialog!!, amount)
                showAddFundsDialog = null
            }
        )
    }
}

@Composable
fun GoalCard(
    goal: SavingsGoal,
    onAddFunds: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = goal.title, style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(R.drawable.back),
                        contentDescription = "Delete"
                    )
                }
            }
            
            val progress = if (goal.targetAmount > 0) (goal.currentAmount / goal.targetAmount).toFloat() else 0f
            
            Text(
                text = "${goal.currentAmount.toInt()} / ${goal.targetAmount.toInt()} saved",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onAddFunds,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Funds")
            }
        }
    }
}

@Composable
fun AddGoalDialog(onDismiss: () -> Unit, onConfirm: (String, Double) -> Unit) {
    var title by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Savings Goal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = target, onValueChange = { target = it }, label = { Text("Target Amount") })
            }
        },
        confirmButton = {
            Button(onClick = { 
                val targetAmount = target.toDoubleOrNull() ?: 0.0
                if (title.isNotBlank() && targetAmount > 0) {
                    onConfirm(title, targetAmount)
                }
            }) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddFundsDialog(goal: SavingsGoal, onDismiss: () -> Unit, onConfirm: (Double) -> Unit) {
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Funds to ${goal.title}") },
        text = {
            OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") })
        },
        confirmButton = {
            Button(onClick = { 
                val fundAmount = amount.toDoubleOrNull() ?: 0.0
                if (fundAmount > 0) {
                    onConfirm(fundAmount)
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
