package com.example.csoftproject.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "expenses")
data class ExpenseEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var transactionValue: Double,
    val categoryId: Int,
    val transactionDate: LocalDate,
    val transactionDescription: String,
    val paymentMethod: String,
    val updateCount: Int
)