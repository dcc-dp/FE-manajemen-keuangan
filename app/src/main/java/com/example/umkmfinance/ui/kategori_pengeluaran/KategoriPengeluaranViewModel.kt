package com.example.umkmfinance.ui.pengeluaran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.KategoriPengeluaran
import com.example.umkmfinance.data.repository.KategoriPengeluaranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class KategoriPengeluaranUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: List<KategoriPengeluaran> = emptyList(),
    val isSaved: Boolean = false
)

class KategoriPengeluaranViewModel(
    private val repository: KategoriPengeluaranRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KategoriPengeluaranUiState())
    val uiState: StateFlow<KategoriPengeluaranUiState> = _uiState.asStateFlow()

    fun loadKategoriPengeluaran(usahaId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getDaftarKategoriPengeluaran(usahaId)
                .onSuccess { list ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            data = list
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Gagal memuat kategori pengeluaran"
                        )
                    }
                }
        }
    }

    fun tambahKategoriPengeluaran(usahaId: Long, nama: String) {
        val bersih = nama.trim()
        if (bersih.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Nama kategori tidak boleh kosong") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSaved = false) }
            repository.simpanKategoriPengeluaran(usahaId = usahaId, nama = bersih)
                .onSuccess { baru ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = null,
                            data = state.data + baru,
                            isSaved = true
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Gagal menyimpan kategori pengeluaran",
                            isSaved = false
                        )
                    }
                }
        }
    }

    fun resetSavedFlag() {
        _uiState.update { it.copy(isSaved = false) }
    }
}