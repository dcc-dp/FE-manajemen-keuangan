package com.example.umkmfinance.ui.pengeluaran

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.KategoriPengeluaran

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KategoriPengeluaranScreen(
    usahaId: Long = 1L,
    kategoriList: List<KategoriPengeluaran> = defaultKategoriPengeluaran(usahaId),
    onKategoriClick: (KategoriPengeluaran) -> Unit = {},
    onSimpanKategori: (nama: String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var showTambahDialog by remember { mutableStateOf(false) }
    var namaBaru by remember { mutableStateOf("") }

    val filteredList = remember(kategoriList, searchQuery) {
        if (searchQuery.isBlank()) {
            kategoriList
        } else {
            kategoriList.filter { it.nama.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    namaBaru = ""
                    showTambahDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Kategori Pengeluaran"
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Kategori Pengeluaran",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${kategoriList.size} kategori • Usaha #$usahaId",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Cari nama kategori pengeluaran...") },
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

                if (filteredList.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Belum ada kategori pengeluaran.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                } else {
                    items(filteredList, key = { it.id }) { item ->
                        KategoriPengeluaranCard(
                            kategori = item,
                            onClick = { onKategoriClick(item) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        if (showTambahDialog) {
            AlertDialog(
                onDismissRequest = { showTambahDialog = false },
                title = {
                    Text(
                        text = "Tambah Kategori Pengeluaran",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    OutlinedTextField(
                        value = namaBaru,
                        onValueChange = { namaBaru = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nama Kategori") },
                        placeholder = { Text("Contoh: Listrik & Air") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val bersih = namaBaru.trim()
                            if (bersih.isNotEmpty()) {
                                onSimpanKategori(bersih)
                                showTambahDialog = false
                            }
                        }
                    ) {
                        Text("Simpan")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTambahDialog = false }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

@Composable
private fun KategoriPengeluaranCard(
    kategori: KategoriPengeluaran,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = kategori.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "ID #${kategori.id} • Usaha #${kategori.usaha_id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

private fun defaultKategoriPengeluaran(usahaId: Long): List<KategoriPengeluaran> = listOf(
    KategoriPengeluaran(id = 1L, usaha_id = usahaId, nama = "Listrik & Air"),
    KategoriPengeluaran(id = 2L, usaha_id = usahaId, nama = "Gaji Karyawan"),
    KategoriPengeluaran(id = 3L, usaha_id = usahaId, nama = "Kemasan & Perlengkapan"),
    KategoriPengeluaran(id = 4L, usaha_id = usahaId, nama = "Transportasi & Kirim")
)

@Preview(
    name = "Kategori Pengeluaran - Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun KategoriPengeluaranPreview() {
    MaterialTheme {
        KategoriPengeluaranScreen()
    }
}