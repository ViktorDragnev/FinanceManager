package com.example.csoftproject.ui.transaction

import com.example.csoftproject.domain.enums.PaymentMethod
import java.time.LocalDate

data class ExpenseFormState(
    val id: Int? = null,
    val value: String = "",
    val category: Int? = null,
    val description: String = "",
    val paymentMethod: PaymentMethod? = null,
    val updateCount: Int? = null,
    val date: LocalDate? = null,
    val isCategoryExpanded: Boolean = false,
    val isPaymentExpanded: Boolean = false,

    val valueError: String? = null,
    val categoryError: String? = null,
    val dateError: String? = null,
    val paymentMethodError: String? = null,
    val descriptionError: String? = null
)