package com.progr3ss.habittracker.data.repository

import com.progr3ss.habittracker.data.local.dao.UserDao
import com.progr3ss.habittracker.data.local.entity.UserEntity
import com.progr3ss.habittracker.data.remote.api.AuthApiService
import com.progr3ss.habittracker.data.remote.api.UserApiService
import com.progr3ss.habittracker.data.remote.dto.*
import com.progr3ss.habittracker.domain.model.AuthTokens
import com.progr3ss.habittracker.domain.model.User
import com.progr3ss.habittracker.util.PreferencesManager
import com.progr3ss.habittracker.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApiService,
    private val userApi: UserApiService,
    private val userDao: UserDao,
    private val preferencesManager: PreferencesManager
) {
    suspend fun login(email: String, password: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                preferencesManager.saveTokens(authResponse.accessToken, authResponse.refreshToken)
                preferencesManager.saveUserId(authResponse.user.id)
                
                val user = authResponse.user.toUser()
                userDao.insertUser(user.toEntity())
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error(response.message() ?: "Login failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun register(username: String, email: String, password: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApi.register(RegisterRequest(username, email, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                preferencesManager.saveTokens(authResponse.accessToken, authResponse.refreshToken)
                preferencesManager.saveUserId(authResponse.user.id)
                
                val user = authResponse.user.toUser()
                userDao.insertUser(user.toEntity())
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error(response.message() ?: "Registration failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun logout(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            authApi.logout()
            preferencesManager.clearTokens()
            userDao.deleteAllUsers()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Logout failed"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun resetPassword(email: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApi.resetPassword(ResetPasswordRequest(email))
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error(response.message() ?: "Reset password failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun isLoggedIn(): Flow<Boolean> {
        return preferencesManager.accessToken.map { it != null }
    }

    suspend fun getProfile(): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val response = userApi.getProfile()
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!.toUser()
                userDao.insertUser(user.toEntity())
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to fetch profile"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateProfile(username: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val response = userApi.updateProfile(UpdateProfileRequest(username))
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!.toUser()
                userDao.insertUser(user.toEntity())
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error(response.message() ?: "Failed to update profile"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    private fun UserDto.toUser() = User(
        id = id,
        username = username,
        email = email,
        profileImageUrl = profileImageUrl,
        createdAt = createdAt ?: System.currentTimeMillis()
    )

    private fun User.toEntity() = UserEntity(
        id = id,
        username = username,
        email = email,
        profileImageUrl = profileImageUrl,
        createdAt = createdAt
    )
}
