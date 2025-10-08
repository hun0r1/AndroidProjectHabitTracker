package com.progr3ss.habittracker.data.remote.api

import com.progr3ss.habittracker.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {
    @POST("auth/local/signup")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/local/signin")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/local/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<AuthResponse>

    @POST("auth/local/logout")
    suspend fun logout(): Response<Unit>

    @POST("auth/reset-password-via-email")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Unit>
}
