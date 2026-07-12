package com.example.csoftproject.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.csoftproject.R
import com.example.csoftproject.domain.enums.FilterForTimeFrame
import com.example.csoftproject.viewModel.vm_util_interfaces.FiltrationByTimeFrame

@Composable
fun TimeFrameFilterDropDown(
    modifier: Modifier = Modifier,
    filtrationByTimeFrame: FiltrationByTimeFrame
) {

    var filterExpanded by remember { mutableStateOf(false) }
    var showRangeModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable { filterExpanded = true },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Icon(
            painter = painterResource(R.drawable.filter_time),
            contentDescription = "Time Frame Filter",
            modifier = Modifier.size(30.dp)
        )

        DropdownMenu(
            expanded = filterExpanded,
            onDismissRequest = { filterExpanded = false }
        ) {

            DropdownMenuItem(
                text = { Text("TODAY") },
                onClick = {
                    filtrationByTimeFrame.changeFilterByTimeFrame(FilterForTimeFrame.TODAY)
                    filterExpanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("THIS WEEK") },
                onClick = {
                    filtrationByTimeFrame.changeFilterByTimeFrame(FilterForTimeFrame.THIS_WEEK)
                    filterExpanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("THIS MONTH") },
                onClick = {
                    filtrationByTimeFrame.changeFilterByTimeFrame(FilterForTimeFrame.THIS_MONTH)
                    filterExpanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("THIS YEAR") },
                onClick = {
                    filtrationByTimeFrame.changeFilterByTimeFrame(FilterForTimeFrame.THIS_YEAR)
                    filterExpanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("CUSTOM") },
                onClick = {
                    filtrationByTimeFrame.changeFilterByTimeFrame(FilterForTimeFrame.CUSTOM)
                    filterExpanded = false
                    showRangeModal = true
                }
            )
        }

        if (showRangeModal) {
            DateRangePickerModal(
                onDateRangeSelected = { (startMillis, endMillis) ->

                    val start = startMillis?.let {
                        java.time.Instant.ofEpochMilli(it)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                    }

                    val end = endMillis?.let {
                        java.time.Instant.ofEpochMilli(it)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                    }

                    filtrationByTimeFrame.setCustomDateRange(start, end)
                    showRangeModal = false
                },
                onDismiss = { showRangeModal = false }
            )
        }
    }
}

@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        dateRangePickerState.selectedStartDateMillis to
                                dateRangePickerState.selectedEndDateMillis
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = { Text("Select date range") },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}
