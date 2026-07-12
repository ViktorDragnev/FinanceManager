package com.example.csoftproject.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.csoftproject.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(categoryEntity: CategoryEntity)

    @Upsert
    suspend fun upsertCategory(categoryEntity: CategoryEntity)

    @Update
    suspend fun updateCategory(categoryEntity: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: Int)

    @Query("SELECT EXISTS (SELECT 1 FROM categories WHERE id = :id)")
    suspend fun categoryExistsById(id: Int): Boolean

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryEntity?

    @Query("SELECT COUNT(*) > 0 FROM categories WHERE LOWER(name) = LOWER(:name)")
    suspend fun categoryExistsByName(name: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM categories WHERE LOWER(name) = LOWER(:name) AND id != :excludeId)")
    suspend fun categoryExistsByNameExcludingId(name: String, excludeId: Int): Boolean

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>
}