package com.example.umkmfinance.data.remote

import com.example.umkmfinance.data.model.DashboardResponse
import com.example.umkmfinance.data.model.LoginRequest
import com.example.umkmfinance.data.model.LoginResponse
import com.example.umkmfinance.data.model.ProdukDetailResponse
import com.example.umkmfinance.data.model.ProdukResponse
import com.example.umkmfinance.data.model.PelangganResponse
import com.example.umkmfinance.data.model.PelangganDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("api/dashboard")
    suspend fun getDashboard(): Response<DashboardResponse>

    @GET("api/produk")
    suspend fun getProduk(): Response<ProdukResponse>

    @GET("api/produk/{id}")
    suspend fun getProdukById(
        @Path("id") id: Long
    ): Response<ProdukDetailResponse>

    @GET("api/pelanggan")
    suspend fun getPelanggan(): Response<PelangganResponse>

    @GET("api/pelanggan/{id}")
    suspend fun getPelangganById(
        @Path("id") id: Long
    ): Response<PelangganDetailResponse>

}