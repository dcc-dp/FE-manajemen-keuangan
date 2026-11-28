package com.example.umkmfinance.ui.pengeluaran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.umkmfinance.data.repository.KategoriPengeluaranRepository

class KategoriPengeluaranViewModelFactory(
    private val repository: KategoriPengeluaranRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KategoriPengeluaranViewModel::class.java)) {
            return KategoriPengeluaranViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}