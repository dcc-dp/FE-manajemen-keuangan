package com.example.umkmfinance.ui.penjualan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.Penjualan
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PenjualanScreen(
    viewModel: PenjualanViewModel,
    onPenjualanClick: (Penjualan) -> Unit,
    onTambahPenjualanClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPenjualan()
    }

    PenjualanContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        penjualanList = uiState.data,
        onPenjualanClick = onPenjualanClick,
        onTambahPenjualanClick = onTambahPenjualanClick
    )
}

@Composable
fun PenjualanContent(
    isLoading: Boolean,
    errorMessage: String?,
    penjualanList: List<Penjualan>,
    onPenjualanClick: (Penjualan) -> Unit,
    onTambahPenjualanClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("SEMUA") }

    val statusOptions = listOf("SEMUA", "LUNAS", "PIUTANG", "BATAL")

    val totalOmzetLunas = remember(penjualanList) {
        penjualanList
            .filter { it.status.equals("LUNAS", ignoreCase = true) }
            .sumOf { it.total }
    }
    val totalPiutang = remember(penjualanList) {
        penjualanList
            .filter { it.status.equals("PIUTANG", ignoreCase = true) }
            .sumOf { it.total }
    }

    val filteredList = remember(penjualanList, searchQuery, selectedStatus) {
        penjualanList.filter { item ->
            val cocokStatus = selectedStatus == "SEMUA" ||
                    item.status.equals(selectedStatus, ignoreCase = true)

            val cocokCari = searchQuery.isBlank() ||
                    item.nomor_invoice.contains(searchQuery, ignoreCase = true) ||
                    item.metode_pembayaran.contains(searchQuery, ignoreCase = true) ||
                    (item.catatan?.contains(searchQuery, ignoreCase = true) == true) ||
                    (item.pelanggan_id?.toString()?.contains(searchQuery) == true)

            cocokStatus && cocokCari
        }
    }

    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = onTambahPenjualanClick,
//                containerColor = MaterialTheme.colorScheme.primary,
//                contentColor = MaterialTheme.colorScheme.onPrimary
//            ) {
//                Icon(Icons.Default.Add, contentDescription = "Transaksi Penjualan Baru")
//            }
//        }
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

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    )
                    {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Transaksi Penjualan",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // KARTU RINGKASAN PENJUALAN
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Total Penjualan Lunas",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = formatRupiah(totalOmzetLunas),
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "${penjualanList.size} total invoice tercatat",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Belum Lunas (Piutang)",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = formatRupiah(totalPiutang),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFC62828)
                                        )
                                    }
                                }
                            }
                        }

                        // SEARCH BAR INVOICE / METODE / CATATAN
                        item {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Cari nomor invoice, metode bayar, atau catatan...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(Icons.Default.Clear, contentDescription = null)
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // FILTER STATUS ENUM('LUNAS','PIUTANG','BATAL')
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                statusOptions.forEach { status ->
                                    FilterChip(
                                        selected = selectedStatus == status,
                                        onClick = { selectedStatus = status },
                                        label = { Text(status) }
                                    )
                                }
                            }
                        }

                        // DAFTAR TRANSAKSI PENJUALAN
                        if (filteredList.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (searchQuery.isNotEmpty() || selectedStatus != "SEMUA") {
                                            "Transaksi penjualan tidak ditemukan."
                                        } else {
                                            "Belum ada data penjualan."
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        } else {
                            items(filteredList, key = { it.id }) { penjualan ->
                                PenjualanCard(
                                    penjualan = penjualan,
                                    onClick = { onPenjualanClick(penjualan) }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PenjualanCard(
    penjualan: Penjualan,
    onClick: () -> Unit
) {
    val statusUpper = penjualan.status.uppercase(Locale.getDefault())
    val (badgeBg, badgeText) = when (statusUpper) {
        "LUNAS" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        "PIUTANG" -> Color(0xFFFFF3E0) to Color(0xFFEF6C00)
        "BATAL" -> Color(0xFFFFEBEE) to Color(0xFFC62828)
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    val pelangganLabel = if (penjualan.pelanggan_id != null) {
        "Pelanggan #${penjualan.pelanggan_id}"
    } else {
        "Pelanggan Umum"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // BARIS ATAS: NOMOR INVOICE, TANGGAL & BADGE STATUS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = penjualan.nomor_invoice,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = penjualan.tanggal ?: "-",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                SuggestionChip(
                    onClick = onClick,
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

            // PELANGGAN & METODE PEMBAYARAN + REKENING
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = pelangganLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "${penjualan.metode_pembayaran} • Rek #${penjualan.rekening_id}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // RINCIAN SUBTOTAL, DISKON, PAJAK, BIAYA LAIN
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Subtotal: ${formatRupiah(penjualan.subtotal)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Diskon: -${formatRupiah(penjualan.diskon)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            if (penjualan.pajak > 0.0 || penjualan.biaya_lain > 0.0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Pajak: ${formatRupiah(penjualan.pajak)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "Biaya Lain: ${formatRupiah(penjualan.biaya_lain)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            if (!penjualan.catatan.isNullOrBlank()) {
                Text(
                    text = "Catatan: ${penjualan.catatan}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // TOTAL AKHIR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Tagihan",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatRupiah(penjualan.total),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun formatRupiah(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

@Preview(name = "Penjualan Screen", showBackground = true, showSystemUi = true)
@Composable
private fun PenjualanScreenPreview() {
    val dummyList = listOf(
        Penjualan(
            id = 1L,
            usaha_id = 1L,
            pelanggan_id = 3L,
            rekening_id = 1L,
            nomor_invoice = "INV-20261125-001",
            tanggal = "2026-11-25 10:15:00",
            subtotal = 75000.0,
            diskon = 5000.0,
            pajak = 0.0,
            biaya_lain = 0.0,
            total = 70000.0,
            metode_pembayaran = "QRIS",
            status = "LUNAS",
            catatan = "Pesanan ambil di kasir",
            created_by = 1L
        ),
        Penjualan(
            id = 2L,
            usaha_id = 1L,
            pelanggan_id = null,
            rekening_id = 1L,
            nomor_invoice = "INV-20261125-002",
            tanggal = "2026-11-25 10:30:00",
            subtotal = 120000.0,
            diskon = 0.0,
            pajak = 12000.0,
            biaya_lain = 5000.0,
            total = 137000.0,
            metode_pembayaran = "TRANSFER",
            status = "PIUTANG",
            catatan = "Jatuh tempo akhir bulan",
            created_by = 1L
        )
    )

    MaterialTheme {
        PenjualanContent(
            isLoading = false,
            errorMessage = null,
            penjualanList = dummyList,
            onPenjualanClick = {}
        )
    }
}