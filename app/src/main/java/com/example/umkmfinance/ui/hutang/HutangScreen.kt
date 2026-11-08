package com.example.umkmfinance.ui.hutang

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
import com.example.umkmfinance.data.model.Hutang
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HutangScreen(
    viewModel: HutangViewModel,
    onHutangClick: (Hutang) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHutang()
    }

    HutangContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        hutangList = uiState.data,
        onHutangClick = onHutangClick
    )
}

@Composable
fun HutangContent(
    isLoading: Boolean,
    errorMessage: String?,
    hutangList: List<Hutang>,
    onHutangClick: (Hutang) -> Unit
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
                            text = "Hutang Usaha",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${hutangList.size} catatan kewajiban supplier",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (hutangList.isEmpty()) {
                        item {
                            Text(text = "Belum ada catatan hutang.")
                        }
                    } else {
                        items(hutangList) { item ->
                            HutangCard(
                                nomorRef = item.nomor_referensi ?: "REF-${item.id}",
                                namaSupplier = item.supplier?.nama ?: "Supplier #${item.supplier_id}",
                                jatuhTempo = item.jatuh_tempo ?: "-",
                                sisaHutang = item.sisa_hutang,
                                status = item.status,
                                onClick = { onHutangClick(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HutangCard(
    nomorRef: String,
    namaSupplier: String,
    jatuhTempo: String,
    sisaHutang: Double,
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
                text = "Supplier: $namaSupplier",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Jatuh Tempo: $jatuhTempo",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Sisa Hutang: ${formatRupiah(sisaHutang)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC62828)
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
    name = "Hutang Screen - Normal",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun HutangScreenPreview() {
    val dummy = listOf(
        Hutang(
            id = 1L,
            usaha_id = 1L,
            supplier_id = 1L,
            nomor_referensi = "PO-2026-0031",
            nominal = 3500000.0,
            sisa_hutang = 1500000.0,
            tanggal_transaksi = "2026-10-25",
            jatuh_tempo = "2026-11-25",
            status = "SEBAGIAN"
        ),
        Hutang(
            id = 2L,
            usaha_id = 1L,
            supplier_id = 2L,
            nomor_referensi = "PO-2026-0042",
            nominal = 2100000.0,
            sisa_hutang = 2100000.0,
            tanggal_transaksi = "2026-11-02",
            jatuh_tempo = "2026-11-18",
            status = "BELUM_LUNAS"
        )
    )

    MaterialTheme {
        HutangContent(
            isLoading = false,
            errorMessage = null,
            hutangList = dummy,
            onHutangClick = {}
        )
    }
}

@Preview(
    name = "Hutang Screen - Empty State",
    showBackground = true
)
@Composable
private fun HutangScreenEmptyPreview() {
    MaterialTheme {
        HutangContent(
            isLoading = false,
            errorMessage = null,
            hutangList = emptyList(),
            onHutangClick = {}
        )
    }
}