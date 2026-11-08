package com.example.umkmfinance.data.model

data class Piutang(
    val id: Long = 0,
    val usaha_id: Long = 0,
    val pelanggan_id: Long = 0,
    val nomor_referensi: String? = null,
    val nominal: Double = 0.0,
    val sisa_piutang: Double = 0.0,
    val tanggal_transaksi: String? = null,
    val jatuh_tempo: String? = null,
    val status: String = "BELUM_LUNAS", // BELUM_LUNAS, SEBAGIAN, LUNAS
    val keterangan: String? = null,
    val pelanggan: Pelanggan? = null
)