package com.example.umkmfinance.data.repository

import android.content.Context
import com.example.umkmfinance.data.model.KategoriProduk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KategoriProdukRepository(
    private val context: Context
) {
    suspend fun getDaftarKategori(): Result<List<KategoriProduk>> = withContext(Dispatchers.IO) {
        try {
            val dummyList = listOf(
                KategoriProduk(
                    id = 1L,
                    usaha_id = 1L,
                    nama = "Minuman Kopi",
                    deskripsi = "Varian kopi espresso base, manual brew, dan kopi susu"
                ),
                KategoriProduk(
                    id = 2L,
                    usaha_id = 1L,
                    nama = "Minuman Non-Kopi",
                    deskripsi = "Matcha, cokelat, teh tarik, dan minuman segar lainnya"
                ),
                KategoriProduk(
                    id = 3L,
                    usaha_id = 1L,
                    nama = "Makanan Berat",
                    deskripsi = "Menu utama nasi, mie, dan hidangan hangat"
                ),
                KategoriProduk(
                    id = 4L,
                    usaha_id = 1L,
                    nama = "Snack & Camilan",
                    deskripsi = "Kue basah, roti bakar, dan gorengan pendamping"
                )
            )
            Result.success(dummyList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun simpanKategori(kategori: KategoriProduk): Result<KategoriProduk> = withContext(Dispatchers.IO) {
        try {
            Result.success(kategori)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hapusKategori(id: Long): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}