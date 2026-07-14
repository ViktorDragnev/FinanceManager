package com.example.csoftproject.domain.services

import com.example.csoftproject.domain.models.SavingsGoal
import kotlinx.coroutines.flow.StateFlow

interface SavingsGoalDataService {
    suspend fun addGoal(goal: SavingsGoal)
    suspend fun updateGoal(goal: SavingsGoal)
    suspend fun deleteGoal(goal: SavingsGoal)
    fun getGoalsFlow(): StateFlow<List<SavingsGoal>>
}
