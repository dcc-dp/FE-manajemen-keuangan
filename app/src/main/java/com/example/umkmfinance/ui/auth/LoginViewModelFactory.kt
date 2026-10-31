package com.example.umkmfinance.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.umkmfinance.data.local.TokenManager
import com.example.umkmfinance.data.remote.ApiService
import com.example.umkmfinance.data.remote.RetrofitClient
import com.example.umkmfinance.data.repository.AuthRepository

class LoginViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {

            val apiService = RetrofitClient
                .create(context)
                .create(ApiService::class.java)

            val repository = AuthRepository(apiService)

            val tokenManager = TokenManager(context)

            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(
                repository = repository,
                tokenManager = tokenManager
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}