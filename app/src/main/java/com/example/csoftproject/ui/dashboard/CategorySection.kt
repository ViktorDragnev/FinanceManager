package com.example.csoftproject.ui.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.csoftproject.R
import com.example.csoftproject.domain.utils.calculateCategoryPercentage
import com.example.csoftproject.ui.theme.BorderSmall
import com.example.csoftproject.ui.theme.ExtraLargePadding
import com.example.csoftproject.ui.theme.IconSizeSmall
import com.example.csoftproject.ui.theme.LargePadding
import com.example.csoftproject.ui.theme.SpaceMedium
import com.example.csoftproject.ui.theme.TextSizeTitle
import com.example.csoftproject.viewModel.DashboardViewModel

@Composable
fun CategorySection(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel
) {
    val expenses by dashboardViewModel.expenses.collectAsState()

    val data = calculateCategoryPercentage(expenses)

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
            data = data
        )
    }
}

@Composable
fun CategoriesList(
    dashboardViewModel: DashboardViewModel,
    modifier: Modifier = Modifier,
    data: Map<Int, Double>
) {
    data.forEach { (categoryId, percentage) ->
        CategoryCard(
            dashboardViewModel = dashboardViewModel,
            categoryId = categoryId,
            percentage = percentage
        )
    }
}

@Composable
fun CategoryCard(
    dashboardViewModel: DashboardViewModel,
    categoryId: Int,
    percentage: Double
) {

    val categories by dashboardViewModel.categories.collectAsState()
    val category = categories.find { it.id == categoryId }

    val color = category?.color ?: Color.Gray.copy(alpha = 0.15f)
    val icon = category?.icon ?: R.drawable.question_sign
    val name = category?.name ?: "Unknown"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(LargePadding)
            .border(
                width = BorderSmall,
                color = MaterialTheme.colorScheme.tertiary,
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(
            containerColor = category?.color?.copy(alpha = 0.15f) ?: Color.Unspecified
        )
    ) {
        Row(
            modifier = Modifier.padding(ExtraLargePadding),
            horizontalArrangement = Arrangement.spacedBy(SpaceMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(category?.icon ?: R.drawable.question_sign),
                contentDescription = category?.name,
                tint = category?.color ?: Color.Unspecified,
                modifier = Modifier.size(IconSizeSmall)
            )

            ProgressBar(
                percentage = percentage,
                color = category?.color ?: Color.Unspecified
            )

            Text(
                text = stringResource(R.string.percentage_format, percentage),
                style = MaterialTheme.typography.titleMedium
            )
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


