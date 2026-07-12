package com.example.csoftproject.domain.utils

import com.example.csoftproject.domain.models.Expense

fun calculateCategoryPercentage(data: List<Expense>): Map<Int, Double> {
    val totalSum = data.sumOf { it.transactionValue }
    if (totalSum == 0.0) return emptyMap()

    return data
        .groupBy { it.category.id }
        .mapValues { (_, items) ->
            (items.sumOf { it.transactionValue } / totalSum) * 100
        }
}