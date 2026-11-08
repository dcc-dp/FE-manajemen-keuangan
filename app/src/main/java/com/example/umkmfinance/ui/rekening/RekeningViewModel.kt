package com.example.umkmfinance.ui.rekening

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.Rekening
import com.example.umkmfinance.data.repository.RekeningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RekeningListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: List<Rekening> = emptyList()
)

data class DetailRekeningUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: Rekening? = null
)

class RekeningViewModel(
    context: Context
) : ViewModel() {

    private val repository = RekeningRepository(context)

    private val _uiState = MutableStateFlow(RekeningListUiState())
    val uiState: StateFlow<RekeningListUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(DetailRekeningUiState())
    val detailUiState: StateFlow<DetailRekeningUiState> = _detailUiState.asStateFlow()

    fun loadRekening() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getDaftarRekening()
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    data = list
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Gagal memuat data rekening"
                )
            }
        }
    }

    fun loadRekeningById(id: Long) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getDetailRekening(id)
            result.onSuccess { detail ->
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    data = detail
                )
            }.onFailure { error ->
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Gagal memuat detail rekening"
                )
            }
        }
    }
}