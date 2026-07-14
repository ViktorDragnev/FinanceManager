package com.example.csoftproject.domain.utils

import com.example.csoftproject.domain.models.Expense

fun calculateCategorySums(data: List<Expense>): Map<Int, Double> {
    return data
        .groupBy { it.category.id }
        .mapValues { (_, items) ->
            items.sumOf { it.transactionValue }
        }
}
