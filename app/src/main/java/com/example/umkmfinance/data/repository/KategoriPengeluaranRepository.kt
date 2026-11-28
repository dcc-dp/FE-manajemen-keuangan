package com.example.umkmfinance.data.repository

import android.content.Context
import com.example.umkmfinance.data.model.KategoriPengeluaran
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KategoriPengeluaranRepository(
    private val context: Context
) {

    suspend fun getDaftarKategoriPengeluaran(
        usahaId: Long
    ): Result<List<KategoriPengeluaran>> = withContext(Dispatchers.IO) {
        try {
            // TODO: Integrasikan pemanggilan Retrofit API atau Room DAO berdasarkan usahaId
            val dummyList = listOf(
                KategoriPengeluaran(id = 1L, usaha_id = usahaId, nama = "Listrik & Air"),
                KategoriPengeluaran(id = 2L, usaha_id = usahaId, nama = "Gaji Karyawan"),
                KategoriPengeluaran(id = 3L, usaha_id = usahaId, nama = "Kemasan & Perlengkapan"),
                KategoriPengeluaran(id = 4L, usaha_id = usahaId, nama = "Transportasi & Kirim")
            )
            Result.success(dummyList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun simpanKategoriPengeluaran(
        usahaId: Long,
        nama: String
    ): Result<KategoriPengeluaran> = withContext(Dispatchers.IO) {
        try {
            // TODO: Kirim request POST ke endpoint backend / Room DAO
            val itemBaru = KategoriPengeluaran(
                id = System.currentTimeMillis(),
                usaha_id = usahaId,
                nama = nama.trim()
            )
            Result.success(itemBaru)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}