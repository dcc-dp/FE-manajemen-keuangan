package com.example.umkmfinance.data.repository

import android.content.Context
import com.example.umkmfinance.data.model.DetailPenjualan
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
            val dummyList = listOf(
                Penjualan(
                    id = 1L,
                    usaha_id = 1L,
                    pelanggan_id = 1L,
                    rekening_id = 1L,
                    nomor_invoice = "INV-20261125-001",
                    tanggal = "2026-11-25 10:15:00",
                    subtotal = 150000.0,
                    diskon = 0.0,
                    pajak = 0.0,
                    biaya_lain = 0.0,
                    total = 150000.0,
                    metode_pembayaran = "QRIS",
                    status = "LUNAS",
                    catatan = "Pembayaran QRIS berhasil",
                    created_by = 1L
                ),
                Penjualan(
                    id = 2L,
                    usaha_id = 1L,
                    pelanggan_id = null,
                    rekening_id = 1L,
                    nomor_invoice = "INV-20261125-002",
                    tanggal = "2026-11-25 10:45:00",
                    subtotal = 420000.0,
                    diskon = 20000.0,
                    pajak = 0.0,
                    biaya_lain = 0.0,
                    total = 400000.0,
                    metode_pembayaran = "TUNAI",
                    status = "PIUTANG",
                    catatan = "Cicilan tahap 1",
                    created_by = 1L
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
                usaha_id = 1L,
                pelanggan_id = 1L,
                rekening_id = 1L,
                nomor_invoice = "INV-20261125-00$id",
                tanggal = "2026-11-25 10:15:00",
                subtotal = 150000.0,
                diskon = 0.0,
                pajak = 0.0,
                biaya_lain = 0.0,
                total = 150000.0,
                metode_pembayaran = "QRIS",
                status = "LUNAS",
                catatan = "Detail transaksi penjualan",
                created_by = 1L
            )
            Result.success(detail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Mendapatkan daftar item detail_penjualan berdasarkan penjualan_id
    suspend fun getDetailItemPenjualan(penjualanId: Long): Result<List<DetailPenjualan>> =
        withContext(Dispatchers.IO) {
            try {
                // TODO: Integrasikan pemanggilan Retrofit API atau Room DAO berdasarkan penjualanId
                val dummyItems = listOf(
                    DetailPenjualan(
                        id = 1L,
                        penjualan_id = penjualanId,
                        produk_id = 101L,
                        qty = 2,
                        harga_modal = 50000.0,
                        harga = 75000.0,
                        diskon = 0.0,
                        subtotal = 150000.0
                    )
                )
                Result.success(dummyItems)
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