package com.example.umkmfinance.data.repository

import android.content.Context
import com.example.umkmfinance.data.model.Rekening
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RekeningRepository(
    private val context: Context
) {

    suspend fun getDaftarRekening(): Result<List<Rekening>> = withContext(Dispatchers.IO) {
        try {
            // TODO: Integrasikan ke endpoint API Retrofit atau Room DAO
            Result.success(emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetailRekening(id: Long): Result<Rekening> = withContext(Dispatchers.IO) {
        try {
            // TODO: Integrasikan ke endpoint API Retrofit atau Room DAO
            Result.failure(Exception("Data rekening tidak ditemukan"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}