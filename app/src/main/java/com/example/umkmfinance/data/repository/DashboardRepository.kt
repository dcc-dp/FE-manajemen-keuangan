package com.example.umkmfinance.data.repository

import com.example.umkmfinance.data.model.DashboardResponse
import com.example.umkmfinance.data.remote.ApiService
import retrofit2.Response

class DashboardRepository(
    private val apiService: ApiService
) {

    suspend fun getDashboard(): Response<DashboardResponse> {
        return apiService.getDashboard()
    }
}