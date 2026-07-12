package com.example.csoftproject.domain.validation

data class ValidationResult(
    val isValid: Boolean,
    val nameError: String? = null
)