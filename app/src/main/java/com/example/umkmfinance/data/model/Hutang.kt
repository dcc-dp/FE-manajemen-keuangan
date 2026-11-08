package com.example.umkmfinance.data.model

data class Hutang(
    val id: Long = 0,
    val usaha_id: Long = 0,
    val supplier_id: Long = 0,
    val pembelian_id: Long? = null,
    val nomor_referensi: String? = null,
    val nominal: Double = 0.0,
    val sisa_hutang: Double = 0.0,
    val tanggal_transaksi: String? = null,
    val jatuh_tempo: String? = null,
    val status: String = "BELUM_LUNAS", // BELUM_LUNAS, SEBAGIAN, LUNAS
    val catatan: String? = null,
    val supplier: Supplier? = null
)