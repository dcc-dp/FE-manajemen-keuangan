package com.example.umkmfinance.ui.produk

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailProdukScreen(
    produkId: Long,
    viewModel: ProdukViewModel
) {
    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(produkId) {
        viewModel.loadProdukById(produkId)
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
                Text(
                    text = uiState.errorMessage
                        ?: "Terjadi kesalahan"
                )
            }
        }

        uiState.data != null -> {
            val produk = uiState.data!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Detail Produk",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = produk.nama,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        DetailItem(
                            label = "Kode Produk",
                            value = produk.kode_produk
                        )

                        DetailItem(
                            label = "Barcode",
                            value = produk.barcode ?: "-"
                        )

                        DetailItem(
                            label = "Kategori ID",
                            value = produk.kategori_id.toString()
                        )

                        DetailItem(
                            label = "Harga Modal",
                            value = formatRupiah(
                                produk.harga_modal
                            )
                        )

                        DetailItem(
                            label = "Harga Jual",
                            value = formatRupiah(
                                produk.harga_jual
                            )
                        )

                        DetailItem(
                            label = "Stok",
                            value = "${produk.stok} ${produk.satuan}"
                        )

                        DetailItem(
                            label = "Status",
                            value = produk.status
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatRupiah(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(
        Locale("id", "ID")
    )

    formatter.maximumFractionDigits = 0

    return formatter.format(value)
}