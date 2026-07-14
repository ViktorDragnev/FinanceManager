package com.example.csoftproject.domain.mappers

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.csoftproject.data.entity.CategoryEntity
import com.example.csoftproject.domain.models.Category

object CategoryMapper {

    fun CategoryEntity.toDomainCategory(): Category {
        return Category(
            id = id,
            icon = icon,
            name = name,
            color = Color(parseColor(color)),
            budgetLimit = budgetLimit,
            updateCount = updateCount
        )
    }

    fun Category.toCategoryEntity(): CategoryEntity {
        return CategoryEntity(
            id = id,
            icon = icon,
            name = name,
            color = String.format("#%08X", color.toArgb()),
            budgetLimit = budgetLimit,
            updateCount = updateCount
        )
    }

    fun toDomainList(entities: List<CategoryEntity>): List<Category> {
        return entities.map { entity -> entity.toDomainCategory() }
    }

    fun toEntityList(domains: List<Category>): List<CategoryEntity> {
        return domains.map { domain -> domain.toCategoryEntity() }
    }
}