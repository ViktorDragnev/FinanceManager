package com.example.csoftproject.ui.statistics

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.csoftproject.R
import com.example.csoftproject.domain.models.Category
import com.example.csoftproject.domain.models.Expense
import com.example.csoftproject.domain.utils.averageDailySpendingForPeriod
import com.example.csoftproject.domain.utils.calculateCategoryPercentage
import com.example.csoftproject.domain.utils.getSingleHighestSpending
import com.example.csoftproject.domain.utils.getTotalForExpenseList
import com.example.csoftproject.domain.utils.getTotalForTheMonth
import com.example.csoftproject.ui.components.ErrorScreen
import com.example.csoftproject.ui.components.LoadingBar
import com.example.csoftproject.ui.components.TimeFrameFilterDropDown
import com.example.csoftproject.ui.dashboard.EmptyListView
import com.example.csoftproject.ui.state.ExpenseUiState
import com.example.csoftproject.ui.theme.ExtraLargePadding
import com.example.csoftproject.ui.theme.LargePadding
import com.example.csoftproject.ui.theme.TextSizeTitle
import com.example.csoftproject.viewModel.StatisticsViewModel
import com.example.csoftproject.viewModel.vm_util_interfaces.FiltrationByTimeFrame
import java.time.LocalDate

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    statisticsViewModel: StatisticsViewModel
) {
    val expenses = statisticsViewModel.expenses.collectAsState()
    val categories = statisticsViewModel.categories.collectAsState()
    val uiState by statisticsViewModel.uiState.collectAsState()
    val filteredExpenses by statisticsViewModel.filteredExpenses.collectAsState()
    val customDateRange by statisticsViewModel.customDateRange.collectAsState()

    val (start, end) = customDateRange
    val safeStart = start ?: LocalDate.now()
    val safeEnd = end ?: LocalDate.now()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(ExtraLargePadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Statistics Overview",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = ExtraLargePadding)
            )
        }

        when (uiState) {
            is ExpenseUiState.Loading -> LoadingBar()

            is ExpenseUiState.EmptyList -> EmptyListView()

            is ExpenseUiState.Error -> {
                ErrorScreen(
                    onRetry = { statisticsViewModel.loadStatistics() }
                )
            }

            is ExpenseUiState.Success -> {
                StatisticsSection(
                    expenses = expenses.value,
                    categories = categories.value,
                    filteredExpenses = filteredExpenses,
                    start = safeStart,
                    end = safeEnd,
                    filtrationByTimeFrame = statisticsViewModel,
                )
            }
        }
    }
}

@Composable
fun StatisticsSection(
    expenses: List<Expense>,
    categories: List<Category>,
    filteredExpenses: List<Expense>,
    start: LocalDate,
    end: LocalDate,
    filtrationByTimeFrame: FiltrationByTimeFrame
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(LargePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        item {
            TimeFrameFilterDropDown(
                modifier = Modifier.fillMaxWidth(),
                filtrationByTimeFrame = filtrationByTimeFrame
            )
        }

        item {
            StatisticCard(
                amount = getSingleHighestSpending(expenses),
                title = stringResource(R.string.single_highest_spending)
            )
        }

        item {
            StatisticCard(
                amount = getTotalForTheMonth(expenses),
                title = stringResource(R.string.total_spent)
            )
        }

        if(start != LocalDate.now() || end != LocalDate.now()) {
            item {
                StatisticCard(
                    amount = getTotalForExpenseList(filteredExpenses),
                    title = stringResource(R.string.total_spending_period) +
                            "$start : $end"
                )
            }

            item {
            StatisticCard(
                    amount = averageDailySpendingForPeriod(
                        filteredExpenses,
                        start,
                        end
                    ),
                    title = stringResource(R.string.average_daily_spending_dates) +
                            "$start : $end"
                )
            }
        }

        item {
            val percentages = calculateCategoryPercentage(filteredExpenses)
            val categories = categories

            Column(
                verticalArrangement = Arrangement.spacedBy(ExtraLargePadding),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Category Breakdown",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = LargePadding)
                )

                percentages.forEach { (categoryId, percent) ->
                    val category = categories.firstOrNull { it.id == categoryId }
                    if (category != null) {
                        CategoryCard(
                            category = category,
                            percentage = percent
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatisticCard(amount: Double, title: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.statistics_card_format, String.format("%.2f", amount)),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = TextSizeTitle,
                modifier = Modifier.padding(top = LargePadding)
            )
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    percentage: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(
            containerColor = category.color.copy(alpha = 0.15f)
        )
    ) {
        Row(
            modifier = Modifier.padding(ExtraLargePadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(category.icon),
                contentDescription = category.name,
                tint = category.color,
                modifier = Modifier.size(32.dp)
            )

            LinearProgressIndicator(
                progress = { (percentage / 100).toFloat() },
                modifier = Modifier.weight(1f),
                color = category.color,
                drawStopIndicator = {}
            )

            Text(
                text = "${String.format("%.1f", percentage)}%",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
