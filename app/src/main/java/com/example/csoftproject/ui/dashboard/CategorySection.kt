package com.example.csoftproject.ui.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.csoftproject.R
import com.example.csoftproject.domain.utils.calculateCategoryPercentage
import com.example.csoftproject.domain.utils.calculateCategorySums
import com.example.csoftproject.ui.theme.*
import com.example.csoftproject.viewModel.DashboardViewModel

@Composable
fun CategorySection(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel
) {
    val expenses by dashboardViewModel.expenses.collectAsState()

    val percentageData = calculateCategoryPercentage(expenses)
    val sumData = calculateCategorySums(expenses)

    Column(
        modifier = Modifier
            .padding(ExtraLargePadding),
        verticalArrangement = Arrangement.spacedBy(ExtraLargePadding)
    ) {
        Text(
            text = "Categories Overview",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(ExtraLargePadding)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            fontSize = TextSizeTitle
        )
        CategoriesList(
            dashboardViewModel = dashboardViewModel,
            percentageData = percentageData,
            sumData = sumData
        )
    }
}

@Composable
fun CategoriesList(
    dashboardViewModel: DashboardViewModel,
    modifier: Modifier = Modifier,
    percentageData: Map<Int, Double>,
    sumData: Map<Int, Double>
) {
    percentageData.forEach { (categoryId, percentage) ->
        CategoryCard(
            dashboardViewModel = dashboardViewModel,
            categoryId = categoryId,
            percentage = percentage,
            spentAmount = sumData[categoryId] ?: 0.0
        )
    }
}

@Composable
fun CategoryCard(
    dashboardViewModel: DashboardViewModel,
    categoryId: Int,
    percentage: Double,
    spentAmount: Double
) {

    val categories by dashboardViewModel.categories.collectAsState()
    val category = categories.find { it.id == categoryId }

    val color = category?.color ?: Color.Gray.copy(alpha = 0.15f)
    val icon = category?.icon ?: R.drawable.question_sign
    val name = category?.name ?: "Unknown"
    
    val budgetLimit = category?.budgetLimit
    val isOverBudget = budgetLimit != null && spentAmount > budgetLimit
    val isNearBudget = budgetLimit != null && spentAmount > (budgetLimit * 0.8)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(LargePadding)
            .border(
                width = BorderSmall,
                color = if (isOverBudget) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary,
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(
            containerColor = category?.color?.copy(alpha = 0.15f) ?: Color.Unspecified
        )
    ) {
        Column(modifier = Modifier.padding(ExtraLargePadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpaceMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = name,
                    tint = category?.color ?: Color.Unspecified,
                    modifier = Modifier.size(IconSizeSmall)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = name, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = stringResource(R.string.percentage_format, percentage),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    
                    Row(modifier = Modifier.fillMaxWidth()) {
                        ProgressBar(
                            percentage = percentage,
                            color = category?.color ?: Color.Unspecified
                        )
                    }
                }
            }
            
            if (budgetLimit != null) {
                Spacer(modifier = Modifier.height(8.dp))
                val budgetProgress = (spentAmount / budgetLimit).toFloat()
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Budget: ${spentAmount.toInt()} / ${budgetLimit.toInt()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isOverBudget) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (isOverBudget) {
                        Text(
                            text = "Limit Exceeded!",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    } else if (isNearBudget) {
                        Text(
                            text = "Near Limit (80%)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFA000), // Amber
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                LinearProgressIndicator(
                    progress = { budgetProgress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = if (isOverBudget) MaterialTheme.colorScheme.error else if (isNearBudget) Color(0xFFFFA000) else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Composable
fun RowScope.ProgressBar(modifier: Modifier = Modifier, percentage: Double, color: Color) {

    LinearProgressIndicator(
        progress = { (percentage / 100).toFloat() },
        modifier = Modifier.weight(1f),
        color = color,
        drawStopIndicator = {}
    )
}


