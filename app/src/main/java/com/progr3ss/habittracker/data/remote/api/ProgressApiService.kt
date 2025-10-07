package com.progr3ss.habittracker.data.remote.api

import com.progr3ss.habittracker.data.remote.dto.CreateProgressRequest
import com.progr3ss.habittracker.data.remote.dto.ProgressDto
import retrofit2.Response
import retrofit2.http.*

interface ProgressApiService {
    @GET("progress")
    suspend fun getProgress(@Query("scheduleId") scheduleId: String): Response<List<ProgressDto>>

    @POST("progress")
    suspend fun createProgress(@Body request: CreateProgressRequest): Response<ProgressDto>

    @DELETE("progress/{id}")
    suspend fun deleteProgress(@Path("id") id: String): Response<Unit>
}
