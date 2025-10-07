package com.progr3ss.habittracker.data.remote.api

import com.progr3ss.habittracker.data.remote.dto.CreateHabitRequest
import com.progr3ss.habittracker.data.remote.dto.HabitCategoryDto
import com.progr3ss.habittracker.data.remote.dto.HabitDto
import retrofit2.Response
import retrofit2.http.*

interface HabitApiService {
    @GET("habit")
    suspend fun getHabits(): Response<List<HabitDto>>

    @GET("habit/user/{userId}")
    suspend fun getUserHabits(@Path("userId") userId: String): Response<List<HabitDto>>

    @POST("habit")
    suspend fun createHabit(@Body request: CreateHabitRequest): Response<HabitDto>

    @GET("habit/categories")
    suspend fun getCategories(): Response<List<HabitCategoryDto>>

    @DELETE("habit/{id}")
    suspend fun deleteHabit(@Path("id") id: String): Response<Unit>
}
