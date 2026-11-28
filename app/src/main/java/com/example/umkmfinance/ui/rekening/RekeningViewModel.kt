package com.example.umkmfinance.ui.rekening

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.Rekening
import com.example.umkmfinance.data.repository.RekeningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RekeningUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: List<Rekening> = emptyList(),
    val isSaved: Boolean = false
)

class RekeningViewModel(
    private val repository: RekeningRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RekeningUiState())
    val uiState: StateFlow<RekeningUiState> = _uiState.asStateFlow()

    fun loadRekening(usahaId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getDaftarRekening(usahaId)
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
                            errorMessage = e.message ?: "Gagal memuat data rekening"
                        )
                    }
                }
        }
    }

    fun tambahRekening(
        usahaId: Long,
        namaRekening: String,
        bank: String,
        nomorRekening: String?,
        atasNama: String?,
        saldoAwal: Double
    ) {
        val namaBersih = namaRekening.trim()
        val bankBersih = bank.trim()
        if (namaBersih.isBlank() || bankBersih.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Nama rekening dan bank wajib diisi") }
            return
        }

        val itemBaru = Rekening(
            usaha_id = usahaId,
            nama_rekening = namaBersih,
            bank = bankBersih,
            nomor_rekening = nomorRekening?.trim()?.ifBlank { null },
            atas_nama = atasNama?.trim()?.ifBlank { null },
            saldo_awal = saldoAwal,
            saldo = saldoAwal
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSaved = false) }
            repository.simpanRekening(itemBaru)
                .onSuccess { tersimpan ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = null,
                            data = state.data + tersimpan,
                            isSaved = true
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Gagal menyimpan rekening",
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