package com.example.umkmfinance.data.model

data class PelangganResponse(
    val success: Boolean,
    val message: String,
    val data: List<Pelanggan>?
)

data class PelangganDetailResponse(
    val success: Boolean,
    val message: String,
    val data: Pelanggan?
)

data class Pelanggan(
    val id: Long,
    val usaha_id: Long,
    val nama: String,
    val telepon: String?,
    val email: String?,
    val alamat: String?,
    val poin: Int
)