package com.example.umkmfinance.data.model

data class KategoriProdukResponse(
    val success: Boolean = false,
    val message: String = "",
    val data: List<KategoriProduk>? = null
)

data class KategoriProdukDetailResponse(
    val success: Boolean = false,
    val message: String = "",
    val data: KategoriProduk? = null
)

data class KategoriProduk(
    val id: Long = 0,
    val usaha_id: Long = 0,
    val nama: String = "",
    val deskripsi: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)