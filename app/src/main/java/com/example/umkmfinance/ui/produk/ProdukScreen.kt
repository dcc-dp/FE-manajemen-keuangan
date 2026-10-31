package com.example.umkmfinance.ui.produk

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.Produk
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProdukScreen(
    viewModel: ProdukViewModel,
    onProdukClick: (Produk) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProduk()
    }

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.errorMessage != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(uiState.errorMessage ?: "Terjadi kesalahan")
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Produk",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${uiState.data.size} produk",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (uiState.data.isEmpty()) {
                    item {
                        Text("Belum ada produk.")
                    }
                } else {
                    items(uiState.data) { produk ->
                        ProdukCard(
                            produk = produk,
                            onClick = {
                                onProdukClick(produk)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProdukCard(
    produk: Produk,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = produk.nama,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text("Kode: ${produk.kode_produk}")

            Text(
                "Harga jual: ${formatRupiah(produk.harga_jual)}"
            )

            Text(
                "Harga modal: ${formatRupiah(produk.harga_modal)}"
            )

            Text(
                "Stok: ${produk.stok} ${produk.satuan}"
            )

            Text("Status: ${produk.status}")
        }
    }
}

private fun formatRupiah(
    value: Double
): String {

    val formatter = NumberFormat.getCurrencyInstance(
        Locale("id", "ID")
    )

    formatter.maximumFractionDigits = 0

    return formatter.format(value)
}