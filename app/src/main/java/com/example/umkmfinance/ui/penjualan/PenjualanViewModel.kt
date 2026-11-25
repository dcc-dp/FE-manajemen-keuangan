package com.example.umkmfinance.ui.penjualan

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.Penjualan
import com.example.umkmfinance.data.repository.PenjualanRepository
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

    private val repository = PenjualanRepository(context)

    private val _uiState = MutableStateFlow(PenjualanUiState())
    val uiState: StateFlow<PenjualanUiState> = _uiState.asStateFlow()

    // Memuat daftar seluruh transaksi penjualan
    fun loadPenjualan() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            repository.getDaftarPenjualan()
                .onSuccess { list ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        data = list,
                        errorMessage = null
                    )
                }
                .onFailure { e ->
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
            repository.getDetailPenjualan(id)
                .onSuccess { detail ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        detailPenjualan = detail,
                        errorMessage = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Gagal memuat detail penjualan"
                    )
                }
        }
    }

    // Menyimpan transaksi penjualan baru
    fun simpanPenjualan(
        penjualan: Penjualan,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            repository.simpanPenjualan(penjualan)
                .onSuccess { tersimpan ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        data = listOf(tersimpan) + _uiState.value.data,
                        detailPenjualan = tersimpan,
                        errorMessage = null
                    )
                    onSuccess()
                }
                .onFailure { e ->
                    val pesan = e.localizedMessage ?: "Gagal menyimpan transaksi penjualan"
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = pesan
                    )
                    onError(pesan)
                }
        }
    }
}