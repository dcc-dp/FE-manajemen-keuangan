package com.example.umkmfinance.data.repository

import com.example.umkmfinance.data.model.Produk
import com.example.umkmfinance.data.model.ProdukDetailResponse
import com.example.umkmfinance.data.model.ProdukResponse
import com.example.umkmfinance.data.remote.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class ProdukRepository(
    private val apiService: ApiService
) {

    // Mengambil semua daftar produk
    suspend fun getProduk(): Response<ProdukResponse> {
        return apiService.getProduk()
    }

    // Mengambil detail produk berdasarkan ID
    suspend fun getProdukById(
        id: Long
    ): Response<ProdukDetailResponse> {
        return apiService.getProdukById(id)
    }

    // Tambah produk dengan dukungan multipart (opsional gambar)
    suspend fun tambahProduk(
        usahaId: RequestBody,
        kategoriProdukId: RequestBody,
        kodeProduk: RequestBody,
        nama: RequestBody,
        hargaModal: RequestBody,
        hargaJual: RequestBody,
        stok: RequestBody,
        satuan: RequestBody,
        status: RequestBody,
        gambar: MultipartBody.Part? = null
    ): Response<ProdukDetailResponse> {
        return apiService.tambahProduk(
            usahaId = usahaId,
            kategoriProdukId = kategoriProdukId,
            kodeProduk = kodeProduk,
            nama = nama,
            hargaModal = hargaModal,
            hargaJual = hargaJual,
            stok = stok,
            satuan = satuan,
            status = status,
            gambar = gambar
        )
    }

    // Perbarui produk
    suspend fun updateProduk(
        id: Long,
        produk: Produk
    ): Response<ProdukDetailResponse> {
        return apiService.updateProduk(id = id, request = produk)
    }

    // Hapus produk
    suspend fun hapusProduk(
        id: Long
    ): Response<ProdukDetailResponse> {
        return apiService.hapusProduk(id = id)
    }

    companion object {
        fun toTextRequestBody(value: String): RequestBody {
            return value.toRequestBody("text/plain".toMediaTypeOrNull())
        }
    }
}