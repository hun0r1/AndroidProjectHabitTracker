package com.progr3ss.habittracker.domain.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val profileImageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
