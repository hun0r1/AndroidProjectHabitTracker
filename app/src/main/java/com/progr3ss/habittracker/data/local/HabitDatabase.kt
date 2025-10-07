package com.progr3ss.habittracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.progr3ss.habittracker.data.local.dao.HabitDao
import com.progr3ss.habittracker.data.local.dao.ScheduleDao
import com.progr3ss.habittracker.data.local.dao.UserDao
import com.progr3ss.habittracker.data.local.entity.HabitEntity
import com.progr3ss.habittracker.data.local.entity.ScheduleEntity
import com.progr3ss.habittracker.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, HabitEntity::class, ScheduleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun habitDao(): HabitDao
    abstract fun scheduleDao(): ScheduleDao
}
