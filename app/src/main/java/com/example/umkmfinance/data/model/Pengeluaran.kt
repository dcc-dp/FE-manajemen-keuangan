package com.example.umkmfinance.data.model

data class Pengeluaran(
    val id: Long = 0L,
    val usaha_id: Long = 0L,
    val kategori_pengeluaran_id: Long = 0L,
    val rekening_id: Long = 0L,
    val tanggal: String = "",
    val nominal: Double = 0.0,
    val keterangan: String? = null,
    val created_by: Long = 0L,
    val created_at: String? = null,
    val updated_at: String? = null,
    val kategori_pengeluaran: KategoriPengeluaran? = null
)