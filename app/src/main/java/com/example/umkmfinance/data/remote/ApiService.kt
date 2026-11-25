package com.example.umkmfinance.data.remote

import com.example.umkmfinance.data.model.DashboardResponse
import com.example.umkmfinance.data.model.KategoriProduk
import com.example.umkmfinance.data.model.KategoriProdukDetailResponse
import com.example.umkmfinance.data.model.KategoriProdukResponse
import com.example.umkmfinance.data.model.LoginRequest
import com.example.umkmfinance.data.model.LoginResponse
import com.example.umkmfinance.data.model.PelangganDetailResponse
import com.example.umkmfinance.data.model.PelangganResponse
import com.example.umkmfinance.data.model.Produk
import com.example.umkmfinance.data.model.ProdukDetailResponse
import com.example.umkmfinance.data.model.ProdukResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("api/dashboard")
    suspend fun getDashboard(): Response<DashboardResponse>

    // ==========================================
    // PRODUK
    // ==========================================
    @GET("api/produk")
    suspend fun getProduk(): Response<ProdukResponse>

    @GET("api/produk/{id}")
    suspend fun getProdukById(
        @Path("id") id: Long
    ): Response<ProdukDetailResponse>

    @Multipart
    @POST("api/produk")
    suspend fun tambahProduk(
        @Part("usaha_id") usahaId: RequestBody,
        @Part("kategori_produk_id") kategoriProdukId: RequestBody,
        @Part("kode_produk") kodeProduk: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("harga_modal") hargaModal: RequestBody,
        @Part("harga_jual") hargaJual: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part("satuan") satuan: RequestBody,
        @Part("status") status: RequestBody,
        @Part gambar: MultipartBody.Part? = null
    ): Response<ProdukDetailResponse>

    @PUT("api/produk/{id}")
    suspend fun updateProduk(
        @Path("id") id: Long,
        @Body request: Produk
    ): Response<ProdukDetailResponse>

    @DELETE("api/produk/{id}")
    suspend fun hapusProduk(
        @Path("id") id: Long
    ): Response<ProdukDetailResponse>

    // ==========================================
    // PELANGGAN
    // ==========================================
    @GET("api/pelanggan")
    suspend fun getPelanggan(): Response<PelangganResponse>

    @GET("api/pelanggan/{id}")
    suspend fun getPelangganById(
        @Path("id") id: Long
    ): Response<PelangganDetailResponse>

    // ==========================================
    // KATEGORI PRODUK
    // ==========================================
    @GET("api/kategori-produk")
    suspend fun getKategoriProduk(): Response<KategoriProdukResponse>

    @POST("api/kategori-produk")
    suspend fun tambahKategoriProduk(
        @Body request: KategoriProduk
    ): Response<KategoriProdukDetailResponse>

    @PUT("api/kategori-produk/{id}")
    suspend fun updateKategoriProduk(
        @Path("id") id: Long,
        @Body request: KategoriProduk
    ): Response<KategoriProdukDetailResponse>

    @DELETE("api/kategori-produk/{id}")
    suspend fun hapusKategoriProduk(
        @Path("id") id: Long
    ): Response<KategoriProdukDetailResponse>
}