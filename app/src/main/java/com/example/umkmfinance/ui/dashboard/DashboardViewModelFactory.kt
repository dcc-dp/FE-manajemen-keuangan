package com.example.umkmfinance.ui.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.umkmfinance.data.remote.ApiService
import com.example.umkmfinance.data.remote.RetrofitClient
import com.example.umkmfinance.data.repository.DashboardRepository

class DashboardViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(
                DashboardViewModel::class.java
            )
        ) {

            val apiService = RetrofitClient
                .create(context)
                .create(ApiService::class.java)

            val repository = DashboardRepository(
                apiService
            )

            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(
                repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}