package com.example.umkmfinance.ui.piutang

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.Piutang
import com.example.umkmfinance.data.repository.PiutangRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PiutangListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: List<Piutang> = emptyList()
)

data class DetailPiutangUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: Piutang? = null
)

class PiutangViewModel(
    context: Context
) : ViewModel() {

    private val repository = PiutangRepository(context)

    private val _uiState = MutableStateFlow(PiutangListUiState())
    val uiState: StateFlow<PiutangListUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(DetailPiutangUiState())
    val detailUiState: StateFlow<DetailPiutangUiState> = _detailUiState.asStateFlow()

    fun loadPiutang() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getDaftarPiutang()
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    data = list
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Gagal memuat data piutang"
                )
            }
        }
    }

    fun loadPiutangById(id: Long) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getDetailPiutang(id)
            result.onSuccess { detail ->
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    data = detail
                )
            }.onFailure { error ->
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Gagal memuat detail piutang"
                )
            }
        }
    }
}