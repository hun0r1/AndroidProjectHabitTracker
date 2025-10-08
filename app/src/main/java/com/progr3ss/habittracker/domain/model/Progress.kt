package com.progr3ss.habittracker.domain.model

data class Progress(
    val id: String,
    val scheduleId: String,
    val habitId: String,
    val value: Int,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
