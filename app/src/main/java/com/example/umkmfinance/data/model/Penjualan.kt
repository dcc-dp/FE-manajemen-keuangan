package com.example.umkmfinance.data.model

import com.google.gson.annotations.SerializedName

data class Penjualan(
    val id: Long = 0L,
    val usaha_id: Long = 1L,
    val pelanggan_id: Long? = null,
    val rekening_id: Long = 1L,
    val nomor_invoice: String = "",
    val tanggal: String? = null,
    val subtotal: Double = 0.0,
    val diskon: Double = 0.0,
    val pajak: Double = 0.0,
    val biaya_lain: Double = 0.0,
    @SerializedName(value = "total", alternate = ["total_bayar", "total_harga"])
    val total: Double = 0.0,
    val metode_pembayaran: String = "TUNAI",
    @SerializedName(value = "status", alternate = ["status_pembayaran"])
    val status: String = "LUNAS",
    val catatan: String? = null,
    val created_by: Long = 1L,
    val created_at: String? = null,
    val updated_at: String? = null
)