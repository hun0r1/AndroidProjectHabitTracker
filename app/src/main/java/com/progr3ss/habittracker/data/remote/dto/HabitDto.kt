package com.progr3ss.habittracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HabitDto(
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String,
    @SerializedName("goal") val goal: String,
    @SerializedName("createdAt") val createdAt: Long? = null
)

data class CreateHabitRequest(
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String,
    @SerializedName("goal") val goal: String
)

data class HabitCategoryDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)
