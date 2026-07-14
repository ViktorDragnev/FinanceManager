package com.example.csoftproject.domain.models.dtos

data class AddCategoryDto(
    val name: String,
    val icon: Int,
    val color: androidx.compose.ui.graphics.Color,
    val budgetLimit: Double? = null
)
