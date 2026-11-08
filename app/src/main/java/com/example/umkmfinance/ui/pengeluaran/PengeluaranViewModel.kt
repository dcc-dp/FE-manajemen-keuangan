package com.example.umkmfinance.ui.pengeluaran

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.Pengeluaran
import com.example.umkmfinance.data.repository.PengeluaranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PengeluaranListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: List<Pengeluaran> = emptyList()
)

data class DetailPengeluaranUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: Pengeluaran? = null
)

class PengeluaranViewModel(
    context: Context
) : ViewModel() {

    private val repository = PengeluaranRepository(context)

    private val _uiState = MutableStateFlow(PengeluaranListUiState())
    val uiState: StateFlow<PengeluaranListUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(DetailPengeluaranUiState())
    val detailUiState: StateFlow<DetailPengeluaranUiState> = _detailUiState.asStateFlow()

    fun loadPengeluaran() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getDaftarPengeluaran()
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    data = list
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Gagal memuat data pengeluaran"
                )
            }
        }
    }

    fun loadPengeluaranById(id: Long) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getDetailPengeluaran(id)
            result.onSuccess { detail ->
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    data = detail
                )
            }.onFailure { error ->
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Gagal memuat detail pengeluaran"
                )
            }
        }
    }
}