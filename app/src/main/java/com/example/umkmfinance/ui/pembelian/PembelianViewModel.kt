package com.example.umkmfinance.ui.pembelian

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.Pembelian
import com.example.umkmfinance.data.repository.PembelianRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PembelianListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: List<Pembelian> = emptyList()
)

data class DetailPembelianUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: Pembelian? = null
)

class PembelianViewModel(
    context: Context
) : ViewModel() {

    private val repository = PembelianRepository(context)

    private val _uiState = MutableStateFlow(PembelianListUiState())
    val uiState: StateFlow<PembelianListUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(DetailPembelianUiState())
    val detailUiState: StateFlow<DetailPembelianUiState> = _detailUiState.asStateFlow()

    fun loadPembelian() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getDaftarPembelian()
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    data = list
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Gagal memuat data pembelian"
                )
            }
        }
    }

    fun loadPembelianById(id: Long) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getDetailPembelian(id)
            result.onSuccess { detail ->
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    data = detail
                )
            }.onFailure { error ->
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Gagal memuat detail pembelian"
                )
            }
        }
    }
}