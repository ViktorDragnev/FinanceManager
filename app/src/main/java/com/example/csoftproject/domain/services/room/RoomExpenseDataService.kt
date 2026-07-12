package com.example.csoftproject.domain.services.room

import com.example.csoftproject.data.dao.CategoryDao
import com.example.csoftproject.data.dao.ExpenseDao
import com.example.csoftproject.data.entity.ExpenseEntity
import com.example.csoftproject.domain.enums.PaymentMethod
import com.example.csoftproject.domain.mappers.ExpenseMapper.toDomainExpense
import com.example.csoftproject.domain.models.Expense
import com.example.csoftproject.domain.models.dtos.AddExpenseDto
import com.example.csoftproject.domain.services.ExpenseDataService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class RoomExpenseDataService(
    private val expenseDao: ExpenseDao,
    categoryDao: CategoryDao
): ExpenseDataService {

    private val expenseFlow = combine(
        expenseDao.getAllExpenses(),
        categoryDao.getAllCategories()
    ) { expenses, categories ->
        val categoryMap = categories.associateBy { it.id }
        expenses.mapNotNull { entity ->
            val category = categoryMap[entity.categoryId]
            if (category != null) {
                entity.toDomainExpense(category)
            } else {
                null
            }
        }
    }.stateIn(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
    )

    override suspend fun addExpense(addExpenseDto: AddExpenseDto) {
        val newExpense = ExpenseEntity(
            transactionValue = addExpenseDto.value,
            categoryId = addExpenseDto.categoryId,
            transactionDate = addExpenseDto.date,
            transactionDescription = addExpenseDto.description,
            paymentMethod = addExpenseDto.paymentMethod.name,
            updateCount = 0
        )
        expenseDao.insertExpense(newExpense)
    }

    override suspend fun updateExpense(expense: Expense) {

        val existingEntity = expenseDao.getExpenseById(expense.id) ?: return

        if (existingEntity.updateCount != expense.updateCount) {
            return
        }

        val updatedExpense = existingEntity.copy(
            transactionValue = expense.transactionValue,
            categoryId = expense.category.id,
            transactionDate = expense.transactionDate,
            transactionDescription = expense.transactionDescription,
            paymentMethod = expense.paymentMethod.name,
            updateCount = existingEntity.updateCount + 1
        )

        expenseDao.updateExpense(updatedExpense)
    }


    override suspend fun deleteExpense(id: Int) {
        val exists = expenseDao.expenseExists(id)
        if (exists) {
            expenseDao.deleteExpenseById(id)
        }
    }

    override fun getExpenseById(id: Int): Expense? {
        return expenseFlow.value.find { it.id == id }
    }

    override fun getExpenseFlow(): StateFlow<List<Expense>> {
        return expenseFlow
    }
}