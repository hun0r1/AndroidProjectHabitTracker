package com.progr3ss.habittracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProgressDto(
    @SerializedName("id") val id: String,
    @SerializedName("scheduleId") val scheduleId: String,
    @SerializedName("habitId") val habitId: String,
    @SerializedName("value") val value: Int,
    @SerializedName("notes") val notes: String? = null,
    @SerializedName("createdAt") val createdAt: Long? = null
)

data class CreateProgressRequest(
    @SerializedName("scheduleId") val scheduleId: String,
    @SerializedName("habitId") val habitId: String,
    @SerializedName("value") val value: Int,
    @SerializedName("notes") val notes: String? = null
)
