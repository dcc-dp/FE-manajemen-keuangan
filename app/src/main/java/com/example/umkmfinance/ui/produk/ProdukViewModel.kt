package com.example.umkmfinance.ui.produk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umkmfinance.data.model.Produk
import com.example.umkmfinance.data.repository.ProdukRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProdukUiState(
    val isLoading: Boolean = false,
    val data: List<Produk> = emptyList(),
    val errorMessage: String? = null
)

data class DetailProdukUiState(
    val isLoading: Boolean = false,
    val data: Produk? = null,
    val errorMessage: String? = null
)

class ProdukViewModel(
    private val repository: ProdukRepository
) : ViewModel() {

    // ==============================
    // STATE DAFTAR PRODUK
    // ==============================

    private val _uiState = MutableStateFlow(
        ProdukUiState()
    )

    val uiState: StateFlow<ProdukUiState> =
        _uiState.asStateFlow()


    // ==============================
    // STATE DETAIL PRODUK
    // ==============================

    private val _detailUiState = MutableStateFlow(
        DetailProdukUiState()
    )

    val detailUiState: StateFlow<DetailProdukUiState> =
        _detailUiState.asStateFlow()


    // ==============================
    // AMBIL SEMUA PRODUK
    // ==============================

    fun loadProduk() {
        viewModelScope.launch {

            _uiState.value = ProdukUiState(
                isLoading = true
            )

            try {

                val response = repository.getProduk()

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body?.success == true) {

                        _uiState.value = ProdukUiState(
                            isLoading = false,
                            data = body.data.orEmpty()
                        )

                    } else {

                        _uiState.value = ProdukUiState(
                            isLoading = false,
                            errorMessage = body?.message
                                ?: "Data produk tidak ditemukan"
                        )
                    }

                } else {

                    _uiState.value = ProdukUiState(
                        isLoading = false,
                        errorMessage = when (response.code()) {

                            401 ->
                                "Sesi login sudah tidak valid"

                            403 ->
                                "Anda tidak memiliki akses produk"

                            else ->
                                "Gagal mengambil produk (${response.code()})"
                        }
                    )
                }

            } catch (e: Exception) {

                _uiState.value = ProdukUiState(
                    isLoading = false,
                    errorMessage = "Tidak dapat terhubung ke server"
                )
            }
        }
    }


    // ==============================
    // AMBIL DETAIL PRODUK
    // ==============================
    fun loadProdukById(id: Long) {
        viewModelScope.launch {
            _detailUiState.value = DetailProdukUiState(
                isLoading = true
            )

            try {
                val response = repository.getProdukById(id)

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body?.success == true) {

                        val produk = body.data

                        if (produk != null) {
                            _detailUiState.value =
                                DetailProdukUiState(
                                    isLoading = false,
                                    data = produk
                                )
                        } else {
                            _detailUiState.value =
                                DetailProdukUiState(
                                    isLoading = false,
                                    errorMessage = "Produk tidak ditemukan"
                                )
                        }

                    } else {
                        _detailUiState.value =
                            DetailProdukUiState(
                                isLoading = false,
                                errorMessage =
                                    body?.message
                                        ?: "Data produk tidak ditemukan"
                            )
                    }

                } else {

                    _detailUiState.value =
                        DetailProdukUiState(
                            isLoading = false,
                            errorMessage = when (response.code()) {
                                401 -> "Sesi login sudah tidak valid"
                                403 -> "Anda tidak memiliki akses produk"
                                404 -> "Produk tidak ditemukan"
                                else ->
                                    "Gagal mengambil produk (${response.code()})"
                            }
                        )
                }

            } catch (e: Exception) {

                _detailUiState.value =
                    DetailProdukUiState(
                        isLoading = false,
                        errorMessage =
                            "Gagal membaca data detail produk: ${e.message}"
                    )
            }
        }
    }


    // ==============================
    // RESET ERROR DAFTAR PRODUK
    // ==============================

    fun resetError() {

        _uiState.value =
            _uiState.value.copy(
                errorMessage = null
            )
    }


    // ==============================
    // RESET ERROR DETAIL PRODUK
    // ==============================

    fun resetDetailError() {

        _detailUiState.value =
            _detailUiState.value.copy(
                errorMessage = null
            )
    }
}