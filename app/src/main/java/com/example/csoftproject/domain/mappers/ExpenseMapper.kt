package com.example.csoftproject.domain.mappers

import com.example.csoftproject.domain.enums.PaymentMethod
import com.example.csoftproject.domain.mappers.CategoryMapper.toDomainCategory
import com.example.csoftproject.data.entity.CategoryEntity
import com.example.csoftproject.data.entity.ExpenseEntity
import com.example.csoftproject.domain.models.Expense

object ExpenseMapper {

    fun ExpenseEntity.toDomainExpense(category: CategoryEntity): Expense {
        return Expense(
            id = id,
            transactionValue = transactionValue,
            category = category.toDomainCategory(),
            transactionDate = transactionDate,
            transactionDescription = transactionDescription,
            paymentMethod = PaymentMethod.valueOf(paymentMethod),
            updateCount = updateCount
        )
    }

    fun Expense.toExpenseEntity(): ExpenseEntity {
        return ExpenseEntity(
            id = id,
            transactionValue = transactionValue,
            categoryId = category.id,
            transactionDate = transactionDate,
            transactionDescription = transactionDescription,
            paymentMethod = paymentMethod.name,
            updateCount = updateCount
        )
    }

    fun List<ExpenseEntity>.toDomainExpenseList(
        categories: Map<Int, CategoryEntity>
    ): List<Expense> {
        return mapNotNull { entity ->
            val category = categories[entity.categoryId]
            if (category != null) {
                entity.toDomainExpense(category)
            } else {
                null
            }
        }
    }

    fun List<Expense>.toExpenseEntityList(): List<ExpenseEntity> {
        return map { it.toExpenseEntity() }
    }
}