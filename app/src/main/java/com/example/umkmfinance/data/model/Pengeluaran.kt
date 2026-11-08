package com.example.umkmfinance.data.model

data class Pengeluaran(
    val id: Long = 0,
    val usaha_id: Long = 0,
    val kategori_pengeluaran_id: Long = 0,
    val judul: String = "",
    val nominal: Double = 0.0,
    val tanggal: String? = null,
    val keterangan: String? = null,
    val metode_pembayaran: String? = null,
    val bukti_nota: String? = null
)