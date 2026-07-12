package com.example.csoftproject.domain.utils

import com.example.csoftproject.domain.enums.FilterForTimeFrame
import com.example.csoftproject.domain.models.Expense
import java.time.LocalDate
import kotlin.collections.filter

fun filterExpensesByTimeFrame(
    filterForTimeFrameOptionsFilter: FilterForTimeFrame,
    list: List<Expense>,
    fistDate: LocalDate?,
    lastDate: LocalDate?
): List<Expense>{

    val currentDate = LocalDate.now()
    val startOfWeek = currentDate.minusDays(currentDate.dayOfWeek.value.toLong() - 1)
    val endOfWeek = startOfWeek.plusDays(6)

    return when (filterForTimeFrameOptionsFilter) {
        FilterForTimeFrame.TODAY -> list.filter { it.transactionDate == currentDate }

        FilterForTimeFrame.THIS_WEEK -> list.filter { expense ->
            expense.transactionDate in startOfWeek..endOfWeek
        }

        FilterForTimeFrame.THIS_MONTH -> list.filter { currentDate.monthValue == it.transactionDate.monthValue }

        FilterForTimeFrame.THIS_YEAR -> list.filter { currentDate.year == it.transactionDate.year }

        FilterForTimeFrame.CUSTOM -> {
            if (fistDate != null && lastDate != null)
                list.filter { it.transactionDate in fistDate..lastDate }
            else list
        }
    }
}