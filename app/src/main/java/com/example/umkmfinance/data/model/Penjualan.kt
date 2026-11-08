package com.example.umkmfinance.data.model

data class Penjualan(
    val id: Long = 0,
    val nomor_invoice: String = "",
    val tanggal: String? = null,
    val total_harga: Double = 0.0,
    val diskon: Double = 0.0,
    val total_bayar: Double = 0.0,
    val metode_pembayaran: String = "TUNAI",
    val status_pembayaran: String = "LUNAS"
)