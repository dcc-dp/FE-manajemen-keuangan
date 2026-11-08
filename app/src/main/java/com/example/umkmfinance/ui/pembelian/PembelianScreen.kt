package com.example.umkmfinance.ui.pembelian

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.Pembelian
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PembelianScreen(
    viewModel: PembelianViewModel,
    onPembelianClick: (Pembelian) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPembelian()
    }

    PembelianContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        pembelianList = uiState.data,
        onPembelianClick = onPembelianClick
    )
}

@Composable
fun PembelianContent(
    isLoading: Boolean,
    errorMessage: String?,
    pembelianList: List<Pembelian>,
    onPembelianClick: (Pembelian) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = errorMessage)
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
                            text = "Pembelian",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${pembelianList.size} transaksi pembelian",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (pembelianList.isEmpty()) {
                        item {
                            Text(text = "Belum ada transaksi pembelian.")
                        }
                    } else {
                        items(pembelianList) { item ->
                            PembelianCard(
                                nomorFaktur = item.nomor_faktur ?: "PO-${item.id}",
                                namaSupplier = item.supplier?.nama ?: "Supplier #${item.supplier_id}",
                                tanggal = item.tanggal ?: "-",
                                total = item.total.toDouble(),
                                status = item.status ?: "SELESAI",
                                onClick = { onPembelianClick(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PembelianCard(
    nomorFaktur: String,
    namaSupplier: String,
    tanggal: String,
    total: Double,
    status: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = nomorFaktur,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Supplier: $namaSupplier",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Tanggal: $tanggal",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Total: ${formatRupiah(total)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun formatRupiah(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

// =============================================================
// PREVIEW ANDROID STUDIO (SPLIT MODE)
// =============================================================

data class PembelianItemPreview(
    val nomorFaktur: String,
    val namaSupplier: String,
    val tanggal: String,
    val total: Double,
    val status: String
)

@Preview(
    name = "Pembelian Screen - Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PembelianScreenPreview() {
    val dummyList = listOf(
        PembelianItemPreview(
            nomorFaktur = "INV-PO-2026-001",
            namaSupplier = "PT Sumber Makmur Beras",
            tanggal = "2026-03-01",
            total = 4500000.0,
            status = "LUNAS"
        ),
        PembelianItemPreview(
            nomorFaktur = "INV-PO-2026-002",
            namaSupplier = "Distributor Minyak Goreng Sejahtera",
            tanggal = "2026-03-03",
            total = 2750000.0,
            status = "BELUM LUNAS"
        )
    )

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Pembelian",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${dummyList.size} transaksi pembelian",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                items(dummyList) { item ->
                    PembelianCard(
                        nomorFaktur = item.nomorFaktur,
                        namaSupplier = item.namaSupplier,
                        tanggal = item.tanggal,
                        total = item.total,
                        status = item.status,
                        onClick = {}
                    )
                }
            }
        }
    }
}