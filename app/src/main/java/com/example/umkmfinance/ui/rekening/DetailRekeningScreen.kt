package com.example.umkmfinance.ui.rekening

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
import com.example.umkmfinance.data.model.Rekening
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailRekeningScreen(
    rekeningId: Long,
    viewModel: RekeningViewModel
) {
    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(rekeningId) {
        viewModel.loadRekeningById(rekeningId)
    }

    DetailRekeningContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        rekening = uiState.data
    )
}

@Composable
fun DetailRekeningContent(
    isLoading: Boolean,
    errorMessage: String?,
    rekening: Rekening?
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

            rekening != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Detail Rekening",
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
                                text = rekening.nama_bank,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            DetailItem(
                                label = "Saldo Terkini",
                                value = formatRupiah(rekening.saldo),
                                isHighlight = true
                            )
                            DetailItem(label = "Nomor Rekening", value = rekening.nomor_rekening ?: "-")
                            DetailItem(label = "Atas Nama", value = rekening.atas_nama ?: "-")
                            DetailItem(label = "Tipe Akun", value = rekening.tipe.replace("_", " "))
                            DetailItem(
                                label = "Status",
                                value = if (rekening.status_aktif) "Aktif" else "Non-Aktif"
                            )
                            DetailItem(label = "Catatan", value = rekening.catatan ?: "-")
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
            color = if (isHighlight) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
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
    name = "Detail Rekening - Normal",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DetailRekeningPreview() {
    val dummy = Rekening(
        id = 1L,
        usaha_id = 1L,
        nama_bank = "BCA Operasional Usaha",
        nomor_rekening = "7820192841",
        atas_nama = "CV Berkah Abadi Makmur",
        tipe = "BANK",
        saldo = 24500000.0,
        status_aktif = true,
        catatan = "Rekening utama penerimaan transfer pelanggan dan pembayaran supplier"
    )

    MaterialTheme {
        DetailRekeningContent(
            isLoading = false,
            errorMessage = null,
            rekening = dummy
        )
    }
}