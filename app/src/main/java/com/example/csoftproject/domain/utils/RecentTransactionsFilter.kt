package com.example.csoftproject.domain.utils

import com.example.csoftproject.domain.models.Expense

fun getMostRecentTransactions(data: List<Expense>): List<Expense>{
    return data
        .sortedByDescending { it.transactionDate }
        .take(5)
}
