package com.example.umkmfinance.data.model

data class Rekening(
    val id: Long = 0L,
    val usaha_id: Long = 0L,
    val nama_rekening: String = "",
    val bank: String = "",
    val nomor_rekening: String? = null,
    val atas_nama: String? = null,
    val saldo_awal: Double = 0.0,
    val saldo: Double = 0.0,
    val created_at: String? = null,
    val updated_at: String? = null
)