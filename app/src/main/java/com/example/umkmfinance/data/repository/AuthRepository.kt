package com.example.umkmfinance.data.repository

import com.example.umkmfinance.data.model.LoginRequest
import com.example.umkmfinance.data.model.LoginResponse
import com.example.umkmfinance.data.remote.ApiService
import retrofit2.Response

class AuthRepository(
    private val apiService: ApiService
) {

    suspend fun login(
        username: String,
        password: String
    ): Response<LoginResponse> {

        val request = LoginRequest(
            username = username,
            password = password
        )

        return apiService.login(request)
    }
}