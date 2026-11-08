package com.example.umkmfinance.ui.piutang

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
import com.example.umkmfinance.data.model.Piutang
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PiutangScreen(
    viewModel: PiutangViewModel,
    onPiutangClick: (Piutang) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPiutang()
    }

    PiutangContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        piutangList = uiState.data,
        onPiutangClick = onPiutangClick
    )
}

@Composable
fun PiutangContent(
    isLoading: Boolean,
    errorMessage: String?,
    piutangList: List<Piutang>,
    onPiutangClick: (Piutang) -> Unit
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
                            text = "Piutang Usaha",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${piutangList.size} catatan tagihan pelanggan",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (piutangList.isEmpty()) {
                        item {
                            Text(text = "Belum ada catatan piutang.")
                        }
                    } else {
                        items(piutangList) { item ->
                            PiutangCard(
                                nomorRef = item.nomor_referensi ?: "REF-${item.id}",
                                namaPelanggan = item.pelanggan?.nama ?: "Pelanggan #${item.pelanggan_id}",
                                jatuhTempo = item.jatuh_tempo ?: "-",
                                sisaPiutang = item.sisa_piutang,
                                status = item.status,
                                onClick = { onPiutangClick(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PiutangCard(
    nomorRef: String,
    namaPelanggan: String,
    jatuhTempo: String,
    sisaPiutang: Double,
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
                    text = nomorRef,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = status.replace("_", " "),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = when (status) {
                        "LUNAS" -> Color(0xFF2E7D32)
                        "SEBAGIAN" -> Color(0xFFE65100)
                        else -> Color(0xFFC62828)
                    }
                )
            }

            Text(
                text = "Pelanggan: $namaPelanggan",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Jatuh Tempo: $jatuhTempo",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Sisa Tagihan: ${formatRupiah(sisaPiutang)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD84315)
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
    name = "Piutang Screen - Normal",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PiutangScreenPreview() {
    val dummy = listOf(
        Piutang(
            id = 1L,
            usaha_id = 1L,
            pelanggan_id = 1L,
            nomor_referensi = "INV-2026-0045",
            nominal = 1200000.0,
            sisa_piutang = 450000.0,
            tanggal_transaksi = "2026-10-15",
            jatuh_tempo = "2026-11-15",
            status = "SEBAGIAN"
        ),
        Piutang(
            id = 2L,
            usaha_id = 1L,
            pelanggan_id = 2L,
            nomor_referensi = "INV-2026-0089",
            nominal = 850000.0,
            sisa_piutang = 850000.0,
            tanggal_transaksi = "2026-11-01",
            jatuh_tempo = "2026-11-20",
            status = "BELUM_LUNAS"
        )
    )

    MaterialTheme {
        PiutangContent(
            isLoading = false,
            errorMessage = null,
            piutangList = dummy,
            onPiutangClick = {}
        )
    }
}

@Preview(
    name = "Piutang Screen - Empty State",
    showBackground = true
)
@Composable
private fun PiutangScreenEmptyPreview() {
    MaterialTheme {
        PiutangContent(
            isLoading = false,
            errorMessage = null,
            piutangList = emptyList(),
            onPiutangClick = {}
        )
    }
}