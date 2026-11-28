package com.example.umkmfinance.data.repository

import android.content.Context
import com.example.umkmfinance.data.model.KategoriPengeluaran
import com.example.umkmfinance.data.model.Pengeluaran
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PengeluaranRepository(
    private val context: Context
) {

    suspend fun getDaftarPengeluaran(
        usahaId: Long
    ): Result<List<Pengeluaran>> = withContext(Dispatchers.IO) {
        try {
            // TODO: Integrasikan pemanggilan Retrofit API atau Room DAO berdasarkan usahaId
            val dummyList = listOf(
                Pengeluaran(
                    id = 1L,
                    usaha_id = usahaId,
                    kategori_pengeluaran_id = 1L,
                    rekening_id = 1L,
                    tanggal = "2026-11-28 08:30:00",
                    nominal = 350000.0,
                    keterangan = "Bayar tagihan listrik dan PDAM bulan November",
                    created_by = 1L,
                    kategori_pengeluaran = KategoriPengeluaran(
                        id = 1L,
                        usaha_id = usahaId,
                        nama = "Listrik & Air"
                    )
                ),
                Pengeluaran(
                    id = 2L,
                    usaha_id = usahaId,
                    kategori_pengeluaran_id = 3L,
                    rekening_id = 1L,
                    tanggal = "2026-11-28 11:15:00",
                    nominal = 125000.0,
                    keterangan = "Beli plastik kemasan dan lakban",
                    created_by = 1L,
                    kategori_pengeluaran = KategoriPengeluaran(
                        id = 3L,
                        usaha_id = usahaId,
                        nama = "Kemasan & Perlengkapan"
                    )
                )
            )
            Result.success(dummyList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun simpanPengeluaran(
        pengeluaran: Pengeluaran
    ): Result<Pengeluaran> = withContext(Dispatchers.IO) {
        try {
            // TODO: Kirim request POST ke endpoint backend / Room DAO
            val tersimpan = pengeluaran.copy(
                id = if (pengeluaran.id == 0L) System.currentTimeMillis() else pengeluaran.id
            )
            Result.success(tersimpan)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}