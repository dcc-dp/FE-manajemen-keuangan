package com.example.umkmfinance.data.repository

import android.content.Context
import com.example.umkmfinance.data.model.Rekening
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RekeningRepository(
    private val context: Context
) {

    suspend fun getDaftarRekening(
        usahaId: Long
    ): Result<List<Rekening>> = withContext(Dispatchers.IO) {
        try {
            // TODO: Integrasikan pemanggilan Retrofit API atau Room DAO berdasarkan usahaId
            val dummyList = listOf(
                Rekening(
                    id = 1L,
                    usaha_id = usahaId,
                    nama_rekening = "Kas Tunai Toko",
                    bank = "KAS",
                    nomor_rekening = null,
                    atas_nama = "Kasir / Toko",
                    saldo_awal = 1000000.0,
                    saldo = 2450000.0
                ),
                Rekening(
                    id = 2L,
                    usaha_id = usahaId,
                    nama_rekening = "Rekening Operasional",
                    bank = "BRI",
                    nomor_rekening = "012345678901234",
                    atas_nama = "Usaha Utama",
                    saldo_awal = 5000000.0,
                    saldo = 12850000.0
                )
            )
            Result.success(dummyList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun simpanRekening(
        rekening: Rekening
    ): Result<Rekening> = withContext(Dispatchers.IO) {
        try {
            // TODO: Kirim request POST ke endpoint backend / Room DAO
            val tersimpan = rekening.copy(
                id = if (rekening.id == 0L) System.currentTimeMillis() else rekening.id,
                saldo = if (rekening.saldo == 0.0 && rekening.saldo_awal > 0.0) {
                    rekening.saldo_awal
                } else {
                    rekening.saldo
                }
            )
            Result.success(tersimpan)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}