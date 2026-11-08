package com.example.umkmfinance.ui.penjualan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.umkmfinance.data.model.Penjualan

@Composable
fun PenjualanScreen(
    viewModel: PenjualanViewModel,
    onPenjualanClick: (Penjualan) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Halaman Transaksi Penjualan")
    }
}