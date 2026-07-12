package com.example.csoftproject.domain.validation.expense_validators.utils_validators

object AmountValidator {

    fun validate(value: String): String? {
        return when {
            value.isBlank() -> "Amount is required"
            value.toDoubleOrNull() == null -> "Amount must be a valid number"
            value.toDouble() <= 0 -> "Amount must be positive"
            else -> null
        }
    }
}