package com.example.csoftproject.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.csoftproject.data.database.converters.ColorConverter
import com.example.csoftproject.data.database.converters.DateConverter
import com.example.csoftproject.data.database.converters.PaymentMethodConverter
import com.example.csoftproject.data.dao.CategoryDao
import com.example.csoftproject.data.dao.ExpenseDao
import com.example.csoftproject.data.dao.SavingsGoalDao
import com.example.csoftproject.data.entity.CategoryEntity
import com.example.csoftproject.data.entity.ExpenseEntity
import com.example.csoftproject.data.entity.SavingsGoalEntity

@Database(
    entities = [ExpenseEntity::class, CategoryEntity::class, SavingsGoalEntity::class],
    version = 2
)
@TypeConverters(
    DateConverter::class,
    ColorConverter::class,
    PaymentMethodConverter::class
)
abstract class DatabaseImpl: RoomDatabase(){

    abstract val expenseDao: ExpenseDao
    abstract val categoryDao: CategoryDao
    abstract val savingsGoalDao: SavingsGoalDao

    companion object{

        @Volatile
        private var INSTANCE: DatabaseImpl? = null

        fun getInstance(context: Context): DatabaseImpl{

            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): DatabaseImpl {
            return Room.databaseBuilder(
                context.applicationContext,
                DatabaseImpl::class.java,
                "finance_manager"
            )
                .fallbackToDestructiveMigration(false)
                .build()
        }
    }
}