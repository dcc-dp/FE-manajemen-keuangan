package com.example.umkmfinance.ui.pembelian

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.Pembelian
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailPembelianScreen(
    pembelianId: Long,
    viewModel: PembelianViewModel
) {
    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(pembelianId) {
        viewModel.loadPembelianById(pembelianId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val pembelianItem: Pembelian? = uiState.data

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
                    Text(text = uiState.errorMessage ?: "Terjadi kesalahan")
                }
            }

            pembelianItem != null -> {
                DetailPembelianView(
                    nomorFaktur = pembelianItem.nomor_faktur ?: "PO-${pembelianItem.id}",
                    supplierNama = pembelianItem.supplier?.nama ?: "Supplier #${pembelianItem.supplier_id}",
                    tanggal = pembelianItem.tanggal ?: "-",
                    status = pembelianItem.status ?: "-",
                    metodePembayaran = pembelianItem.metode_pembayaran ?: "-",
                    total = pembelianItem.total.toDouble(),
                    catatan = pembelianItem.catatan ?: "-"
                )
            }
        }
    }
}

@Composable
private fun DetailPembelianView(
    nomorFaktur: String,
    supplierNama: String,
    tanggal: String,
    status: String,
    metodePembayaran: String,
    total: Double,
    catatan: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Detail Pembelian",
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
                    text = nomorFaktur,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                DetailItem(label = "Supplier", value = supplierNama)
                DetailItem(label = "Tanggal", value = tanggal)
                DetailItem(label = "Status", value = status)
                DetailItem(label = "Metode Pembayaran", value = metodePembayaran)
                DetailItem(label = "Total Transaksi", value = formatRupiah(total))
                DetailItem(label = "Catatan", value = catatan)
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
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

// =============================================================
// PREVIEW ANDROID STUDIO
// =============================================================

@Preview(
    name = "Detail Pembelian - Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DetailPembelianPreview() {
    MaterialTheme {
        DetailPembelianView(
            nomorFaktur = "INV-PO-2026-001",
            supplierNama = "PT Sumber Makmur Beras",
            tanggal = "01 Maret 2026",
            status = "LUNAS",
            metodePembayaran = "TRANSFER BANK",
            total = 4500000.0,
            catatan = "Pembelian stok beras pandan wangi 50 sak"
        )
    }
}