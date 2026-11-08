package com.example.umkmfinance.ui.rekening

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
import com.example.umkmfinance.data.model.Rekening
import java.text.NumberFormat
import java.util.Locale

@Composable
fun RekeningScreen(
    viewModel: RekeningViewModel,
    onRekeningClick: (Rekening) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRekening()
    }

    RekeningContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        rekeningList = uiState.data,
        onRekeningClick = onRekeningClick
    )
}

@Composable
fun RekeningContent(
    isLoading: Boolean,
    errorMessage: String?,
    rekeningList: List<Rekening>,
    onRekeningClick: (Rekening) -> Unit
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
                val totalSaldoSemua = rekeningList.sumOf { it.saldo }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Rekening & Kas",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Total Likuiditas: ${formatRupiah(totalSaldoSemua)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1B5E20)
                        )
                    }

                    if (rekeningList.isEmpty()) {
                        item {
                            Text(text = "Belum ada akun kas atau rekening terdaftar.")
                        }
                    } else {
                        items(rekeningList) { item ->
                            RekeningCard(
                                namaBank = item.nama_bank,
                                nomorRekening = item.nomor_rekening ?: "Tanpa No. Rekening",
                                atasNama = item.atas_nama ?: "-",
                                tipe = item.tipe,
                                saldo = item.saldo,
                                statusAktif = item.status_aktif,
                                onClick = { onRekeningClick(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RekeningCard(
    namaBank: String,
    nomorRekening: String,
    atasNama: String,
    tipe: String,
    saldo: Double,
    statusAktif: Boolean,
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
                    text = namaBank,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (statusAktif) tipe.replace("_", " ") else "NON-AKTIF",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (statusAktif) Color(0xFF1565C0) else Color(0xFF757575)
                )
            }

            Text(
                text = nomorRekening,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "a.n. $atasNama",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Saldo: ${formatRupiah(saldo)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
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
// PREVIEW ANDROID STUDIO
// =============================================================

@Preview(
    name = "Rekening Screen - Normal",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun RekeningScreenPreview() {
    val dummy = listOf(
        Rekening(
            id = 1L,
            usaha_id = 1L,
            nama_bank = "BCA Operasional",
            nomor_rekening = "7820192841",
            atas_nama = "CV Berkah Abadi",
            tipe = "BANK",
            saldo = 24500000.0,
            status_aktif = true
        ),
        Rekening(
            id = 2L,
            usaha_id = 1L,
            nama_bank = "Kas Tunai Toko",
            nomor_rekening = "-",
            atas_nama = "Kasir Utama",
            tipe = "KAS_TUNAI",
            saldo = 1850000.0,
            status_aktif = true
        )
    )

    MaterialTheme {
        RekeningContent(
            isLoading = false,
            errorMessage = null,
            rekeningList = dummy,
            onRekeningClick = {}
        )
    }
}

@Preview(
    name = "Rekening Screen - Empty State",
    showBackground = true
)
@Composable
private fun RekeningScreenEmptyPreview() {
    MaterialTheme {
        RekeningContent(
            isLoading = false,
            errorMessage = null,
            rekeningList = emptyList(),
            onRekeningClick = {}
        )
    }
}