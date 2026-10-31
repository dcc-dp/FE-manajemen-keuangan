package com.example.umkmfinance.ui.pelanggan

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.umkmfinance.data.remote.ApiService
import com.example.umkmfinance.data.remote.RetrofitClient
import com.example.umkmfinance.data.repository.PelangganRepository

class PelangganViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(
                PelangganViewModel::class.java
            )
        ) {

            val apiService =
                RetrofitClient
                    .create(context)
                    .create(ApiService::class.java)

            val repository =
                PelangganRepository(apiService)

            @Suppress("UNCHECKED_CAST")
            return PelangganViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}