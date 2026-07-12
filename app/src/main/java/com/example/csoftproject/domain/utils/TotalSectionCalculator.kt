package com.example.csoftproject.domain.utils

import com.example.csoftproject.domain.models.Expense
import java.time.LocalDate

fun getTotalForTheMonth(expenseList: List<Expense>): Double {
    val dataList= getDataForCurrentMonth(expenseList)

    var total = 0.0

    for (data in dataList) {
        total += data.transactionValue
    }

    return total
}

fun getDataForCurrentMonth(expenseList: List<Expense>): List<Expense>{

    val currentDate = LocalDate.now()
    val resultList = mutableListOf<Expense>()

    for(data in expenseList){
        if(data.transactionDate.month == currentDate.month){
            resultList.add(data)
        }
    }

    return resultList
}

fun getTotalValue(expenseList: List<Expense>): Double {

    val dataList= getDataForCurrentMonth(expenseList)
    var biggestValue = 0.0

    for (data in dataList) {
        if (data.transactionValue > biggestValue) {
            biggestValue = data.transactionValue
        }
    }

    return biggestValue
}