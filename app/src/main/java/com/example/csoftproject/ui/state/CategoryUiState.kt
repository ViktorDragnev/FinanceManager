package com.example.csoftproject.ui.state

import com.example.csoftproject.domain.models.Category

sealed class CategoryUiState{

    object Loading : CategoryUiState()
    data class Success(val data: List<Category>) : CategoryUiState()
    data class EmptyList(val message: String) : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
}