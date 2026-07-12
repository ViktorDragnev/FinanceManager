package com.example.csoftproject.domain.validation.expense_validators.utils_validators

import com.example.csoftproject.domain.enums.PaymentMethod

object PaymentMethodValidator {

    fun validate(method: PaymentMethod?): String? {
        return if (method == null)
            "Payment method is required"
        else
            null
    }
}