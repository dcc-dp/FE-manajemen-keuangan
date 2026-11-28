package com.example.umkmfinance.ui.pengeluaran

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.KategoriPengeluaran
import com.example.umkmfinance.data.model.Pengeluaran
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PengeluaranScreen(
    viewModel: PengeluaranViewModel,
    usahaId: Long = 1L,
    createdBy: Long = 1L,
    initialKategoriId: Long? = null,
    kategoriList: List<KategoriPengeluaran> = defaultKategoriOptions(usahaId),
    onPengeluaranClick: (Pengeluaran) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(usahaId) {
        viewModel.loadPengeluaran(usahaId)
    }

    PengeluaranContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        usahaId = usahaId,
        pengeluaranList = uiState.data,
        kategoriList = kategoriList,
        initialKategoriId = initialKategoriId,
        onPengeluaranClick = onPengeluaranClick,
        onSimpanPengeluaran = { kategori, rekeningId, tanggal, nominal, keterangan ->
            viewModel.tambahPengeluaran(
                usahaId = usahaId,
                kategori = kategori,
                rekeningId = rekeningId,
                tanggal = tanggal,
                nominal = nominal,
                keterangan = keterangan,
                createdBy = createdBy
            )
        }
    )
}

@Composable
fun PengeluaranContent(
    isLoading: Boolean,
    errorMessage: String?,
    usahaId: Long = 1L,
    pengeluaranList: List<Pengeluaran>,
    kategoriList: List<KategoriPengeluaran>,
    initialKategoriId: Long? = null,
    onPengeluaranClick: (Pengeluaran) -> Unit = {},
    onSimpanPengeluaran: (
        kategori: KategoriPengeluaran,
        rekeningId: Long,
        tanggal: String,
        nominal: Double,
        keterangan: String?
    ) -> Unit = { _, _, _, _, _ -> }
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedKategoriId by remember(initialKategoriId) { mutableStateOf(initialKategoriId) }
    var showTambahDialog by remember { mutableStateOf(false) }

    val activeKategori = remember(kategoriList, selectedKategoriId) {
        kategoriList.find { it.id == selectedKategoriId }
    }

    val filteredList = remember(pengeluaranList, kategoriList, searchQuery, selectedKategoriId) {
        pengeluaranList.filter { item ->
            val cocokKategori = if (selectedKategoriId == null) {
                true
            } else {
                val idCocok = item.kategori_pengeluaran_id == selectedKategoriId ||
                        item.kategori_pengeluaran?.id == selectedKategoriId
                val namaTarget = activeKategori?.nama?.trim()?.lowercase(Locale.getDefault())
                val namaItem = item.kategori_pengeluaran?.nama?.trim()?.lowercase(Locale.getDefault())
                idCocok || (namaTarget != null && namaItem != null && namaTarget == namaItem)
            }

            val namaKategori = item.kategori_pengeluaran?.nama
                ?: kategoriList.find { it.id == item.kategori_pengeluaran_id }?.nama
                ?: "Kategori #${item.kategori_pengeluaran_id}"
            val keterangan = item.keterangan ?: ""

            val cocokCari = searchQuery.isBlank() ||
                    namaKategori.contains(searchQuery, ignoreCase = true) ||
                    keterangan.contains(searchQuery, ignoreCase = true) ||
                    item.tanggal.contains(searchQuery, ignoreCase = true)

            cocokKategori && cocokCari
        }
    }

    val totalDitampilkan = remember(filteredList) {
        filteredList.sumOf { it.nominal }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showTambahDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Pengeluaran"
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
                isLoading && pengeluaranList.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null && pengeluaranList.isEmpty() -> {
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
                                text = activeKategori?.let { "Pengeluaran • ${it.nama}" } ?: "Pengeluaran",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // KARTU RINGKASAN PENGELUARAN
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.65f)
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = activeKategori?.let {
                                            "Riwayat ${it.nama} • Usaha #$usahaId"
                                        } ?: "Total Semua Pengeluaran • Usaha #$usahaId",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    Text(
                                        text = formatRupiah(totalDitampilkan),
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFC62828)
                                    )
                                    Text(
                                        text = "${filteredList.size} riwayat pengeluaran",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }

                        // PENCARIAN
                        item {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Cari keterangan, nominal, atau tanggal...") },
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

                        // FILTER PER KATEGORI PENGELUARAN
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = selectedKategoriId == null,
                                    onClick = { selectedKategoriId = null },
                                    label = { Text("SEMUA") }
                                )
                                kategoriList.forEach { kategori ->
                                    FilterChip(
                                        selected = selectedKategoriId == kategori.id,
                                        onClick = { selectedKategoriId = kategori.id },
                                        label = { Text(kategori.nama) }
                                    )
                                }
                            }
                        }

                        // DAFTAR RIWAYAT PENGELUARAN
                        if (filteredList.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = activeKategori?.let {
                                            "Belum ada riwayat pengeluaran untuk kategori ${it.nama}."
                                        } ?: "Belum ada catatan pengeluaran.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        } else {
                            items(filteredList, key = { it.id }) { item ->
                                val namaKategoriItem = item.kategori_pengeluaran?.nama
                                    ?: kategoriList.find { it.id == item.kategori_pengeluaran_id }?.nama
                                    ?: "Kategori #${item.kategori_pengeluaran_id}"

                                PengeluaranCard(
                                    pengeluaran = item,
                                    namaKategori = namaKategoriItem,
                                    onClick = { onPengeluaranClick(item) }
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

        if (showTambahDialog) {
            TambahPengeluaranDialog(
                kategoriList = kategoriList,
                defaultKategori = activeKategori ?: kategoriList.firstOrNull(),
                onDismiss = { showTambahDialog = false },
                onSimpan = { kategori, rekeningId, tanggal, nominal, keterangan ->
                    onSimpanPengeluaran(kategori, rekeningId, tanggal, nominal, keterangan)
                    showTambahDialog = false
                }
            )
        }
    }
}

@Composable
private fun PengeluaranCard(
    pengeluaran: Pengeluaran,
    namaKategori: String,
    onClick: () -> Unit
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = namaKategori,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "Rek #${pengeluaran.rekening_id}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Text(
                text = pengeluaran.tanggal,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            if (!pengeluaran.keterangan.isNullOrBlank()) {
                Text(
                    text = pengeluaran.keterangan,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nominal Pengeluaran",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatRupiah(pengeluaran.nominal),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828)
                )
            }
        }
    }
}

@Composable
private fun TambahPengeluaranDialog(
    kategoriList: List<KategoriPengeluaran>,
    defaultKategori: KategoriPengeluaran?,
    onDismiss: () -> Unit,
    onSimpan: (
        kategori: KategoriPengeluaran,
        rekeningId: Long,
        tanggal: String,
        nominal: Double,
        keterangan: String?
    ) -> Unit
) {
    val defaultTanggal = remember {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID")).format(Date())
    }
    var selectedKategori by remember(kategoriList, defaultKategori) {
        mutableStateOf(defaultKategori ?: kategoriList.firstOrNull())
    }
    var rekeningIdInput by remember { mutableStateOf("1") }
    var tanggal by remember { mutableStateOf(defaultTanggal) }
    var nominalInput by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Tambah Pengeluaran",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Pilih Kategori Pengeluaran",
                    style = MaterialTheme.typography.labelMedium
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    kategoriList.forEach { kategori ->
                        FilterChip(
                            selected = selectedKategori?.id == kategori.id,
                            onClick = { selectedKategori = kategori },
                            label = { Text(kategori.nama) }
                        )
                    }
                }

                OutlinedTextField(
                    value = nominalInput,
                    onValueChange = { nominalInput = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nominal (Rp)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = tanggal,
                        onValueChange = { tanggal = it },
                        modifier = Modifier.weight(1.3f),
                        label = { Text("Tanggal") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = rekeningIdInput,
                        onValueChange = { rekeningIdInput = it.filter { ch -> ch.isDigit() } },
                        modifier = Modifier.weight(0.7f),
                        label = { Text("Rek ID") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                OutlinedTextField(
                    value = keterangan,
                    onValueChange = { keterangan = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Keterangan (Opsional)") },
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val kategori = selectedKategori ?: return@TextButton
                    val nominal = nominalInput.toDoubleOrNull() ?: 0.0
                    val rekeningId = rekeningIdInput.toLongOrNull() ?: 1L
                    if (nominal > 0.0) {
                        onSimpan(
                            kategori,
                            rekeningId,
                            tanggal.trim().ifBlank { defaultTanggal },
                            nominal,
                            keterangan.trim().ifBlank { null }
                        )
                    }
                }
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

private fun formatRupiah(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

private fun defaultKategoriOptions(usahaId: Long): List<KategoriPengeluaran> = listOf(
    KategoriPengeluaran(id = 1L, usaha_id = usahaId, nama = "Listrik & Air"),
    KategoriPengeluaran(id = 2L, usaha_id = usahaId, nama = "Gaji Karyawan"),
    KategoriPengeluaran(id = 3L, usaha_id = usahaId, nama = "Kemasan & Perlengkapan"),
    KategoriPengeluaran(id = 4L, usaha_id = usahaId, nama = "Transportasi & Kirim")
)

@Preview(
    name = "Pengeluaran Screen - Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PengeluaranScreenPreview() {
    val kategoriSample = defaultKategoriOptions(1L)
    val dummyList = listOf(
        Pengeluaran(
            id = 1L,
            usaha_id = 1L,
            kategori_pengeluaran_id = 1L,
            rekening_id = 1L,
            tanggal = "2026-11-28 08:30:00",
            nominal = 350000.0,
            keterangan = "Tagihan listrik & air bulan ini",
            created_by = 1L,
            kategori_pengeluaran = kategoriSample[0]
        ),
        Pengeluaran(
            id = 2L,
            usaha_id = 1L,
            kategori_pengeluaran_id = 1L,
            rekening_id = 1L,
            tanggal = "2026-11-15 10:00:00",
            nominal = 95000.0,
            keterangan = "Isi ulang token listrik & air galon/PDAM",
            created_by = 1L,
            kategori_pengeluaran = kategoriSample[0]
        ),
        Pengeluaran(
            id = 3L,
            usaha_id = 1L,
            kategori_pengeluaran_id = 3L,
            rekening_id = 1L,
            tanggal = "2026-11-28 11:15:00",
            nominal = 125000.0,
            keterangan = "Plastik kemasan & lakban",
            created_by = 1L,
            kategori_pengeluaran = kategoriSample[2]
        )
    )

    MaterialTheme {
        PengeluaranContent(
            isLoading = false,
            errorMessage = null,
            usahaId = 1L,
            pengeluaranList = dummyList,
            kategoriList = kategoriSample,
            initialKategoriId = 1L
        )
    }
}