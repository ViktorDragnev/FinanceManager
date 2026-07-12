package com.example.csoftproject.domain.models.dtos

import com.example.csoftproject.domain.enums.PaymentMethod
import java.time.LocalDate

data class AddExpenseDto(
    val value: Double,
    val categoryId: Int,
    val date: LocalDate,
    val description: String,
    val paymentMethod: PaymentMethod
)