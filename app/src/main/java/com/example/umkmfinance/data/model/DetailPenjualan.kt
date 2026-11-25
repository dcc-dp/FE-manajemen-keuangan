package com.example.umkmfinance.data.model

data class DetailPenjualan(
    val id: Long = 0L,
    val penjualan_id: Long = 0L,
    val produk_id: Long = 0L,
    val qty: Int = 1,
    val harga_modal: Double = 0.0,
    val harga: Double = 0.0,
    val diskon: Double = 0.0,
    val subtotal: Double = 0.0
)