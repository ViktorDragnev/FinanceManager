package com.example.csoftproject.domain.validation.expense_validators.utils_validators

import java.time.LocalDate

object DateValidator {

    fun validate(date: LocalDate?): String? {

        if (date == null) {
            return "Date is required"
        }
        else
            return null
    }
}