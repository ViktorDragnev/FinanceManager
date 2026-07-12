package com.example.csoftproject.domain.validation.expense_validators.utils_validators

object DescriptionValidator {

    fun validate(description: String): String? {

        return if(description.isEmpty()){
            "Description is required"

        } else
            null
    }
}