package com.example.umkmfinance.data.repository

import android.content.Context
import com.example.umkmfinance.data.model.Penjualan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PenjualanRepository(
    private val context: Context
) {

    // Mendapatkan seluruh daftar transaksi penjualan
    suspend fun getDaftarPenjualan(): Result<List<Penjualan>> = withContext(Dispatchers.IO) {
        try {
            // TODO: Integrasikan pemanggilan Retrofit API atau Room DAO
            // Contoh data mock sementara sebelum terhubung API:
            val dummyList = listOf(
                Penjualan(
                    id = 1L,
                    nomor_invoice = "INV-20261108-001",
                    tanggal = "08 Nov 2026 10:15",
                    total_harga = 150000.0,
                    diskon = 0.0,
                    total_bayar = 150000.0,
                    metode_pembayaran = "QRIS",
                    status_pembayaran = "LUNAS"
                ),
                Penjualan(
                    id = 2L,
                    nomor_invoice = "INV-20261108-002",
                    tanggal = "08 Nov 2026 11:30",
                    total_harga = 420000.0,
                    diskon = 20000.0,
                    total_bayar = 400000.0,
                    metode_pembayaran = "TUNAI",
                    status_pembayaran = "LUNAS"
                )
            )
            Result.success(dummyList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Mendapatkan detail satu transaksi penjualan berdasarkan ID
    suspend fun getDetailPenjualan(id: Long): Result<Penjualan> = withContext(Dispatchers.IO) {
        try {
            // TODO: Integrasikan pemanggilan API detail / Room DAO
            val detail = Penjualan(
                id = id,
                nomor_invoice = "INV-20261108-00$id",
                tanggal = "08 Nov 2026 10:15",
                total_harga = 150000.0,
                diskon = 0.0,
                total_bayar = 150000.0,
                metode_pembayaran = "QRIS",
                status_pembayaran = "LUNAS"
            )
            Result.success(detail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Menyimpan transaksi kasir/POS baru
    suspend fun simpanPenjualan(penjualan: Penjualan): Result<Penjualan> = withContext(Dispatchers.IO) {
        try {
            // TODO: Kirim request POST ke endpoint Node.js / Room DAO
            // val response = apiService.createPenjualan(penjualan)
            Result.success(penjualan)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}