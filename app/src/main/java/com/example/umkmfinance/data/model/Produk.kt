package com.example.umkmfinance.data.model

data class ProdukResponse(
    val success: Boolean = false,
    val message: String = "",
    val data: List<Produk>? = null
)

data class ProdukDetailResponse(
    val success: Boolean = false,
    val message: String = "",
    val data: Produk? = null
)

data class Produk(
    val id: Long = 0,
    val usaha_id: Long = 0,
    val kategori_id: Long = 0,
    val kode_produk: String = "",
    val barcode: String? = null,
    val nama: String = "",
    val harga_modal: Double = 0.0,
    val harga_jual: Double = 0.0,
    val stok: Int = 0,
    val satuan: String = "Pcs",
    val status: String = "ACTIVE"
)