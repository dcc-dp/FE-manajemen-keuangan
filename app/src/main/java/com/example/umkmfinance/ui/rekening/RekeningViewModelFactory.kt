package com.example.umkmfinance.ui.rekening

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.umkmfinance.data.repository.RekeningRepository

class RekeningViewModelFactory(
    private val repository: RekeningRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RekeningViewModel::class.java)) {
            return RekeningViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}