package com.progr3ss.habittracker.domain.model

data class Habit(
    val id: String,
    val userId: String,
    val name: String,
    val category: String,
    val goal: String,
    val createdAt: Long = System.currentTimeMillis()
)
