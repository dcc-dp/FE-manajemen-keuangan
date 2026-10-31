package com.example.umkmfinance.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.local.TokenManager
import com.example.umkmfinance.data.remote.ApiService
import com.example.umkmfinance.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val role: String? = null
)

class LoginViewModel(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(
        username: String,
        password: String
    ) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(
                errorMessage = "Username dan password wajib diisi"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)

            try {
                val response = repository.login(
                    username = username,
                    password = password
                )

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body?.success == true && body.data != null) {
                        val data = body.data
                        val user = data.user

                        val role = user.roles.firstOrNull()

                        if (role == null) {
                            _uiState.value = LoginUiState(
                                errorMessage = "Role pengguna tidak ditemukan"
                            )
                            return@launch
                        }

                        tokenManager.saveToken(data.token)
                        tokenManager.saveUserId(user.id)
                        tokenManager.saveUsername(user.username)
                        tokenManager.saveRole(role)

                        _uiState.value = LoginUiState(
                            isSuccess = true,
                            role = role
                        )

                    } else {
                        _uiState.value = LoginUiState(
                            errorMessage = body?.message
                                ?: "Login gagal"
                        )
                    }

                } else {
                    _uiState.value = LoginUiState(
                        errorMessage = when (response.code()) {
                            401 -> "Username atau password salah"
                            403 -> "Akun tidak memiliki akses"
                            else -> "Login gagal (${response.code()})"
                        }
                    )
                }

            } catch (e: Exception) {
                _uiState.value = LoginUiState(
                    errorMessage = "Tidak dapat terhubung ke server"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}