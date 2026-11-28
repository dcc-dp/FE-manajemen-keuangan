package com.example.umkmfinance.data.model

data class Pembelian(
    val id: Long = 0L,
    val usaha_id: Long = 0L,
    val supplier_id: Long = 0L,
    val rekening_id: Long = 1L,
    val nomor_faktur: String? = null,
    val tanggal: String? = null,
    val subtotal: Double = 0.0,
    val diskon: Double = 0.0,
    val pajak: Double = 0.0,
    val ongkir: Double = 0.0,
    val total: Double = 0.0,
    val metode_pembayaran: String? = null,
    val status: String? = null,
    val catatan: String? = null,
    val created_by: Long = 1L,
    val created_at: String? = null,
    val updated_at: String? = null,
    val supplier: Supplier? = null,
    val detail_pembelian: List<DetailPembelianItem>? = null
)

data class DetailPembelianItem(
    val id: Long = 0L,
    val pembelian_id: Long = 0L,
    val produk_id: Long = 0L,
    val nama_produk: String? = null,
    val jumlah: Int = 0,
    val harga_beli: Double = 0.0,
    val subtotal: Double = 0.0
)