package com.progr3ss.habittracker.data.repository

import com.progr3ss.habittracker.data.local.dao.ScheduleDao
import com.progr3ss.habittracker.data.local.entity.ScheduleEntity
import com.progr3ss.habittracker.data.remote.api.ScheduleApiService
import com.progr3ss.habittracker.data.remote.dto.CreateScheduleRequest
import com.progr3ss.habittracker.data.remote.dto.ScheduleDto
import com.progr3ss.habittracker.data.remote.dto.UpdateScheduleRequest
import com.progr3ss.habittracker.domain.model.RepeatPattern
import com.progr3ss.habittracker.domain.model.Schedule
import com.progr3ss.habittracker.domain.model.ScheduleStatus
import com.progr3ss.habittracker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor(
    private val scheduleApi: ScheduleApiService,
    private val scheduleDao: ScheduleDao
) {
    fun getSchedulesByDate(date: String): Flow<List<Schedule>> {
        return scheduleDao.getSchedulesByDate(date).map { entities ->
            entities.map { it.toSchedule() }
        }
    }

    suspend fun fetchSchedulesByDate(date: String): Flow<Resource<List<Schedule>>> = flow {
        emit(Resource.Loading())
        try {
            val response = scheduleApi.getDaySchedules(date)
            if (response.isSuccessful && response.body() != null) {
                val schedules = response.body()!!.map { it.toSchedule() }
                scheduleDao.insertSchedules(schedules.map { it.toEntity() })
                emit(Resource.Success(schedules))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to fetch schedules"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }

    suspend fun createSchedule(
        habitId: String,
        startTime: String,
        endTime: String,
        repeatPattern: String,
        date: String
    ): Flow<Resource<Schedule>> = flow {
        emit(Resource.Loading())
        try {
            val request = CreateScheduleRequest(habitId, startTime, endTime, repeatPattern, date)
            val response = scheduleApi.createDaySchedule(request)
            if (response.isSuccessful && response.body() != null) {
                val schedule = response.body()!!.toSchedule()
                scheduleDao.insertSchedule(schedule.toEntity())
                emit(Resource.Success(schedule))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to create schedule"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }

    suspend fun updateSchedule(
        scheduleId: String,
        startTime: String? = null,
        endTime: String? = null,
        status: String? = null,
        notes: String? = null
    ): Flow<Resource<Schedule>> = flow {
        emit(Resource.Loading())
        try {
            val request = UpdateScheduleRequest(startTime, endTime, status, notes)
            val response = scheduleApi.updateSchedule(scheduleId, request)
            if (response.isSuccessful && response.body() != null) {
                val schedule = response.body()!!.toSchedule()
                scheduleDao.insertSchedule(schedule.toEntity())
                emit(Resource.Success(schedule))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to update schedule"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }

    suspend fun deleteSchedule(scheduleId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val response = scheduleApi.deleteSchedule(scheduleId)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to delete schedule"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }

    private fun ScheduleDto.toSchedule() = Schedule(
        id = id,
        habitId = habitId,
        habitName = habitName ?: "",
        startTime = startTime,
        endTime = endTime,
        repeatPattern = RepeatPattern.valueOf(repeatPattern.uppercase()),
        status = ScheduleStatus.valueOf(status.uppercase().replace(" ", "_")),
        date = date,
        notes = notes ?: "",
        createdAt = createdAt ?: System.currentTimeMillis()
    )

    private fun Schedule.toEntity() = ScheduleEntity(
        id = id,
        habitId = habitId,
        habitName = habitName,
        startTime = startTime,
        endTime = endTime,
        repeatPattern = repeatPattern.name,
        status = status.name,
        date = date,
        notes = notes,
        createdAt = createdAt
    )

    private fun ScheduleEntity.toSchedule() = Schedule(
        id = id,
        habitId = habitId,
        habitName = habitName,
        startTime = startTime,
        endTime = endTime,
        repeatPattern = RepeatPattern.valueOf(repeatPattern),
        status = ScheduleStatus.valueOf(status),
        date = date,
        notes = notes,
        createdAt = createdAt
    )
}
