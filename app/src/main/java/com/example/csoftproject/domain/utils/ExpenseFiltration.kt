package com.example.csoftproject.domain.utils

import com.example.csoftproject.domain.models.Expense

fun filterExpensesByCategory(list: List<Expense>, filter: Int?): List<Expense> {
    return filter?.let {
            id -> list.filter {
        it.category.id == id
    }
    } ?: list
}