package com.progr3ss.habittracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey val id: String,
    val habitId: String,
    val habitName: String,
    val startTime: String,
    val endTime: String,
    val repeatPattern: String,
    val status: String,
    val date: String,
    val notes: String,
    val createdAt: Long
)
