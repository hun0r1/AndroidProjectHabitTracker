package com.progr3ss.habittracker.data.repository

import com.progr3ss.habittracker.data.local.dao.HabitDao
import com.progr3ss.habittracker.data.local.entity.HabitEntity
import com.progr3ss.habittracker.data.remote.api.HabitApiService
import com.progr3ss.habittracker.data.remote.dto.CreateHabitRequest
import com.progr3ss.habittracker.data.remote.dto.HabitDto
import com.progr3ss.habittracker.domain.model.Habit
import com.progr3ss.habittracker.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitApi: HabitApiService,
    private val habitDao: HabitDao
) {
    fun getUserHabits(userId: String): Flow<List<Habit>> {
        return habitDao.getUserHabits(userId).map { entities ->
            entities.map { it.toHabit() }
        }
    }

    suspend fun fetchUserHabits(userId: String): Flow<Resource<List<Habit>>> = flow {
        emit(Resource.Loading())
        try {
            val response = habitApi.getUserHabits(userId)
            if (response.isSuccessful && response.body() != null) {
                val habits = response.body()!!.map { it.toHabit() }
                habitDao.insertHabits(habits.map { it.toEntity() })
                emit(Resource.Success(habits))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to fetch habits"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun createHabit(name: String, category: String, goal: String): Flow<Resource<Habit>> = flow {
        emit(Resource.Loading())
        try {
            val response = habitApi.createHabit(CreateHabitRequest(name, category, goal))
            if (response.isSuccessful && response.body() != null) {
                val habit = response.body()!!.toHabit()
                habitDao.insertHabit(habit.toEntity())
                emit(Resource.Success(habit))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to create habit"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun deleteHabit(habitId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val response = habitApi.deleteHabit(habitId)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to delete habit"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getCategories(): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading())
        try {
            val response = habitApi.getCategories()
            if (response.isSuccessful && response.body() != null) {
                val categories = response.body()!!.map { it.name }
                emit(Resource.Success(categories))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to fetch categories"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    private fun HabitDto.toHabit() = Habit(
        id = id,
        userId = userId,
        name = name,
        category = category,
        goal = goal,
        createdAt = createdAt ?: System.currentTimeMillis()
    )

    private fun Habit.toEntity() = HabitEntity(
        id = id,
        userId = userId,
        name = name,
        category = category,
        goal = goal,
        createdAt = createdAt
    )

    private fun HabitEntity.toHabit() = Habit(
        id = id,
        userId = userId,
        name = name,
        category = category,
        goal = goal,
        createdAt = createdAt
    )
}
