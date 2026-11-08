package com.example.umkmfinance.data.repository

import android.content.Context
import com.example.umkmfinance.data.model.Piutang
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PiutangRepository(
    private val context: Context
) {

    suspend fun getDaftarPiutang(): Result<List<Piutang>> = withContext(Dispatchers.IO) {
        try {
            // TODO: Hubungkan ke ApiService Retrofit atau Room DAO
            Result.success(emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetailPiutang(id: Long): Result<Piutang> = withContext(Dispatchers.IO) {
        try {
            // TODO: Hubungkan ke ApiService Retrofit atau Room DAO
            Result.failure(Exception("Data piutang tidak ditemukan"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}