package com.example.umkmfinance.ui.pembelian

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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

data class DetailPembelianData(
    val id: Long = 0L,
    val pembelian_id: Long = 0L,
    val produk_id: Long = 0L,
    val qty: Int = 0,
    val harga: Double = 0.0,
    val harga_jual: Double = 0.0,
    val subtotal: Double = 0.0
)

@Composable
fun DetailPembelianScreen(
    pembelianId: Long,
    viewModel: PembelianViewModel,
    detailItems: List<DetailPembelianData> = emptyList()
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
                    catatan = pembelianItem.catatan ?: "-",
                    items = detailItems
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
    catatan: String,
    items: List<DetailPembelianData>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Detail Pembelian",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
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

        Text(
            text = "Item Detail Pembelian (${items.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        if (items.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada item detail_pembelian.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            items.forEach { row ->
                DetailPembelianRowCard(row = row)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DetailPembelianRowCard(
    row: DetailPembelianData
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Produk #${row.produk_id}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ID #${row.id} • Beli #${row.pembelian_id}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            DetailItem(label = "Qty", value = "${row.qty}")
            DetailItem(label = "Harga Beli", value = formatRupiah(row.harga))
            DetailItem(label = "Harga Jual", value = formatRupiah(row.harga_jual))

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            DetailItem(
                label = "Subtotal",
                value = formatRupiah(row.subtotal),
                highlight = true
            )
        }
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String,
    highlight: Boolean = false
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
            color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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
    val sampleItems = listOf(
        DetailPembelianData(
            id = 1L,
            pembelian_id = 10L,
            produk_id = 5L,
            qty = 50,
            harga = 80000.0,
            harga_jual = 90000.0,
            subtotal = 4000000.0
        ),
        DetailPembelianData(
            id = 2L,
            pembelian_id = 10L,
            produk_id = 8L,
            qty = 10,
            harga = 50000.0,
            harga_jual = 60000.0,
            subtotal = 500000.0
        )
    )

    MaterialTheme {
        DetailPembelianView(
            nomorFaktur = "INV-PO-2026-001",
            supplierNama = "PT Sumber Makmur Beras",
            tanggal = "01 Maret 2026",
            status = "LUNAS",
            metodePembayaran = "TRANSFER BANK",
            total = 4500000.0,
            catatan = "Pembelian stok beras pandan wangi 50 sak",
            items = sampleItems
        )
    }
}