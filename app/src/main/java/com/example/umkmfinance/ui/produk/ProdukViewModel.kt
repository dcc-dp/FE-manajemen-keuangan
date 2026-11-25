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
    val kategoriId: Long = 0L,
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

    // Menyimpan seluruh data produk dari server sebagai sumber filter lokal
    private var semuaProdukCache: List<Produk> = emptyList()

    // ==============================
    // STATE DAFTAR PRODUK
    // ==============================
    private val _uiState = MutableStateFlow(ProdukUiState())
    val uiState: StateFlow<ProdukUiState> = _uiState.asStateFlow()

    // ==============================
    // STATE DETAIL PRODUK
    // ==============================
    private val _detailUiState = MutableStateFlow(DetailProdukUiState())
    val detailUiState: StateFlow<DetailProdukUiState> = _detailUiState.asStateFlow()

    // ==============================
    // AMBIL & FILTER PRODUK BERDASARKAN KATEGORI
    // ==============================
    fun loadProduk(kategoriId: Long = 0L) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                kategoriId = kategoriId,
                errorMessage = null
            )

            try {
                val response = repository.getProduk()

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body?.success == true) {
                        semuaProdukCache = body.data.orEmpty()
                        val hasilFilter = terapkanFilter(semuaProdukCache, kategoriId)

                        _uiState.value = ProdukUiState(
                            isLoading = false,
                            data = hasilFilter,
                            kategoriId = kategoriId
                        )
                    } else {
                        _uiState.value = ProdukUiState(
                            isLoading = false,
                            kategoriId = kategoriId,
                            errorMessage = body?.message ?: "Data produk tidak ditemukan"
                        )
                    }
                } else {
                    _uiState.value = ProdukUiState(
                        isLoading = false,
                        kategoriId = kategoriId,
                        errorMessage = when (response.code()) {
                            401 -> "Sesi login sudah tidak valid"
                            403 -> "Anda tidak memiliki akses produk"
                            else -> "Gagal mengambil produk (${response.code()})"
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ProdukUiState(
                    isLoading = false,
                    kategoriId = kategoriId,
                    errorMessage = "Tidak dapat terhubung ke server"
                )
            }
        }
    }

    // Filter ulang dari cache (misal saat pengguna menekan chip "Lihat Semua Produk" / kategoriId = 0L)
    fun filterByKategori(kategoriId: Long) {
        val hasilFilter = terapkanFilter(semuaProdukCache, kategoriId)
        _uiState.value = _uiState.value.copy(
            data = hasilFilter,
            kategoriId = kategoriId,
            errorMessage = null
        )
    }

    private fun terapkanFilter(list: List<Produk>, kategoriId: Long): List<Produk> {
        return if (kategoriId == 0L) {
            list
        } else {
            list.filter { it.kategori_id == kategoriId }
        }
    }

    // ==============================
    // AMBIL DETAIL PRODUK
    // ==============================
    fun loadProdukById(id: Long) {
        viewModelScope.launch {
            _detailUiState.value = DetailProdukUiState(isLoading = true)

            try {
                val response = repository.getProdukById(id)

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body?.success == true) {
                        val produk = body.data
                        if (produk != null) {
                            _detailUiState.value = DetailProdukUiState(
                                isLoading = false,
                                data = produk
                            )
                        } else {
                            _detailUiState.value = DetailProdukUiState(
                                isLoading = false,
                                errorMessage = "Produk tidak ditemukan"
                            )
                        }
                    } else {
                        _detailUiState.value = DetailProdukUiState(
                            isLoading = false,
                            errorMessage = body?.message ?: "Data produk tidak ditemukan"
                        )
                    }
                } else {
                    _detailUiState.value = DetailProdukUiState(
                        isLoading = false,
                        errorMessage = when (response.code()) {
                            401 -> "Sesi login sudah tidak valid"
                            403 -> "Anda tidak memiliki akses produk"
                            404 -> "Produk tidak ditemukan"
                            else -> "Gagal mengambil produk (${response.code()})"
                        }
                    )
                }
            } catch (e: Exception) {
                _detailUiState.value = DetailProdukUiState(
                    isLoading = false,
                    errorMessage = "Gagal membaca data detail produk: ${e.message}"
                )
            }
        }
    }

    // ==============================
    // PERBARUI PRODUK (EDIT)
    // ==============================
    fun updateProduk(
        id: Long,
        produk: Produk,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val response = repository.updateProduk(id = id, produk = produk)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {
                        val produkTerbaru = body.data ?: produk
                        _detailUiState.value = DetailProdukUiState(
                            isLoading = false,
                            data = produkTerbaru
                        )
                        semuaProdukCache = semuaProdukCache.map { item ->
                            if (item.id == id) produkTerbaru else item
                        }
                        _uiState.value = _uiState.value.copy(
                            data = terapkanFilter(semuaProdukCache, _uiState.value.kategoriId)
                        )
                        onSuccess()
                    } else {
                        val pesan = body?.message ?: "Gagal memperbarui produk"
                        _detailUiState.value = DetailProdukUiState(
                            isLoading = false,
                            data = produk,
                            errorMessage = pesan
                        )
                        onError(pesan)
                    }
                } else {
                    val pesan = when (response.code()) {
                        401 -> "Sesi login sudah tidak valid"
                        403 -> "Anda tidak memiliki akses mengubah produk"
                        404 -> "Produk tidak ditemukan"
                        else -> "Gagal memperbarui produk (${response.code()})"
                    }
                    _detailUiState.value = DetailProdukUiState(
                        isLoading = false,
                        data = produk,
                        errorMessage = pesan
                    )
                    onError(pesan)
                }
            } catch (e: Exception) {
                val pesan = "Tidak dapat terhubung ke server: ${e.message}"
                _detailUiState.value = DetailProdukUiState(
                    isLoading = false,
                    data = produk,
                    errorMessage = pesan
                )
                onError(pesan)
            }
        }
    }

    // ==============================
    // HAPUS PRODUK
    // ==============================
    fun hapusProduk(
        id: Long,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val response = repository.hapusProduk(id = id)

                if (response.isSuccessful && response.body()?.success == true) {
                    semuaProdukCache = semuaProdukCache.filterNot { it.id == id }
                    _uiState.value = _uiState.value.copy(
                        data = terapkanFilter(semuaProdukCache, _uiState.value.kategoriId)
                    )
                    _detailUiState.value = DetailProdukUiState(isLoading = false)
                    onSuccess()
                } else {
                    val pesan = response.body()?.message ?: "Gagal menghapus produk (${response.code()})"
                    _detailUiState.value = _detailUiState.value.copy(
                        isLoading = false,
                        errorMessage = pesan
                    )
                    onError(pesan)
                }
            } catch (e: Exception) {
                val pesan = "Tidak dapat terhubung ke server: ${e.message}"
                _detailUiState.value = _detailUiState.value.copy(
                    isLoading = false,
                    errorMessage = pesan
                )
                onError(pesan)
            }
        }
    }

    // ==============================
    // RESET ERROR DAFTAR PRODUK
    // ==============================
    fun resetError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    // ==============================
    // RESET ERROR DETAIL PRODUK
    // ==============================
    fun resetDetailError() {
        _detailUiState.value = _detailUiState.value.copy(errorMessage = null)
    }
}