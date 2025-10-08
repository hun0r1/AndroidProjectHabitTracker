package com.progr3ss.habittracker.data.remote.api

import com.progr3ss.habittracker.data.remote.dto.CreateScheduleRequest
import com.progr3ss.habittracker.data.remote.dto.ScheduleDto
import com.progr3ss.habittracker.data.remote.dto.UpdateScheduleRequest
import retrofit2.Response
import retrofit2.http.*

interface ScheduleApiService {
    @GET("schedule/day")
    suspend fun getDaySchedules(@Query("date") date: String): Response<List<ScheduleDto>>

    @GET("schedule/{id}")
    suspend fun getScheduleById(@Path("id") id: String): Response<ScheduleDto>

    @POST("schedule/day")
    suspend fun createDaySchedule(@Body request: CreateScheduleRequest): Response<ScheduleDto>

    @POST("schedule/recurring")
    suspend fun createRecurringSchedule(@Body request: CreateScheduleRequest): Response<ScheduleDto>

    @PUT("schedule/{id}")
    suspend fun updateSchedule(
        @Path("id") id: String,
        @Body request: UpdateScheduleRequest
    ): Response<ScheduleDto>

    @DELETE("schedule/{id}")
    suspend fun deleteSchedule(@Path("id") id: String): Response<Unit>
}
