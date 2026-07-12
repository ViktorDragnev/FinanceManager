package com.example.csoftproject.domain.models

import com.example.csoftproject.domain.enums.PaymentMethod
import java.time.LocalDate

data class Expense(
    val id: Int,
    val transactionValue: Double,
    val category: Category,
    val transactionDate: LocalDate,
    val transactionDescription: String,
    val paymentMethod: PaymentMethod,
    val updateCount: Int
)