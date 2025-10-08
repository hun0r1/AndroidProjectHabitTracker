package com.progr3ss.habittracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String? = null,
    @SerializedName("createdAt") val createdAt: Long? = null
)

data class UpdateProfileRequest(
    @SerializedName("username") val username: String
)
