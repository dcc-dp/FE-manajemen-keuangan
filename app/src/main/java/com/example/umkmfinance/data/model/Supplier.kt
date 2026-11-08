package com.example.umkmfinance.data.model

data class SupplierResponse(
    val success: Boolean = false,
    val message: String = "",
    val data: List<Supplier>? = null
)

data class SupplierDetailResponse(
    val success: Boolean = false,
    val message: String = "",
    val data: Supplier? = null
)

data class Supplier(
    val id: Long = 0,
    val usaha_id: Long = 0,
    val nama: String = "",
    val telepon: String? = null,
    val email: String? = null,
    val alamat: String? = null,
    val nama_kontak: String? = null,
    val catatan: String? = null
)