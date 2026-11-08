package com.example.umkmfinance.ui.pengeluaran

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.Pengeluaran
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PengeluaranScreen(
    viewModel: PengeluaranViewModel,
    onPengeluaranClick: (Pengeluaran) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPengeluaran()
    }

    PengeluaranContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        pengeluaranList = uiState.data,
        onPengeluaranClick = onPengeluaranClick
    )
}

@Composable
fun PengeluaranContent(
    isLoading: Boolean,
    errorMessage: String?,
    pengeluaranList: List<Pengeluaran>,
    onPengeluaranClick: (Pengeluaran) -> Unit
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
                            text = "Pengeluaran Biaya",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${pengeluaranList.size} catatan pengeluaran",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (pengeluaranList.isEmpty()) {
                        item {
                            Text(text = "Belum ada catatan pengeluaran.")
                        }
                    } else {
                        items(pengeluaranList) { item ->
                            PengeluaranCard(
                                judul = item.judul,
                                nominal = item.nominal,
                                tanggal = item.tanggal ?: "-",
                                metodePembayaran = item.metode_pembayaran ?: "-",
                                keterangan = item.keterangan ?: "-",
                                onClick = { onPengeluaranClick(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PengeluaranCard(
    judul: String,
    nominal: Double,
    tanggal: String,
    metodePembayaran: String,
    keterangan: String,
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
                    text = judul,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = formatRupiah(nominal),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828)
                )
            }

            Text(
                text = "Tanggal: $tanggal",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Metode: $metodePembayaran",
                style = MaterialTheme.typography.bodySmall
            )

            if (keterangan.isNotEmpty() && keterangan != "-") {
                Text(
                    text = "Keterangan: $keterangan",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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

@Preview(
    name = "Pengeluaran Screen - Normal",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PengeluaranScreenPreview() {
    val dummyList = listOf(
        Pengeluaran(
            id = 1L,
            usaha_id = 1L,
            kategori_pengeluaran_id = 1L,
            judul = "Listrik Toko Bulan Ini",
            nominal = 450000.0,
            tanggal = "2026-11-01",
            metode_pembayaran = "TRANSFER",
            keterangan = "Pembayaran token PLN 2200 VA"
        ),
        Pengeluaran(
            id = 2L,
            usaha_id = 1L,
            kategori_pengeluaran_id = 2L,
            judul = "Beli Gas LPG 3Kg (4 Tabung)",
            nominal = 92000.0,
            tanggal = "2026-11-04",
            metode_pembayaran = "TUNAI",
            keterangan = "Operasional dapur produksi"
        )
    )

    MaterialTheme {
        PengeluaranContent(
            isLoading = false,
            errorMessage = null,
            pengeluaranList = dummyList,
            onPengeluaranClick = {}
        )
    }
}

@Preview(
    name = "Pengeluaran Screen - Empty State",
    showBackground = true
)
@Composable
private fun PengeluaranScreenEmptyPreview() {
    MaterialTheme {
        PengeluaranContent(
            isLoading = false,
            errorMessage = null,
            pengeluaranList = emptyList(),
            onPengeluaranClick = {}
        )
    }
}