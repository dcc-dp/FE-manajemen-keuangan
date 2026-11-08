package com.example.umkmfinance.ui.produk

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.Produk
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProdukScreen(
    viewModel: ProdukViewModel,
    onProdukClick: (Produk) -> Unit,
    onTambahProdukClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProduk()
    }

    ProdukContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        produkList = uiState.data,
        onProdukClick = onProdukClick,
        onTambahProdukClick = onTambahProdukClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdukContent(
    isLoading: Boolean,
    errorMessage: String?,
    produkList: List<Produk>,
    onProdukClick: (Produk) -> Unit,
    onTambahProdukClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    // Logika Filter dan Search
    val filteredList = remember(produkList, searchQuery, selectedFilter) {
        produkList.filter { produk ->
            val matchesSearch = produk.nama.contains(searchQuery, ignoreCase = true) ||
                    produk.kode_produk.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "Stok Menipis" -> produk.stok in 1..5
                "Habis" -> produk.stok <= 0
                "Aktif" -> produk.status.equals("ACTIVE", ignoreCase = true) || produk.status.equals("AKTIF", ignoreCase = true)
                else -> true
            }

            matchesSearch && matchesFilter
        }
    }

    // Kalkulasi Nilai Aset Persediaan Produk
    val totalNilaiAset = remember(produkList) {
        produkList.sumOf { it.harga_modal * it.stok }
    }
    val totalUnitStok = remember(produkList) {
        produkList.sumOf { it.stok }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onTambahProdukClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Produk"
                )
            }
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
                                text = "Katalog Produk",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // RINGKASAN NILAI ASET
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
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
                                    Column {
                                        Text(
                                            text = "Nilai Aset Persediaan",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = formatRupiah(totalNilaiAset),
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Total Item",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "$totalUnitStok unit",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }
                        }

                        // SEARCH BAR
                        item {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Cari nama atau kode produk...") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Hapus teks"
                                            )
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // FILTER CHIP KATEGORI / STATUS
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val filters = listOf("Semua", "Aktif", "Stok Menipis", "Habis")
                                filters.forEach { filter ->
                                    FilterChip(
                                        selected = selectedFilter == filter,
                                        onClick = { selectedFilter = filter },
                                        label = { Text(filter) }
                                    )
                                }
                            }
                        }

                        // JUMLAH PRODUK TERFILTER
                        item {
                            Text(
                                text = "Menampilkan ${filteredList.size} dari ${produkList.size} produk",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        // DAFTAR PRODUK
                        if (filteredList.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (searchQuery.isNotEmpty()) "Produk tidak ditemukan." else "Belum ada produk pada kategori ini.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        } else {
                            items(filteredList) { produk ->
                                ProdukCard(
                                    nama = produk.nama,
                                    kodeProduk = produk.kode_produk,
                                    hargaJual = produk.harga_jual,
                                    hargaModal = produk.harga_modal,
                                    stok = produk.stok,
                                    satuan = produk.satuan,
                                    status = produk.status,
                                    onClick = { onProdukClick(produk) }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp)) // Padding agar tidak tertutup FAB
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProdukCard(
    nama: String,
    kodeProduk: String,
    hargaJual: Double,
    hargaModal: Double,
    stok: Int,
    satuan: String,
    status: String,
    onClick: () -> Unit
) {
    // Penentuan badge kondisi stok
    val isStokHabis = stok <= 0
    val isStokMenipis = stok in 1..5

    val stokBadgeColor = when {
        isStokHabis -> Color(0xFFC62828)
        isStokMenipis -> Color(0xFFE65100)
        else -> Color(0xFF2E7D32)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Baris: Nama & Status Active
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = nama,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Kode: $kodeProduk",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                // Badge Status
                SuggestionChip(
                    onClick = {},
                    label = {
                        Text(
                            text = status.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        labelColor = if (status.equals("ACTIVE", true) || status.equals("AKTIF", true)) Color(0xFF1B5E20) else Color(0xFF616161)
                    )
                )
            }

            // Baris Harga Jual dan Modal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Harga Jual",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = formatRupiah(hargaJual),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Harga Modal",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = formatRupiah(hargaModal),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Baris Status Stok dengan Peringatan Warna
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isStokMenipis || isStokHabis) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Peringatan Stok",
                            tint = stokBadgeColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                    }
                    Text(
                        text = when {
                            isStokHabis -> "Stok Habis"
                            isStokMenipis -> "Stok Menipis: $stok $satuan"
                            else -> "Tersedia: $stok $satuan"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isStokMenipis || isStokHabis) FontWeight.Bold else FontWeight.Normal,
                        color = stokBadgeColor
                    )
                }

                // Perkiraan Laba Satuan
                val profit = (hargaJual - hargaModal).coerceAtLeast(0.0)
                Text(
                    text = "Laba/unit: ${formatRupiah(profit)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF2E7D32)
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

@Preview(name = "Produk Screen - Berisi Data", showBackground = true, showSystemUi = true)
@Composable
private fun ProdukScreenFullPreview() {
    val dummy = listOf(
        Produk(
            id = 1L,
            nama = "Es Teh Manis Jumbo",
            kode_produk = "PRD-001",
            harga_jual = 5000.0,
            harga_modal = 2500.0,
            stok = 25,
            satuan = "Gelas",
            status = "ACTIVE"
        ),
        Produk(
            id = 2L,
            nama = "Kopi Susu Aren",
            kode_produk = "PRD-002",
            harga_jual = 15000.0,
            harga_modal = 8000.0,
            stok = 3, // Memicu badge stok menipis
            satuan = "Cup",
            status = "ACTIVE"
        ),
        Produk(
            id = 3L,
            nama = "Roti Panggang Keju",
            kode_produk = "PRD-003",
            harga_jual = 12000.0,
            harga_modal = 6000.0,
            stok = 0, // Memicu badge stok habis
            satuan = "Porsi",
            status = "ACTIVE"
        )
    )

    MaterialTheme {
        ProdukContent(
            isLoading = false,
            errorMessage = null,
            produkList = dummy,
            onProdukClick = {},
            onTambahProdukClick = {}
        )
    }
}