package com.example.umkmfinance.ui.hutang

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
import com.example.umkmfinance.data.model.Hutang
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailHutangScreen(
    hutangId: Long,
    viewModel: HutangViewModel
) {
    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(hutangId) {
        viewModel.loadHutangById(hutangId)
    }

    DetailHutangContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        hutang = uiState.data
    )
}

@Composable
fun DetailHutangContent(
    isLoading: Boolean,
    errorMessage: String?,
    hutang: Hutang?
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

            hutang != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Detail Hutang",
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
                                text = hutang.nomor_referensi ?: "REF-${hutang.id}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            DetailItem(
                                label = "Supplier",
                                value = hutang.supplier?.nama ?: "ID #${hutang.supplier_id}"
                            )
                            DetailItem(
                                label = "Total Kewajiban",
                                value = formatRupiah(hutang.nominal)
                            )
                            DetailItem(
                                label = "Sisa Hutang",
                                value = formatRupiah(hutang.sisa_hutang),
                                isHighlight = true
                            )
                            DetailItem(
                                label = "Status",
                                value = hutang.status.replace("_", " ")
                            )
                            DetailItem(
                                label = "Tanggal Transaksi",
                                value = hutang.tanggal_transaksi ?: "-"
                            )
                            DetailItem(
                                label = "Jatuh Tempo",
                                value = hutang.jatuh_tempo ?: "-"
                            )
                            DetailItem(
                                label = "Catatan",
                                value = hutang.catatan ?: "-"
                            )
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
    name = "Detail Hutang - Normal",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DetailHutangPreview() {
    val dummy = Hutang(
        id = 1L,
        usaha_id = 1L,
        supplier_id = 1L,
        nomor_referensi = "PO-2026-0031",
        nominal = 3500000.0,
        sisa_hutang = 1500000.0,
        tanggal_transaksi = "25 Oktober 2026",
        jatuh_tempo = "25 November 2026",
        status = "SEBAGIAN",
        catatan = "Pembelian bahan baku beras termin 30 hari"
    )

    MaterialTheme {
        DetailHutangContent(
            isLoading = false,
            errorMessage = null,
            hutang = dummy
        )
    }
}