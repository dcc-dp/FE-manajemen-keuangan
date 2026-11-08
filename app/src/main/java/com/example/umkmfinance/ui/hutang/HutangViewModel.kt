package com.example.umkmfinance.ui.hutang

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.Hutang
import com.example.umkmfinance.data.repository.HutangRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HutangListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: List<Hutang> = emptyList()
)

data class DetailHutangUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: Hutang? = null
)

class HutangViewModel(
    context: Context
) : ViewModel() {

    private val repository = HutangRepository(context)

    private val _uiState = MutableStateFlow(HutangListUiState())
    val uiState: StateFlow<HutangListUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(DetailHutangUiState())
    val detailUiState: StateFlow<DetailHutangUiState> = _detailUiState.asStateFlow()

    fun loadHutang() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getDaftarHutang()
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    data = list
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Gagal memuat data hutang"
                )
            }
        }
    }

    fun loadHutangById(id: Long) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getDetailHutang(id)
            result.onSuccess { detail ->
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    data = detail
                )
            }.onFailure { error ->
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Gagal memuat detail hutang"
                )
            }
        }
    }
}