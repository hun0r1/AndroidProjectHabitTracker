package com.progr3ss.habittracker.data.local.dao

import androidx.room.*
import com.progr3ss.habittracker.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE userId = :userId")
    fun getUserHabits(userId: String): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun getHabitById(habitId: String): Flow<HabitEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<HabitEntity>)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("DELETE FROM habits")
    suspend fun deleteAllHabits()
}
