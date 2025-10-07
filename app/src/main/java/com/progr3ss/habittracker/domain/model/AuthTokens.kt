package com.progr3ss.habittracker.domain.model

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String
)
