package com.example.umkmfinance.data.repository

import android.content.Context
import com.example.umkmfinance.data.model.Hutang
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HutangRepository(
    private val context: Context
) {

    suspend fun getDaftarHutang(): Result<List<Hutang>> = withContext(Dispatchers.IO) {
        try {
            // TODO: Integrasikan ke endpoint API Retrofit atau Room DAO lokal
            Result.success(emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetailHutang(id: Long): Result<Hutang> = withContext(Dispatchers.IO) {
        try {
            // TODO: Integrasikan ke endpoint API Retrofit atau Room DAO lokal
            Result.failure(Exception("Data hutang tidak ditemukan"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}