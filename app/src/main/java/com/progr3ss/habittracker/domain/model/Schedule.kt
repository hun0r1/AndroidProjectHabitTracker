package com.progr3ss.habittracker.domain.model

data class Schedule(
    val id: String,
    val habitId: String,
    val habitName: String,
    val startTime: String,
    val endTime: String,
    val repeatPattern: RepeatPattern,
    val status: ScheduleStatus,
    val date: String,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class RepeatPattern {
    DAILY, WEEKLY, MONTHLY, CUSTOM
}

enum class ScheduleStatus {
    PENDING, IN_PROGRESS, COMPLETED
}
