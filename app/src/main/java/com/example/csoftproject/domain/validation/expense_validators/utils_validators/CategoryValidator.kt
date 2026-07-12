package com.example.csoftproject.domain.validation.expense_validators.utils_validators

object CategoryValidator {

    fun validate(categoryId: Int?): String? {
        if (categoryId == null) {
            return "Category is required"
        }
        else
            return null
    }
}