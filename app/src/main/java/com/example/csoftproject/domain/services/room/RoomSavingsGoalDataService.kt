package com.example.csoftproject.domain.services.room

import com.example.csoftproject.data.dao.SavingsGoalDao
import com.example.csoftproject.domain.mappers.SavingsGoalMapper.toDomain
import com.example.csoftproject.domain.mappers.SavingsGoalMapper.toEntity
import com.example.csoftproject.domain.models.SavingsGoal
import com.example.csoftproject.domain.services.SavingsGoalDataService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*

class RoomSavingsGoalDataService(
    private val savingsGoalDao: SavingsGoalDao
) : SavingsGoalDataService {

    private val goalsFlow = savingsGoalDao.getAllGoals()
        .map { entities -> entities.map { it.toDomain() } }
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override suspend fun addGoal(goal: SavingsGoal) {
        savingsGoalDao.insert(goal.toEntity())
    }

    override suspend fun updateGoal(goal: SavingsGoal) {
        savingsGoalDao.update(goal.toEntity())
    }

    override suspend fun deleteGoal(goal: SavingsGoal) {
        savingsGoalDao.delete(goal.toEntity())
    }

    override fun getGoalsFlow(): StateFlow<List<SavingsGoal>> {
        return goalsFlow
    }
}
