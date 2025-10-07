package com.progr3ss.habittracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ScheduleDto(
    @SerializedName("id") val id: String,
    @SerializedName("habitId") val habitId: String,
    @SerializedName("habitName") val habitName: String? = null,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("repeatPattern") val repeatPattern: String,
    @SerializedName("status") val status: String,
    @SerializedName("date") val date: String,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("createdAt") val createdAt: Long? = null
)

data class CreateScheduleRequest(
    @SerializedName("habitId") val habitId: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("repeatPattern") val repeatPattern: String,
    @SerializedName("date") val date: String
)

data class UpdateScheduleRequest(
    @SerializedName("startTime") val startTime: String? = null,
    @SerializedName("endTime") val endTime: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("notes") val notes: String? = null
)
