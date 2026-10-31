package com.example.umkmfinance.data.remote

import android.content.Context
import com.example.umkmfinance.data.local.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.15:3000/"

    fun create(context: Context): Retrofit {

        val tokenManager = TokenManager(context)

        val client = OkHttpClient.Builder()
            .addInterceptor(
                AuthInterceptor(tokenManager)
            )
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }
}