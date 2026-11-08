package com.example.umkmfinance.data.model

data class Pembelian(
    val id: Long = 0,
    val usaha_id: Long = 0,
    val supplier_id: Long = 0,
    val nomor_faktur: String? = null,
    val tanggal: String? = null,
    val total: Double = 0.0,
    val status: String? = null,
    val metode_pembayaran: String? = null,
    val catatan: String? = null,
    val supplier: Supplier? = null,
    val detail_pembelian: List<DetailPembelianItem>? = null
)

data class DetailPembelianItem(
    val id: Long = 0,
    val pembelian_id: Long = 0,
    val produk_id: Long = 0,
    val nama_produk: String? = null,
    val jumlah: Int = 0,
    val harga_beli: Double = 0.0,
    val subtotal: Double = 0.0
)