package com.example.umkmfinance.data.repository

import android.content.Context
import com.example.umkmfinance.data.model.Pengeluaran
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PengeluaranRepository(
    private val context: Context
) {

    suspend fun getDaftarPengeluaran(): Result<List<Pengeluaran>> = withContext(Dispatchers.IO) {
        try {
            // TODO: Integrasikan dengan ApiService atau Room DAO
            Result.success(emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetailPengeluaran(id: Long): Result<Pengeluaran> = withContext(Dispatchers.IO) {
        try {
            // TODO: Integrasikan dengan ApiService atau Room DAO
            Result.failure(Exception("Data pengeluaran tidak ditemukan"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}