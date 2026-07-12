package com.example.csoftproject.domain.models

import androidx.compose.ui.graphics.Color

data class Category(
    val id: Int = 0,
    val icon: Int,
    val name: String,
    val color: Color,
    val updateCount: Int
)
