package com.example.umkmfinance.ui.pembelian

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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.umkmfinance.data.model.Pembelian
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PembelianScreen(
    viewModel: PembelianViewModel,
    onPembelianClick: (Pembelian) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPembelian()
    }

    PembelianContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        pembelianList = uiState.data,
        onPembelianClick = onPembelianClick
    )
}

@Composable
fun PembelianContent(
    isLoading: Boolean,
    errorMessage: String?,
    pembelianList: List<Pembelian>,
    onPembelianClick: (Pembelian) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("SEMUA") }

    val statusOptions = listOf("SEMUA", "LUNAS", "HUTANG", "BATAL")

    val totalLunas = remember(pembelianList) {
        pembelianList
            .filter { (it.status ?: "").equals("LUNAS", ignoreCase = true) }
            .sumOf { it.total.toDouble() }
    }
    val totalHutang = remember(pembelianList) {
        pembelianList
            .filter { (it.status ?: "").equals("HUTANG", ignoreCase = true) }
            .sumOf { it.total.toDouble() }
    }

    val filteredList = remember(pembelianList, searchQuery, selectedStatus) {
        pembelianList.filter { item ->
            val statusItem = (item.status ?: "LUNAS").uppercase(Locale.getDefault())
            val cocokStatus = selectedStatus == "SEMUA" || statusItem == selectedStatus

            val nomorFaktur = item.nomor_faktur ?: "PO-${item.id}"
            val metode = item.metode_pembayaran ?: ""
            val catatan = item.catatan ?: ""
            val supplierText = item.supplier?.nama ?: "Supplier #${item.supplier_id}"

            val cocokCari = searchQuery.isBlank() ||
                    nomorFaktur.contains(searchQuery, ignoreCase = true) ||
                    supplierText.contains(searchQuery, ignoreCase = true) ||
                    metode.contains(searchQuery, ignoreCase = true) ||
                    catatan.contains(searchQuery, ignoreCase = true)

            cocokStatus && cocokCari
        }
    }

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
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Transaksi Pembelian",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // KARTU RINGKASAN PEMBELIAN
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
                                        text = "Total Pembelian Lunas",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = formatRupiah(totalLunas),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "${pembelianList.size} transaksi pembelian",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Hutang Supplier",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = formatRupiah(totalHutang),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFC62828)
                                    )
                                }
                            }
                        }
                    }

                    // PENCARIAN FAKTUR / SUPPLIER / METODE / CATATAN
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Cari nomor faktur, supplier, metode bayar, atau catatan...") },
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

                    // FILTER STATUS ENUM('LUNAS','HUTANG','BATAL')
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

                    // DAFTAR PEMBELIAN
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
                                        "Transaksi pembelian tidak ditemukan."
                                    } else {
                                        "Belum ada transaksi pembelian."
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    } else {
                        items(filteredList, key = { it.id }) { item ->
                            PembelianCard(
                                pembelian = item,
                                onClick = { onPembelianClick(item) }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PembelianCard(
    pembelian: Pembelian,
    onClick: () -> Unit
) {
    val statusUpper = (pembelian.status ?: "LUNAS").uppercase(Locale.getDefault())
    val (badgeBg, badgeText) = when (statusUpper) {
        "LUNAS" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        "HUTANG" -> Color(0xFFFFF3E0) to Color(0xFFEF6C00)
        "BATAL" -> Color(0xFFFFEBEE) to Color(0xFFC62828)
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    val nomorFaktur = pembelian.nomor_faktur ?: "PO-${pembelian.id}"
    val namaSupplier = pembelian.supplier?.nama ?: "Supplier #${pembelian.supplier_id}"
    val metodeBayar = pembelian.metode_pembayaran ?: "TUNAI"
    val subtotal = pembelian.subtotal?.toDouble() ?: pembelian.total.toDouble()
    val diskon = pembelian.diskon?.toDouble() ?: 0.0
    val pajak = pembelian.pajak?.toDouble() ?: 0.0
    val ongkir = pembelian.ongkir?.toDouble() ?: 0.0
    val total = pembelian.total.toDouble()

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
            // BARIS ATAS: NOMOR FAKTUR, TANGGAL & BADGE STATUS
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
                            text = nomorFaktur,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = pembelian.tanggal ?: "-",
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

            // SUPPLIER & METODE PEMBAYARAN + REKENING
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = namaSupplier,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
                        text = "$metodeBayar • Rek #${pembelian.rekening_id}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // RINCIAN SUBTOTAL, DISKON, PAJAK & ONGKIR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Subtotal: ${formatRupiah(subtotal)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Diskon: -${formatRupiah(diskon)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            if (pajak > 0.0 || ongkir > 0.0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Pajak: ${formatRupiah(pajak)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "Ongkir: ${formatRupiah(ongkir)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            if (!pembelian.catatan.isNullOrBlank()) {
                Text(
                    text = "Catatan: ${pembelian.catatan}",
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
                    text = "Total Pembelian",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatRupiah(total),
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

// =============================================================
// PREVIEW ANDROID STUDIO (SPLIT MODE)
// =============================================================

@Preview(
    name = "Pembelian Screen - Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PembelianScreenPreview() {
    val dummyList = listOf(
        Pembelian(
            id = 1L,
            usaha_id = 1L,
            supplier_id = 1L,
            rekening_id = 1L,
            nomor_faktur = "INV-PO-2026-001",
            tanggal = "2026-03-01 09:30:00",
            subtotal = 4500000.0,
            diskon = 100000.0,
            pajak = 0.0,
            ongkir = 100000.0,
            total = 4500000.0,
            metode_pembayaran = "TRANSFER",
            status = "LUNAS",
            catatan = "Barang diterima gudang utama",
            created_by = 1L
        ),
        Pembelian(
            id = 2L,
            usaha_id = 1L,
            supplier_id = 2L,
            rekening_id = 1L,
            nomor_faktur = "INV-PO-2026-002",
            tanggal = "2026-03-03 14:10:00",
            subtotal = 2750000.0,
            diskon = 0.0,
            pajak = 0.0,
            ongkir = 50000.0,
            total = 2800000.0,
            metode_pembayaran = "TUNAI",
            status = "HUTANG",
            catatan = "Tempo 14 hari",
            created_by = 1L
        )
    )

    MaterialTheme {
        PembelianContent(
            isLoading = false,
            errorMessage = null,
            pembelianList = dummyList,
            onPembelianClick = {}
        )
    }
}