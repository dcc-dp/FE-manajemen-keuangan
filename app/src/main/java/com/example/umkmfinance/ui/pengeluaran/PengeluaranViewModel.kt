package com.example.umkmfinance.ui.pengeluaran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.KategoriPengeluaran
import com.example.umkmfinance.data.model.Pengeluaran
import com.example.umkmfinance.data.repository.PengeluaranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PengeluaranUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: List<Pengeluaran> = emptyList(),
    val isSaved: Boolean = false
)

class PengeluaranViewModel(
    private val repository: PengeluaranRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PengeluaranUiState())
    val uiState: StateFlow<PengeluaranUiState> = _uiState.asStateFlow()

    fun loadPengeluaran(usahaId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getDaftarPengeluaran(usahaId)
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
                            errorMessage = e.message ?: "Gagal memuat data pengeluaran"
                        )
                    }
                }
        }
    }

    fun tambahPengeluaran(
        usahaId: Long,
        kategori: KategoriPengeluaran,
        rekeningId: Long,
        tanggal: String,
        nominal: Double,
        keterangan: String?,
        createdBy: Long
    ) {
        if (nominal <= 0.0) {
            _uiState.update { it.copy(errorMessage = "Nominal pengeluaran harus lebih dari 0") }
            return
        }

        val itemBaru = Pengeluaran(
            usaha_id = usahaId,
            kategori_pengeluaran_id = kategori.id,
            rekening_id = rekeningId,
            tanggal = tanggal,
            nominal = nominal,
            keterangan = keterangan?.trim()?.ifBlank { null },
            created_by = createdBy,
            kategori_pengeluaran = kategori
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSaved = false) }
            repository.simpanPengeluaran(itemBaru)
                .onSuccess { tersimpan ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = null,
                            data = listOf(tersimpan) + state.data,
                            isSaved = true
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Gagal menyimpan pengeluaran",
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