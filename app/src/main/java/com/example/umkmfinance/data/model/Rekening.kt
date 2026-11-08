package com.example.umkmfinance.data.model

data class Rekening(
    val id: Long = 0,
    val usaha_id: Long = 0,
    val nama_bank: String = "",       // Contoh: BCA, Mandiri, Kas Tunai Toko
    val nomor_rekening: String? = null,
    val atas_nama: String? = null,
    val tipe: String = "BANK",        // BANK, KAS_TUNAI, E_WALLET
    val saldo: Double = 0.0,
    val status_aktif: Boolean = true,
    val catatan: String? = null
)