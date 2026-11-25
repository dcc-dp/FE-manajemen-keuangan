package com.example.umkmfinance.ui.kategori

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.KategoriProduk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahKategoriProdukScreen(
    kategoriToEdit: KategoriProduk? = null,
    onNavigateBack: () -> Unit,
    onSimpanKategori: (KategoriProduk) -> Unit
) {
    val isEditMode = kategoriToEdit != null

    var nama by remember { mutableStateOf(kategoriToEdit?.nama ?: "") }
    var deskripsi by remember { mutableStateOf(kategoriToEdit?.deskripsi ?: "") }
    var namaError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "Edit Kategori Produk" else "Tambah Kategori Produk",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = nama,
                onValueChange = {
                    nama = it
                    if (it.isNotBlank()) namaError = false
                },
                label = { Text("Nama Kategori *") },
                placeholder = { Text("Contoh: Minuman Kopi") },
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
                placeholder = { Text("Contoh: Varian kopi espresso base & manual brew") },
                minLines = 3,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    namaError = nama.isBlank()
                    if (!namaError) {
                        val kategoriBaru = KategoriProduk(
                            id = kategoriToEdit?.id ?: 0L,
                            usaha_id = kategoriToEdit?.usaha_id ?: 1L,
                            nama = nama.trim(),
                            deskripsi = deskripsi.trim().ifEmpty { null }
                        )
                        onSimpanKategori(kategoriBaru)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (isEditMode) "Perbarui Kategori" else "Simpan Kategori",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(name = "Tambah Kategori Produk", showBackground = true, showSystemUi = true)
@Composable
private fun TambahKategoriProdukScreenPreview() {
    MaterialTheme {
        TambahKategoriProdukScreen(
            kategoriToEdit = null,
            onNavigateBack = {},
            onSimpanKategori = {}
        )
    }
}