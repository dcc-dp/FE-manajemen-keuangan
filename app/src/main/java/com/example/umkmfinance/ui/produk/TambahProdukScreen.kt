package com.example.umkmfinance.ui.produk

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.Produk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahProdukScreen(
    onNavigateBack: () -> Unit,
    onSimpanProduk: (Produk) -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var kodeProduk by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var hargaModalText by remember { mutableStateOf("") }
    var hargaJualText by remember { mutableStateOf("") }
    var stokText by remember { mutableStateOf("") }
    var satuan by remember { mutableStateOf("Pcs") }
    var status by remember { mutableStateOf("ACTIVE") }

    // Error state untuk validasi input
    var namaError by remember { mutableStateOf(false) }
    var hargaJualError by remember { mutableStateOf(false) }
    var stokError by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tambah Produk Baru",
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
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Nama Produk
            OutlinedTextField(
                value = nama,
                onValueChange = {
                    nama = it
                    if (it.isNotBlank()) namaError = false
                },
                label = { Text("Nama Produk *") },
                isError = namaError,
                supportingText = {
                    if (namaError) Text("Nama produk wajib diisi")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            // Kode Produk & Barcode
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = kodeProduk,
                    onValueChange = { kodeProduk = it },
                    label = { Text("Kode (Opsional)") },
                    placeholder = { Text("PRD-001") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = barcode,
                    onValueChange = { barcode = it },
                    label = { Text("Barcode (Opsional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // Harga Modal & Harga Jual
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = hargaModalText,
                    onValueChange = { hargaModalText = it.filter { char -> char.isDigit() } },
                    label = { Text("Harga Modal (Rp)") },
                    placeholder = { Text("0") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = hargaJualText,
                    onValueChange = {
                        hargaJualText = it.filter { char -> char.isDigit() }
                        if (it.isNotBlank()) hargaJualError = false
                    },
                    label = { Text("Harga Jual (Rp) *") },
                    placeholder = { Text("0") },
                    isError = hargaJualError,
                    supportingText = {
                        if (hargaJualError) Text("Wajib diisi")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // Stok & Satuan
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = stokText,
                    onValueChange = {
                        stokText = it.filter { char -> char.isDigit() }
                        if (it.isNotBlank()) stokError = false
                    },
                    label = { Text("Stok Awal *") },
                    placeholder = { Text("0") },
                    isError = stokError,
                    supportingText = {
                        if (stokError) Text("Wajib diisi")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = satuan,
                    onValueChange = { satuan = it },
                    label = { Text("Satuan") },
                    placeholder = { Text("Pcs, Porsi, Gelas") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // Status Produk
            Text(
                text = "Status Produk",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = status == "ACTIVE",
                    onClick = { status = "ACTIVE" },
                    label = { Text("Aktif Dijual") }
                )
                FilterChip(
                    selected = status == "INACTIVE",
                    onClick = { status = "INACTIVE" },
                    label = { Text("Non-Aktif / Draft") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Simpan
            Button(
                onClick = {
                    namaError = nama.isBlank()
                    hargaJualError = hargaJualText.isBlank()
                    stokError = stokText.isBlank()

                    if (!namaError && !hargaJualError && !stokError) {
                        val produkBaru = Produk(
                            nama = nama.trim(),
                            kode_produk = if (kodeProduk.isNotBlank()) kodeProduk.trim() else "PRD-${System.currentTimeMillis() % 10000}",
                            barcode = barcode.trim().ifEmpty { null },
                            harga_modal = hargaModalText.toDoubleOrNull() ?: 0.0,
                            harga_jual = hargaJualText.toDoubleOrNull() ?: 0.0,
                            stok = stokText.toIntOrNull() ?: 0,
                            satuan = satuan.trim().ifEmpty { "Pcs" },
                            status = status
                        )
                        onSimpanProduk(produkBaru)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Simpan Produk",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// =============================================================
// PREVIEW ANDROID STUDIO
// =============================================================
@Preview(
    name = "Tambah Produk Screen",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun TambahProdukScreenPreview() {
    MaterialTheme {
        TambahProdukScreen(
            onNavigateBack = {},
            onSimpanProduk = {}
        )
    }
}