package com.example.umkmfinance.ui.pengeluaran

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
fun DetailPengeluaranScreen(
    pengeluaranId: Long,
    viewModel: PengeluaranViewModel
) {
    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(pengeluaranId) {
        viewModel.loadPengeluaranById(pengeluaranId)
    }

    DetailPengeluaranContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        pengeluaran = uiState.data
    )
}

@Composable
fun DetailPengeluaranContent(
    isLoading: Boolean,
    errorMessage: String?,
    pengeluaran: Pengeluaran?
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

            pengeluaran != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Detail Pengeluaran",
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
                                text = pengeluaran.judul,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            DetailItem(
                                label = "Nominal",
                                value = formatRupiah(pengeluaran.nominal),
                                isHighlight = true
                            )
                            DetailItem(label = "Tanggal", value = pengeluaran.tanggal ?: "-")
                            DetailItem(label = "Kategori ID", value = pengeluaran.kategori_pengeluaran_id.toString())
                            DetailItem(label = "Metode Pembayaran", value = pengeluaran.metode_pembayaran ?: "-")
                            DetailItem(label = "Keterangan", value = pengeluaran.keterangan ?: "-")
                            DetailItem(label = "Bukti Nota", value = pengeluaran.bukti_nota ?: "Tidak ada lampiran")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String,
    isHighlight: Boolean = false
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
            fontWeight = FontWeight.SemiBold,
            color = if (isHighlight) Color(0xFFC62828) else MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun formatRupiah(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

// =============================================================
// PREVIEW ANDROID STUDIO
// =============================================================

@Preview(
    name = "Detail Pengeluaran - Normal",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DetailPengeluaranPreview() {
    val dummy = Pengeluaran(
        id = 1L,
        usaha_id = 1L,
        kategori_pengeluaran_id = 1L,
        judul = "Listrik Toko Bulan Ini",
        nominal = 450000.0,
        tanggal = "01 November 2026",
        metode_pembayaran = "TRANSFER BANK",
        keterangan = "Pembayaran token PLN 2200 VA untuk operasional",
        bukti_nota = "nota_pln_nov2026.jpg"
    )

    MaterialTheme {
        DetailPengeluaranContent(
            isLoading = false,
            errorMessage = null,
            pengeluaran = dummy
        )
    }
}