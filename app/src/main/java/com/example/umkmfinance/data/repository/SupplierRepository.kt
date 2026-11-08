package com.example.umkmfinance.data.repository

import android.content.Context
import com.example.umkmfinance.data.model.Supplier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SupplierRepository(
    private val context: Context
) {
    suspend fun getDaftarSupplier(): Result<List<Supplier>> = withContext(Dispatchers.IO) {
        try {
            val dummyList = listOf(
                Supplier(
                    id = 1L,
                    usaha_id = 1L,
                    nama = "Agen Kopi Nusantara Makassar",
                    nama_kontak = "Pak Rian (Sales)",
                    telepon = "081234567891",
                    email = "sales@kopinusantara.id",
                    alamat = "Jl. Perintis Kemerdekaan KM 9, Makassar",
                    catatan = "BCA 8273648122 a/n Rian. Minimal order 10 kg, kirim tiap Selasa & Jumat."
                ),
                Supplier(
                    id = 2L,
                    usaha_id = 1L,
                    nama = "Distributor Susu & Sirup Berkah",
                    nama_kontak = "Ibu Maya",
                    telepon = "085299887766",
                    email = "distribusi.berkah@gmail.com",
                    alamat = "Kompleks Pergudangan Parangloe Blok D",
                    catatan = "Mandiri 1520038472911. Diskon 5% jika bayar tunai di tempat."
                ),
                Supplier(
                    id = 3L,
                    usaha_id = 1L,
                    nama = "Toko Kemasan & Cup Plastik Sentosa",
                    nama_kontak = "Ko Hendra",
                    telepon = "082190001122",
                    email = null,
                    alamat = "Pasar Butung Lantai 1",
                    catatan = "Langganan cup sealer & sedotan steril."
                )
            )
            Result.success(dummyList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetailSupplier(id: Long): Result<Supplier> = withContext(Dispatchers.IO) {
        try {
            val supplier = Supplier(
                id = id,
                usaha_id = 1L,
                nama = "Agen Kopi Nusantara Makassar",
                nama_kontak = "Pak Rian (Sales)",
                telepon = "081234567891",
                email = "sales@kopinusantara.id",
                alamat = "Jl. Perintis Kemerdekaan KM 9, Makassar",
                catatan = "BCA 8273648122 a/n Rian. Minimal order 10 kg, kirim tiap Selasa & Jumat."
            )
            Result.success(supplier)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun simpanSupplier(supplier: Supplier): Result<Supplier> = withContext(Dispatchers.IO) {
        try {
            Result.success(supplier)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hapusSupplier(id: Long): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}