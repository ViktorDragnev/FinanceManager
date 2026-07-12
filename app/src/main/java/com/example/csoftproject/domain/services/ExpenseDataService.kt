package com.example.csoftproject.domain.services

import com.example.csoftproject.domain.models.Expense
import com.example.csoftproject.domain.models.dtos.AddExpenseDto
import kotlinx.coroutines.flow.StateFlow

interface ExpenseDataService {
    suspend fun addExpense(addExpenseDto: AddExpenseDto)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(id: Int)
    fun getExpenseById(id: Int): Expense?
    fun getExpenseFlow(): StateFlow<List<Expense>>
}