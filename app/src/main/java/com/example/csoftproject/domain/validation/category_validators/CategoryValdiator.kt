package com.example.csoftproject.domain.validation.category_validators

import com.example.csoftproject.domain.validation.ValidationResult

object CategoryValidator {

    fun validate(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(
                isValid = false,
                nameError = "Category name is required"
            )
        }

        if (name.length <= 3) {
            return ValidationResult(
                isValid = false,
                nameError = "Name must be at least 4 characters"
            )
        }

        if (name.length > 12) {
            return ValidationResult(
                isValid = false,
                nameError = "Name must be less than 12 characters"
            )
        }

        return ValidationResult(isValid = true)
    }
}