package com.example.umkmfinance.ui.penjualan

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.example.umkmfinance.data.model.DetailPenjualan
import com.example.umkmfinance.data.model.Penjualan
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailPenjualanScreen(
    penjualanId: Long,
    viewModel: PenjualanViewModel,
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(penjualanId) {
        viewModel.loadDetailPenjualan(penjualanId)
    }

    DetailPenjualanContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        penjualan = uiState.detailPenjualan,
        itemsDetail = emptyList(),
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPenjualanContent(
    isLoading: Boolean,
    errorMessage: String?,
    penjualan: Penjualan?,
    itemsDetail: List<DetailPenjualan> = emptyList(),
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Penjualan",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = errorMessage)
                    }
                }

                penjualan == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Data penjualan tidak ditemukan.")
                    }
                }

                else -> {
                    val statusUpper = penjualan.status.uppercase(Locale.getDefault())
                    val (badgeBg, badgeText) = when (statusUpper) {
                        "LUNAS" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
                        "PIUTANG" -> Color(0xFFFFF3E0) to Color(0xFFEF6C00)
                        "BATAL" -> Color(0xFFFFEBEE) to Color(0xFFC62828)
                        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))

                        // HEADER INVOICE & STATUS TRANSAKSI
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = penjualan.nomor_invoice,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    SuggestionChip(
                                        onClick = {},
                                        label = {
                                            Text(
                                                text = statusUpper,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold
                                            )
                                        },
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = badgeBg,
                                            labelColor = badgeText
                                        )
                                    )
                                }

                                Text(
                                    text = "Tanggal: ${penjualan.tanggal ?: "-"} • ${penjualan.metode_pembayaran} (Rek #${penjualan.rekening_id})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = penjualan.pelanggan_id?.let { "Pelanggan #$it" } ?: "Pelanggan Umum",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // DAFTAR ITEM TABEL detail_penjualan
                        Text(
                            text = "Item Detail Penjualan (${itemsDetail.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        if (itemsDetail.isEmpty()) {
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
                                        text = "Belum ada item detail_penjualan untuk transaksi #${penjualan.id}.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        } else {
                            itemsDetail.forEach { item ->
                                DetailPenjualanItemCard(item = item)
                            }
                        }

                        // RINGKASAN TOTAL TRANSAKSI
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Ringkasan Pembayaran",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                DetailBaris("Subtotal", formatRupiah(penjualan.subtotal))
                                DetailBaris("Diskon", "-${formatRupiah(penjualan.diskon)}")
                                DetailBaris("Pajak", formatRupiah(penjualan.pajak))
                                DetailBaris("Biaya Lain", formatRupiah(penjualan.biaya_lain))
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Total Akhir",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = formatRupiah(penjualan.total),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                if (!penjualan.catatan.isNullOrBlank()) {
                                    Text(
                                        text = "Catatan: ${penjualan.catatan}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailPenjualanItemCard(
    item: DetailPenjualan
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
                    text = "Produk #${item.produk_id}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Detail #${item.id} • Inv #${item.penjualan_id}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            DetailBaris("Qty", "${item.qty}")
            DetailBaris("Harga Modal", formatRupiah(item.harga_modal))
            DetailBaris("Harga Jual", formatRupiah(item.harga))
            DetailBaris("Diskon", "-${formatRupiah(item.diskon)}")

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            DetailBaris(
                label = "Subtotal Item",
                nilai = formatRupiah(item.subtotal),
                boldNilai = true
            )
        }
    }
}

@Composable
private fun DetailBaris(
    label: String,
    nilai: String,
    boldNilai: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = nilai,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (boldNilai) FontWeight.Bold else FontWeight.Medium,
            color = if (boldNilai) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun formatRupiah(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

@Preview(name = "Detail Penjualan Screen", showBackground = true, showSystemUi = true)
@Composable
private fun DetailPenjualanScreenPreview() {
    val samplePenjualan = Penjualan(
        id = 1L,
        usaha_id = 1L,
        pelanggan_id = 3L,
        rekening_id = 1L,
        nomor_invoice = "INV-20261125-001",
        tanggal = "2026-11-25 10:15:00",
        subtotal = 50000.0,
        diskon = 2000.0,
        pajak = 0.0,
        biaya_lain = 0.0,
        total = 48000.0,
        metode_pembayaran = "QRIS",
        status = "LUNAS",
        catatan = "Dibayar lunas di kasir",
        created_by = 1L
    )

    val sampleItems = listOf(
        DetailPenjualan(
            id = 101L,
            penjualan_id = 1L,
            produk_id = 12L,
            qty = 2,
            harga_modal = 10000.0,
            harga = 18000.0,
            diskon = 1000.0,
            subtotal = 35000.0
        ),
        DetailPenjualan(
            id = 102L,
            penjualan_id = 1L,
            produk_id = 19L,
            qty = 1,
            harga_modal = 8000.0,
            harga = 14000.0,
            diskon = 1000.0,
            subtotal = 13000.0
        )
    )

    MaterialTheme {
        DetailPenjualanContent(
            isLoading = false,
            errorMessage = null,
            penjualan = samplePenjualan,
            itemsDetail = sampleItems
        )
    }
}