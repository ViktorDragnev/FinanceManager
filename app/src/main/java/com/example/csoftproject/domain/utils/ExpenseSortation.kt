package com.example.csoftproject.domain.utils

import com.example.csoftproject.domain.enums.SortingExpenseOptions
import com.example.csoftproject.domain.models.Expense

fun sortExpensesByValueOrDate(list: List<Expense>, sort: SortingExpenseOptions): List<Expense> {
    return when (sort) {
        SortingExpenseOptions.AMOUNT_DESCENDING -> list.sortedByDescending { it.transactionValue }
        SortingExpenseOptions.DATE_DESCENDING -> list.sortedByDescending { it.transactionDate }
        SortingExpenseOptions.AMOUNT_ASCENDING -> list.sortedBy { it.transactionValue }
        SortingExpenseOptions.DATE_ASCENDING -> list.sortedBy { it.transactionDate }
    }
}