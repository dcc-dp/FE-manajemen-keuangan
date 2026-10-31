package com.example.umkmfinance.ui.pelanggan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.Pelanggan
import com.example.umkmfinance.data.repository.PelangganRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PelangganUiState(
    val isLoading: Boolean = false,
    val data: List<Pelanggan> = emptyList(),
    val errorMessage: String? = null
)

data class DetailPelangganUiState(
    val isLoading: Boolean = false,
    val data: Pelanggan? = null,
    val errorMessage: String? = null
)

class PelangganViewModel(
    private val repository: PelangganRepository
) : ViewModel() {

    // ==========================================
    // STATE DAFTAR PELANGGAN
    // ==========================================

    private val _uiState = MutableStateFlow(
        PelangganUiState()
    )

    val uiState: StateFlow<PelangganUiState> =
        _uiState.asStateFlow()


    // ==========================================
    // STATE DETAIL PELANGGAN
    // ==========================================

    private val _detailUiState = MutableStateFlow(
        DetailPelangganUiState()
    )

    val detailUiState: StateFlow<DetailPelangganUiState> =
        _detailUiState.asStateFlow()


    // ==========================================
    // LOAD DAFTAR PELANGGAN
    // ==========================================

    fun loadPelanggan() {

        viewModelScope.launch {

            _uiState.value = PelangganUiState(
                isLoading = true
            )

            try {

                val response =
                    repository.getPelanggan()

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body?.success == true) {

                        _uiState.value =
                            PelangganUiState(
                                isLoading = false,
                                data = body.data.orEmpty()
                            )

                    } else {

                        _uiState.value =
                            PelangganUiState(
                                isLoading = false,
                                errorMessage =
                                    body?.message
                                        ?: "Data pelanggan tidak ditemukan"
                            )
                    }

                } else {

                    _uiState.value =
                        PelangganUiState(
                            isLoading = false,
                            errorMessage =
                                when (response.code()) {

                                    401 ->
                                        "Sesi login sudah tidak valid"

                                    403 ->
                                        "Anda tidak memiliki akses pelanggan"

                                    404 ->
                                        "Data pelanggan tidak ditemukan"

                                    else ->
                                        "Gagal mengambil pelanggan (${response.code()})"
                                }
                        )
                }

            } catch (e: Exception) {

                _uiState.value =
                    PelangganUiState(
                        isLoading = false,
                        errorMessage =
                            "Tidak dapat terhubung ke server"
                    )
            }
        }
    }


    // ==========================================
    // LOAD DETAIL PELANGGAN
    // ==========================================

    fun loadPelangganById(id: Long) {

        viewModelScope.launch {

            _detailUiState.value =
                DetailPelangganUiState(
                    isLoading = true
                )

            try {

                val response =
                    repository.getPelangganById(id)

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body?.success == true) {

                        val pelanggan =
                            body.data

                        if (pelanggan != null) {

                            _detailUiState.value =
                                DetailPelangganUiState(
                                    isLoading = false,
                                    data = pelanggan
                                )

                        } else {

                            _detailUiState.value =
                                DetailPelangganUiState(
                                    isLoading = false,
                                    errorMessage =
                                        "Pelanggan tidak ditemukan"
                                )
                        }

                    } else {

                        _detailUiState.value =
                            DetailPelangganUiState(
                                isLoading = false,
                                errorMessage =
                                    body?.message
                                        ?: "Data pelanggan tidak ditemukan"
                            )
                    }

                } else {

                    _detailUiState.value =
                        DetailPelangganUiState(
                            isLoading = false,
                            errorMessage =
                                when (response.code()) {

                                    401 ->
                                        "Sesi login sudah tidak valid"

                                    403 ->
                                        "Anda tidak memiliki akses pelanggan"

                                    404 ->
                                        "Pelanggan tidak ditemukan"

                                    else ->
                                        "Gagal mengambil pelanggan (${response.code()})"
                                }
                        )
                }

            } catch (e: Exception) {

                _detailUiState.value =
                    DetailPelangganUiState(
                        isLoading = false,
                        errorMessage =
                            "Gagal membaca data detail pelanggan: ${e.message}"
                    )
            }
        }
    }
}