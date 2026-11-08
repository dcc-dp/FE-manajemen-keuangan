package com.example.umkmfinance.ui.supplier

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.Supplier
import com.example.umkmfinance.data.repository.SupplierRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SupplierUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: List<Supplier> = emptyList()
)

data class DetailSupplierUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: Supplier? = null
)

class SupplierViewModel(
    private val context: Context
) : ViewModel() {

    private val repository = SupplierRepository(context)

    private val _uiState = MutableStateFlow(SupplierUiState())
    val uiState: StateFlow<SupplierUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(DetailSupplierUiState())
    val detailUiState: StateFlow<DetailSupplierUiState> = _detailUiState.asStateFlow()

    fun loadSupplier() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getDaftarSupplier()
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(isLoading = false, data = list)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = error.localizedMessage ?: "Gagal memuat supplier")
            }
        }
    }

    fun loadSupplierById(id: Long) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getDetailSupplier(id)
            result.onSuccess { supplier ->
                _detailUiState.value = _detailUiState.value.copy(isLoading = false, data = supplier)
            }.onFailure { error ->
                _detailUiState.value = _detailUiState.value.copy(isLoading = false, errorMessage = error.localizedMessage ?: "Gagal memuat detail supplier")
            }
        }
    }

    fun hapusSupplier(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = repository.hapusSupplier(id)
            result.onSuccess {
                loadSupplier()
                onSuccess()
            }
        }
    }
}