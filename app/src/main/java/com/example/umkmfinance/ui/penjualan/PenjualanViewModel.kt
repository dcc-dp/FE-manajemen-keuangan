package com.example.umkmfinance.ui.penjualan

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.Penjualan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PenjualanUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: List<Penjualan> = emptyList(),
    val detailPenjualan: Penjualan? = null
)

class PenjualanViewModel(
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(PenjualanUiState())
    val uiState: StateFlow<PenjualanUiState> = _uiState.asStateFlow()

    // Memuat daftar seluruh transaksi penjualan
    fun loadPenjualan() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                // TODO: Hubungkan ke PenjualanRepository / API Backend Node.js
                // val list = repository.getPenjualanList()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    data = emptyList()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Gagal memuat transaksi penjualan"
                )
            }
        }
    }

    // Memuat detail struk/invoice berdasarkan ID transaksi
    fun loadDetailPenjualan(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                // TODO: Hubungkan ke PenjualanRepository / API Backend Node.js
                // val detail = repository.getPenjualanById(id)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    detailPenjualan = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Gagal memuat detail penjualan"
                )
            }
        }
    }
}