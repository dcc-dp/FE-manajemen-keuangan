package com.example.umkmfinance.ui.kategori

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.KategoriProduk

@Composable
fun KategoriProdukScreen(
    viewModel: KategoriProdukViewModel,
    onNavigateBack: () -> Unit = {},
    onKategoriClick: (KategoriProduk) -> Unit = {},
    onSemuaProdukClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadKategori()
    }

    KategoriProdukContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        kategoriList = uiState.data,
        onNavigateBack = onNavigateBack,
        onKategoriClick = onKategoriClick,
        onSemuaProdukClick = onSemuaProdukClick,
        onSimpanKategori = { kategori ->
            viewModel.simpanKategori(kategori) {}
        },
        onHapusKategori = { id ->
            viewModel.hapusKategori(id)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KategoriProdukContent(
    isLoading: Boolean,
    errorMessage: String?,
    kategoriList: List<KategoriProduk>,
    onNavigateBack: () -> Unit,
    onKategoriClick: (KategoriProduk) -> Unit,
    onSemuaProdukClick: () -> Unit,
    onSimpanKategori: (KategoriProduk) -> Unit,
    onHapusKategori: (Long) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showFormDialog by remember { mutableStateOf(false) }
    var kategoriToEdit by remember { mutableStateOf<KategoriProduk?>(null) }
    var kategoriToDelete by remember { mutableStateOf<KategoriProduk?>(null) }

    val filteredList = remember(kategoriList, searchQuery) {
        kategoriList.filter {
            it.nama.contains(searchQuery, ignoreCase = true) ||
                    (it.deskripsi?.contains(searchQuery, ignoreCase = true) == true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kategori Produk", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    kategoriToEdit = null
                    showFormDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Kategori")
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
                        // SEARCH BAR
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Cari kategori produk...") },
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

                        // KARTU SHORTCUT: LIHAT SEMUA PRODUK
                        item {
                            OutlinedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSemuaProdukClick() },
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Inventory2,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Column {
                                            Text(
                                                text = "Semua Produk",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "Tampilkan seluruh item tanpa filter kategori",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null
                                    )
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Pilih Kategori untuk Kelola Produk (${filteredList.size})",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        // LIST KATEGORI (BISA DIKLIK UNTUK MASUK KE PRODUK)
                        if (filteredList.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (searchQuery.isNotEmpty()) "Kategori tidak ditemukan." else "Belum ada kategori produk.",
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        } else {
                            items(filteredList, key = { it.id }) { kategori ->
                                KategoriCard(
                                    kategori = kategori,
                                    onCardClick = { onKategoriClick(kategori) },
                                    onEditClick = {
                                        kategoriToEdit = kategori
                                        showFormDialog = true
                                    },
                                    onDeleteClick = {
                                        kategoriToDelete = kategori
                                    }
                                )
                            }
                        }

                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }

    if (showFormDialog) {
        FormKategoriDialog(
            kategoriToEdit = kategoriToEdit,
            onDismiss = { showFormDialog = false },
            onSimpan = { kategoriBaru ->
                onSimpanKategori(kategoriBaru)
                showFormDialog = false
            }
        )
    }

    kategoriToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { kategoriToDelete = null },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Hapus Kategori?") },
            text = { Text("Yakin ingin menghapus kategori \"${target.nama}\"?") },
            confirmButton = {
                Button(
                    onClick = {
                        onHapusKategori(target.id)
                        kategoriToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { kategoriToDelete = null }) { Text("Batal") }
            }
        )
    }
}

@Composable
private fun KategoriCard(
    kategori: KategoriProduk,
    onCardClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Category,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Column {
                    Text(
                        text = kategori.nama,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (!kategori.deskripsi.isNullOrBlank()) {
                        Text(
                            text = kategori.deskripsi,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            maxLines = 2
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Kategori",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Kategori",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Buka Produk",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun FormKategoriDialog(
    kategoriToEdit: KategoriProduk?,
    onDismiss: () -> Unit,
    onSimpan: (KategoriProduk) -> Unit
) {
    val isEdit = kategoriToEdit != null
    var nama by remember { mutableStateOf(kategoriToEdit?.nama ?: "") }
    var deskripsi by remember { mutableStateOf(kategoriToEdit?.deskripsi ?: "") }
    var namaError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEdit) "Edit Kategori Produk" else "Tambah Kategori Baru",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nama,
                    onValueChange = {
                        nama = it
                        if (it.isNotBlank()) namaError = false
                    },
                    label = { Text("Nama Kategori *") },
                    placeholder = { Text("Contoh: Minuman Dingin") },
                    isError = namaError,
                    supportingText = {
                        if (namaError) Text("Nama kategori wajib diisi")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    label = { Text("Deskripsi (Opsional)") },
                    placeholder = { Text("Keterangan jenis produk dalam kategori ini") },
                    minLines = 2,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nama.isBlank()) {
                        namaError = true
                    } else {
                        onSimpan(
                            KategoriProduk(
                                id = kategoriToEdit?.id ?: 0L,
                                usaha_id = kategoriToEdit?.usaha_id ?: 1L,
                                nama = nama.trim(),
                                deskripsi = deskripsi.trim().ifEmpty { null }
                            )
                        )
                    }
                }
            ) {
                Text(if (isEdit) "Perbarui" else "Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

// =============================================================
// PREVIEW SPLIT MODE ANDROID STUDIO
// =============================================================

@Preview(
    name = "Kategori Produk - Split Mode",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun KategoriProdukScreenPreview() {
    val dummyKategori = listOf(
        KategoriProduk(
            id = 1L,
            usaha_id = 1L,
            nama = "Minuman Kopi",
            deskripsi = "Varian kopi espresso base, manual brew, dan kopi susu"
        ),
        KategoriProduk(
            id = 2L,
            usaha_id = 1L,
            nama = "Makanan Berat",
            deskripsi = "Menu utama nasi, mie, dan hidangan hangat"
        ),
        KategoriProduk(
            id = 3L,
            usaha_id = 1L,
            nama = "Snack & Camilan",
            deskripsi = "Kue basah, roti bakar, dan gorengan"
        )
    )

    MaterialTheme {
        KategoriProdukContent(
            isLoading = false,
            errorMessage = null,
            kategoriList = dummyKategori,
            onNavigateBack = {},
            onKategoriClick = {},
            onSemuaProdukClick = {},
            onSimpanKategori = {},
            onHapusKategori = {}
        )
    }
}