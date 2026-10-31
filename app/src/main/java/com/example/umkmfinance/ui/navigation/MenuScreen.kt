package com.example.umkmfinance.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuScreen(
    role: String,
    onMenuClick: (String) -> Unit
) {

    val menus = when (role) {

        "OWNER" -> listOf(
            "Penjualan",
            "Pembelian",
            "Produk",
            "Pelanggan",
            "Supplier",
            "Pengeluaran",
            "Piutang",
            "Hutang",
            "Rekening",
            "Target Keuangan",
            "Laporan",
            "Pengguna",
            "Role & Permission",
            "Notifikasi"
        )

        "ADMIN" -> listOf(
            "Penjualan",
            "Pembelian",
            "Produk",
            "Pelanggan",
            "Supplier",
            "Pengeluaran",
            "Piutang",
            "Hutang",
            "Rekening",
            "Laporan",
            "Notifikasi"
        )

        "KASIR" -> listOf(
            "Penjualan",
            "Pelanggan",
            "Piutang",
            "Notifikasi"
        )

        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Menu $role"
        )

        menus.forEach { menu ->

            Button(
                onClick = {
                    onMenuClick(menu)
                }
            ){

                Text(
                    text = menu
                )
            }
        }
    }
}