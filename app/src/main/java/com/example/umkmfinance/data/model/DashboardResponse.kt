package com.example.umkmfinance.data.model

data class DashboardResponse(
    val success: Boolean,
    val message: String,
    val data: DashboardData?
)

data class DashboardData(
    val ringkasan: DashboardRingkasan,
    val bulan_berjalan: DashboardBulanBerjalan,
    val stok_rendah: List<StokRendah>?,
    val transaksi_kas_terbaru: List<TransaksiKasTerbaru>?
)
data class DashboardRingkasan(
    val total_pendapatan: Double,
    val total_hpp: Double,
    val laba_kotor: Double,
    val total_pengeluaran: Double,
    val laba_bersih: Double,
    val total_piutang: Double,
    val total_hutang: Double,
    val total_saldo: Double
)

data class DashboardBulanBerjalan(
    val total_pendapatan: Double,
    val total_hpp: Double,
    val laba_kotor: Double,
    val total_pengeluaran: Double,
    val laba_bersih: Double
)

data class StokRendah(
    val id: Long,
    val nama: String,
    val stok: Int,
    val satuan: String
)

data class TransaksiKasTerbaru(
    val id: Long,
    val referensi_tipe: String,
    val nomor_referensi: String?,
    val jenis: String,
    val nominal: Double,
    val saldo_sebelum: Double,
    val saldo_setelah: Double,
    val tanggal: String,
    val keterangan: String?
)