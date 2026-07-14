package com.example.csoftproject.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csoftproject.domain.models.SavingsGoal
import com.example.csoftproject.domain.services.SavingsGoalDataService
import kotlinx.coroutines.launch

class SavingsGoalViewModel(
    private val savingsGoalDataService: SavingsGoalDataService
) : ViewModel() {

    val goals = savingsGoalDataService.getGoalsFlow()

    fun addGoal(title: String, targetAmount: Double) {
        viewModelScope.launch {
            val goal = SavingsGoal(
                title = title,
                targetAmount = targetAmount,
                currentAmount = 0.0,
                updateCount = 0
            )
            savingsGoalDataService.addGoal(goal)
        }
    }

    fun updateGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            savingsGoalDataService.updateGoal(goal)
        }
    }

    fun deleteGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            savingsGoalDataService.deleteGoal(goal)
        }
    }

    fun addToGoal(goal: SavingsGoal, amount: Double) {
        viewModelScope.launch {
            val updatedGoal = goal.copy(
                currentAmount = goal.currentAmount + amount,
                updateCount = goal.updateCount + 1
            )
            savingsGoalDataService.updateGoal(updatedGoal)
        }
    }
}
