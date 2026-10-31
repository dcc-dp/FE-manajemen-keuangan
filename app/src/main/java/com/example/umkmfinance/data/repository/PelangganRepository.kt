package com.example.umkmfinance.data.repository

import com.example.umkmfinance.data.model.PelangganDetailResponse
import com.example.umkmfinance.data.model.PelangganResponse
import com.example.umkmfinance.data.remote.ApiService
import retrofit2.Response

class PelangganRepository(
    private val apiService: ApiService
) {

    suspend fun getPelanggan(): Response<PelangganResponse> {
        return apiService.getPelanggan()
    }

    suspend fun getPelangganById(
        id: Long
    ): Response<PelangganDetailResponse> {
        return apiService.getPelangganById(id)
    }
}