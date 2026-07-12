package com.example.csoftproject.ui.category

import androidx.compose.ui.graphics.Color

data class CategoryStateForm(
    val name: String,
    val icon: Int,
    val color: Color = Color(0xFFEF5350)
)