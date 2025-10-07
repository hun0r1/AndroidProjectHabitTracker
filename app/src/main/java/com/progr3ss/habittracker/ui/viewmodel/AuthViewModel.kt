package com.progr3ss.habittracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progr3ss.habittracker.data.repository.AuthRepository
import com.progr3ss.habittracker.domain.model.User
import com.progr3ss.habittracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<User>?>(null)
    val loginState: StateFlow<Resource<User>?> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<Resource<User>?>(null)
    val registerState: StateFlow<Resource<User>?> = _registerState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = authRepository.isLoggedIn()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { resource ->
                _loginState.value = resource
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            authRepository.register(username, email, password).collect { resource ->
                _registerState.value = resource
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout().collect()
        }
    }

    fun resetLoginState() {
        _loginState.value = null
    }

    fun resetRegisterState() {
        _registerState.value = null
    }
}
