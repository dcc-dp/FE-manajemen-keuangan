package com.example.umkmfinance.ui.piutang

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
import com.example.umkmfinance.data.model.Piutang
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailPiutangScreen(
    piutangId: Long,
    viewModel: PiutangViewModel
) {
    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(piutangId) {
        viewModel.loadPiutangById(piutangId)
    }

    DetailPiutangContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        piutang = uiState.data
    )
}

@Composable
fun DetailPiutangContent(
    isLoading: Boolean,
    errorMessage: String?,
    piutang: Piutang?
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

            piutang != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Detail Piutang",
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
                                text = piutang.nomor_referensi ?: "REF-${piutang.id}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            DetailItem(
                                label = "Pelanggan",
                                value = piutang.pelanggan?.nama ?: "ID #${piutang.pelanggan_id}"
                            )
                            DetailItem(
                                label = "Total Nominal",
                                value = formatRupiah(piutang.nominal)
                            )
                            DetailItem(
                                label = "Sisa Tagihan",
                                value = formatRupiah(piutang.sisa_piutang),
                                isHighlight = true
                            )
                            DetailItem(
                                label = "Status",
                                value = piutang.status.replace("_", " ")
                            )
                            DetailItem(
                                label = "Tanggal Transaksi",
                                value = piutang.tanggal_transaksi ?: "-"
                            )
                            DetailItem(
                                label = "Jatuh Tempo",
                                value = piutang.jatuh_tempo ?: "-"
                            )
                            DetailItem(
                                label = "Keterangan",
                                value = piutang.keterangan ?: "-"
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
            color = if (isHighlight) Color(0xFFD84315) else MaterialTheme.colorScheme.onSurface
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
    name = "Detail Piutang - Normal",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DetailPiutangPreview() {
    val dummy = Piutang(
        id = 1L,
        usaha_id = 1L,
        pelanggan_id = 1L,
        nomor_referensi = "INV-2026-0045",
        nominal = 1200000.0,
        sisa_piutang = 450000.0,
        tanggal_transaksi = "15 Oktober 2026",
        jatuh_tempo = "15 November 2026",
        status = "SEBAGIAN",
        keterangan = "Pengambilan pesanan catering arisan"
    )

    MaterialTheme {
        DetailPiutangContent(
            isLoading = false,
            errorMessage = null,
            piutang = dummy
        )
    }
}