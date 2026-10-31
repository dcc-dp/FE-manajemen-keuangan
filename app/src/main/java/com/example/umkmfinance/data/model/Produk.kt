package com.example.umkmfinance.data.model

data class ProdukResponse(
    val success: Boolean,
    val message: String,
    val data: List<Produk>?
)

data class ProdukDetailResponse(
    val success: Boolean,
    val message: String,
    val data: Produk?
)

data class Produk(
    val id: Long,
    val usaha_id: Long,
    val kategori_id: Long,
    val kode_produk: String,
    val barcode: String?,
    val nama: String,
    val harga_modal: Double,
    val harga_jual: Double,
    val stok: Int,
    val satuan: String,
    val status: String
)