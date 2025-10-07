package com.progr3ss.habittracker.data.remote.api

import com.progr3ss.habittracker.data.remote.dto.UpdateProfileRequest
import com.progr3ss.habittracker.data.remote.dto.UserDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {
    @GET("profile")
    suspend fun getProfile(): Response<UserDto>

    @PUT("profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UserDto>

    @Multipart
    @POST("profile/upload-profile-image")
    suspend fun uploadProfileImage(@Part image: MultipartBody.Part): Response<UserDto>
}
