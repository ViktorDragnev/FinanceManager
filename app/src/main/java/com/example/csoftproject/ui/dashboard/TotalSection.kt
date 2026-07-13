package com.example.csoftproject.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.csoftproject.R
import com.example.csoftproject.domain.utils.getTotalForTheMonth
import com.example.csoftproject.domain.utils.getTotalValue
import com.example.csoftproject.ui.theme.ElevationLarge
import com.example.csoftproject.ui.theme.ExtraLargePadding
import com.example.csoftproject.ui.theme.LargePadding
import com.example.csoftproject.ui.theme.MediumCardSize
import com.example.csoftproject.ui.theme.TextSizeTitle
import com.example.csoftproject.viewModel.DashboardViewModel

@Composable
fun TotalSection(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel
){
    Column(
        modifier = Modifier
            .padding(LargePadding)
            .fillMaxWidth()
    ) {
        Text(
            text = "Total Statistics",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(ExtraLargePadding)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            fontSize = TextSizeTitle
        )

        TotalStatisticsComp(dashboardViewModel = dashboardViewModel)
    }
}

@Composable
fun TotalStatisticsComp(modifier: Modifier = Modifier, dashboardViewModel: DashboardViewModel) {

    val expenses by dashboardViewModel.expenses.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SummaryCard(getTotalForTheMonth(expenses), "This Month Total")

        SummaryCard(getTotalValue(expenses), "Biggest Transaction")
    }
}

@Composable
fun RowScope.SummaryCard(value: Double, title: String) {

    Card(
        modifier = Modifier
            .padding(LargePadding)
            .padding(bottom = LargePadding)
            .size(MediumCardSize)
            .weight(1f)
            .height(MediumCardSize),
        elevation = CardDefaults.cardElevation(ElevationLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryFixedDim
        )
    ) {
        Column(
            modifier = Modifier
                .padding(ExtraLargePadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.value_format, value),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = TextSizeTitle
            )
        }
    }
}