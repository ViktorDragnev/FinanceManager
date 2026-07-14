package com.example.csoftproject.domain.services.room

import androidx.compose.ui.graphics.toArgb
import com.example.csoftproject.data.dao.CategoryDao
import com.example.csoftproject.data.entity.CategoryEntity
import com.example.csoftproject.domain.mappers.CategoryMapper.toDomainCategory
import com.example.csoftproject.domain.models.Category
import com.example.csoftproject.domain.models.dtos.AddCategoryDto
import com.example.csoftproject.domain.services.CategoryDataService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RoomCategoryDataService(private val categoryDao: CategoryDao): CategoryDataService {

    private val categoriesFlow = categoryDao.getAllCategories()
        .map { entities ->
            entities.map { categoryEntity -> categoryEntity.toDomainCategory() }
        }
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override suspend fun getCategoryById(id: Int): Category? {
        val entity = categoryDao.getCategoryById(id)
        return entity?.let { entity.toDomainCategory() }
    }

    override suspend fun categoryExistsByName(name: String): Boolean {
        if (name.isBlank()) {
            return false
        }
        return categoryDao.categoryExistsByName(name)
    }

    override suspend fun categoryExistsByName(name: String, excludeId: Int): Boolean {
        if (name.isBlank()) {
            return false
        }
        return categoryDao.categoryExistsByNameExcludingId(name, excludeId)
    }

    override suspend fun addCategory(addCategoryDto: AddCategoryDto) {

        val newCategory = CategoryEntity(
            name = addCategoryDto.name,
            icon = addCategoryDto.icon,
            color = String.format("#%08X", addCategoryDto.color.toArgb()),
            budgetLimit = addCategoryDto.budgetLimit,
            updateCount = 0
        )

        categoryDao.insertCategory(newCategory)
    }

    override suspend fun updateCategory(category: Category) {

        val existing = categoryDao.getCategoryById(category.id) ?: return

        if (existing.updateCount != category.updateCount) {
            return
        }

        val updated = existing.copy(
            name = category.name,
            icon = category.icon,
            color = String.format("#%08X", category.color.toArgb()),
            budgetLimit = category.budgetLimit,
            updateCount = existing.updateCount + 1
        )

        categoryDao.updateCategory(updated)
    }

    override suspend fun deleteCategory(id: Int) {
        val exists = categoryDao.categoryExistsById(id)
        if (exists) {
            categoryDao.deleteCategoryById(id)
        }
    }

    override fun getCategoryFlow(): StateFlow<List<Category>> {
        return categoriesFlow
    }
}