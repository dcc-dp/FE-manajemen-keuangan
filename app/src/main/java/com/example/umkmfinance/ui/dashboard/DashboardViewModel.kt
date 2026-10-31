package com.example.umkmfinance.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.DashboardData
import com.example.umkmfinance.data.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = false,
    val data: DashboardData? = null,
    val errorMessage: String? = null
)

class DashboardViewModel(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DashboardUiState()
    )

    val uiState: StateFlow<DashboardUiState> =
        _uiState.asStateFlow()

    fun loadDashboard() {

        viewModelScope.launch {

            _uiState.value = DashboardUiState(
                isLoading = true
            )

            try {

                val response = repository.getDashboard()

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body?.success == true && body.data != null) {

                        _uiState.value = DashboardUiState(
                            isLoading = false,
                            data = body.data
                        )

                    } else {

                        _uiState.value = DashboardUiState(
                            errorMessage = body?.message
                                ?: "Data dashboard tidak ditemukan"
                        )
                    }

                } else {

                    _uiState.value = DashboardUiState(
                        errorMessage = when (response.code()) {

                            401 -> "Sesi login sudah tidak valid"

                            403 -> "Anda tidak memiliki akses dashboard"

                            else -> "Gagal mengambil dashboard (${response.code()})"
                        }
                    )
                }

            } catch (e: Exception) {

                _uiState.value = DashboardUiState(
                    errorMessage = "Tidak dapat terhubung ke server"
                )
            }
        }
    }

    fun resetError() {

        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}