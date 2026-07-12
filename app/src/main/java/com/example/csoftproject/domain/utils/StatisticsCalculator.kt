package com.example.csoftproject.domain.utils

import com.example.csoftproject.domain.models.Expense
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun getTotalForExpenseList(expenseList: List<Expense>): Double{

    var result = 0.0

    for(data in expenseList){
        result += data.transactionValue
    }

    return result
}

fun averageDailySpendingForPeriod(
    expenseList: List<Expense>,
    startDate: LocalDate,
    endDate: LocalDate
): Double {

    if (expenseList.isEmpty()) return 0.0

    val total = expenseList.sumOf { it.transactionValue }

    val days = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1

    return total / days
}

fun getSingleHighestSpending(expenseList: List<Expense>): Double{
    val dataList= getDataForCurrentMonth(expenseList)
    var biggestValue = Double.MIN_VALUE

    for(data in dataList){
        if(data.transactionValue > biggestValue){
            biggestValue = data.transactionValue
        }
    }

    return biggestValue
}

