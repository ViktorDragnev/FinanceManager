package com.example.csoftproject.domain.services

import androidx.compose.ui.graphics.Color
import com.example.csoftproject.domain.models.Category
import com.example.csoftproject.domain.models.dtos.AddCategoryDto
import kotlinx.coroutines.flow.StateFlow

interface CategoryDataService {

    suspend fun addCategory(addCategoryDto: AddCategoryDto)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(id: Int)
    suspend fun getCategoryById(id: Int): Category?
    suspend fun categoryExistsByName(name: String): Boolean
    suspend fun categoryExistsByName(name: String, excludeId: Int): Boolean
    fun getCategoryFlow(): StateFlow<List<Category>>
}