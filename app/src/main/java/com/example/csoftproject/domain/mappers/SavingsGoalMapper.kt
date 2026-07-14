package com.example.csoftproject.domain.mappers

import com.example.csoftproject.data.entity.SavingsGoalEntity
import com.example.csoftproject.domain.models.SavingsGoal

object SavingsGoalMapper {
    fun SavingsGoalEntity.toDomain(): SavingsGoal {
        return SavingsGoal(
            id = id,
            title = title,
            targetAmount = targetAmount,
            currentAmount = currentAmount,
            updateCount = updateCount
        )
    }

    fun SavingsGoal.toEntity(): SavingsGoalEntity {
        return SavingsGoalEntity(
            id = id,
            title = title,
            targetAmount = targetAmount,
            currentAmount = currentAmount,
            updateCount = updateCount
        )
    }
}
