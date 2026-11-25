package com.example.umkmfinance.ui.kategori

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.KategoriProduk
import com.example.umkmfinance.data.repository.KategoriProdukRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class KategoriProdukUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: List<KategoriProduk> = emptyList()
)

class KategoriProdukViewModel(
    private val context: Context
) : ViewModel() {

    private val repository = KategoriProdukRepository(context)

    private val _uiState = MutableStateFlow(KategoriProdukUiState())
    val uiState: StateFlow<KategoriProdukUiState> = _uiState.asStateFlow()

    fun loadKategori() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            repository.getDaftarKategori()
                .onSuccess { list ->
                    _uiState.value = _uiState.value.copy(isLoading = false, data = list)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Gagal memuat kategori produk"
                    )
                }
        }
    }

    fun simpanKategori(kategori: KategoriProduk, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.simpanKategori(kategori).onSuccess { saved ->
                val currentList = _uiState.value.data.toMutableList()
                if (kategori.id == 0L) {
                    val newId = (currentList.maxOfOrNull { it.id } ?: 0L) + 1L
                    currentList.add(saved.copy(id = newId))
                } else {
                    val index = currentList.indexOfFirst { it.id == kategori.id }
                    if (index != -1) currentList[index] = saved
                }
                _uiState.value = _uiState.value.copy(data = currentList)
                onSuccess()
            }
        }
    }

    fun hapusKategori(id: Long) {
        viewModelScope.launch {
            repository.hapusKategori(id).onSuccess {
                val updated = _uiState.value.data.filterNot { it.id == id }
                _uiState.value = _uiState.value.copy(data = updated)
            }
        }
    }
}