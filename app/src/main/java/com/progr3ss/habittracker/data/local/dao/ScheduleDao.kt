package com.progr3ss.habittracker.data.local.dao

import androidx.room.*
import com.progr3ss.habittracker.data.local.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules WHERE date = :date ORDER BY startTime ASC")
    fun getSchedulesByDate(date: String): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE id = :scheduleId")
    fun getScheduleById(scheduleId: String): Flow<ScheduleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ScheduleEntity>)

    @Update
    suspend fun updateSchedule(schedule: ScheduleEntity)

    @Delete
    suspend fun deleteSchedule(schedule: ScheduleEntity)

    @Query("DELETE FROM schedules")
    suspend fun deleteAllSchedules()
}
