package com.example.csoftproject.domain.models

data class SavingsGoal(
    val id: Int = 0,
    val title: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val updateCount: Int
)
