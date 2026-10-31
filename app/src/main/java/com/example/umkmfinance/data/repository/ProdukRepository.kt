package com.example.umkmfinance.data.repository

import com.example.umkmfinance.data.model.ProdukDetailResponse
import com.example.umkmfinance.data.model.ProdukResponse
import com.example.umkmfinance.data.remote.ApiService
import retrofit2.Response

class ProdukRepository(
    private val apiService: ApiService
) {

    suspend fun getProduk(): Response<ProdukResponse> {
        return apiService.getProduk()
    }

    suspend fun getProdukById(
        id: Long
    ): Response<ProdukDetailResponse> {
        return apiService.getProdukById(id)
    }
}