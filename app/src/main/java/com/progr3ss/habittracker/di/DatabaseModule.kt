package com.progr3ss.habittracker.di

import android.content.Context
import androidx.room.Room
import com.progr3ss.habittracker.data.local.HabitDatabase
import com.progr3ss.habittracker.data.local.dao.HabitDao
import com.progr3ss.habittracker.data.local.dao.ScheduleDao
import com.progr3ss.habittracker.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHabitDatabase(@ApplicationContext context: Context): HabitDatabase {
        return Room.databaseBuilder(
            context,
            HabitDatabase::class.java,
            "habit_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: HabitDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideHabitDao(database: HabitDatabase): HabitDao {
        return database.habitDao()
    }

    @Provides
    @Singleton
    fun provideScheduleDao(database: HabitDatabase): ScheduleDao {
        return database.scheduleDao()
    }
}
