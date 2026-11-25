package com.example.umkmfinance.ui.produk

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.Produk

@Composable
fun EditProdukScreen(
    produkId: Long,
    viewModel: ProdukViewModel,
    onNavigateBack: () -> Unit = {},
    onSimpanSukses: () -> Unit = {}
) {
    val detailState by viewModel.detailUiState.collectAsState()
    var isSaving by remember { mutableStateOf(false) }
    var saveError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(produkId) {
        viewModel.loadProdukById(produkId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            detailState.isLoading && detailState.data == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            detailState.errorMessage != null && detailState.data == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = detailState.errorMessage ?: "Gagal memuat data produk")
                }
            }

            detailState.data != null -> {
                val produkAsli = detailState.data!!
                EditProdukForm(
                    produkAwal = produkAsli,
                    isSaving = isSaving,
                    externalError = saveError,
                    onNavigateBack = onNavigateBack,
                    onSimpanPerubahan = { produkDiubah ->
                        isSaving = true
                        saveError = null
                        viewModel.updateProduk(
                            id = produkAsli.id,
                            produk = produkDiubah,
                            onSuccess = {
                                isSaving = false
                                onSimpanSukses()
                            },
                            onError = { pesanError ->
                                isSaving = false
                                saveError = pesanError
                            }
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProdukForm(
    produkAwal: Produk,
    isSaving: Boolean = false,
    externalError: String? = null,
    onNavigateBack: () -> Unit = {},
    onSimpanPerubahan: (Produk) -> Unit = {}
) {
    var nama by remember(produkAwal.id) { mutableStateOf(produkAwal.nama) }
    var kodeProduk by remember(produkAwal.id) { mutableStateOf(produkAwal.kode_produk) }
    var barcode by remember(produkAwal.id) { mutableStateOf(produkAwal.barcode ?: "") }
    var kategoriIdText by remember(produkAwal.id) { mutableStateOf(produkAwal.kategori_id.toString()) }
    var hargaModalText by remember(produkAwal.id) {
        mutableStateOf(
            if (produkAwal.harga_modal % 1.0 == 0.0) produkAwal.harga_modal.toLong().toString()
            else produkAwal.harga_modal.toString()
        )
    }
    var hargaJualText by remember(produkAwal.id) {
        mutableStateOf(
            if (produkAwal.harga_jual % 1.0 == 0.0) produkAwal.harga_jual.toLong().toString()
            else produkAwal.harga_jual.toString()
        )
    }
    var stokText by remember(produkAwal.id) { mutableStateOf(produkAwal.stok.toString()) }
    var satuan by remember(produkAwal.id) { mutableStateOf(produkAwal.satuan) }
    var status by remember(produkAwal.id) {
        mutableStateOf(
            if (produkAwal.status.equals("ACTIVE", true) || produkAwal.status.equals("AKTIF", true)) "AKTIF"
            else "NONAKTIF"
        )
    }

    var namaError by remember { mutableStateOf(false) }
    var kodeError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Produk",
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            if (externalError != null) {
                Text(
                    text = externalError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

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

            OutlinedTextField(
                value = kodeProduk,
                onValueChange = {
                    kodeProduk = it
                    if (it.isNotBlank()) kodeError = false
                },
                label = { Text("Kode Produk *") },
                isError = kodeError,
                supportingText = {
                    if (kodeError) Text("Kode produk wajib diisi")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = kategoriIdText,
                    onValueChange = { kategoriIdText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("ID Kategori") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = barcode,
                    onValueChange = { barcode = it },
                    label = { Text("Barcode (Opsional)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = hargaModalText,
                    onValueChange = { hargaModalText = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    label = { Text("Harga Modal *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = hargaJualText,
                    onValueChange = { hargaJualText = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    label = { Text("Harga Jual *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = stokText,
                    onValueChange = { stokText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Stok *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = satuan,
                    onValueChange = { satuan = it },
                    label = { Text("Satuan *") },
                    placeholder = { Text("Cup / Porsi / Pcs") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Text(
                text = "Status Produk",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("AKTIF", "NONAKTIF").forEach { opsiStatus ->
                    FilterChip(
                        selected = status == opsiStatus,
                        onClick = { status = opsiStatus },
                        label = { Text(opsiStatus) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    namaError = nama.isBlank()
                    kodeError = kodeProduk.isBlank()
                    if (!namaError && !kodeError) {
                        val produkBaru = produkAwal.copy(
                            nama = nama.trim(),
                            kode_produk = kodeProduk.trim(),
                            barcode = barcode.trim().ifEmpty { null },
                            kategori_id = kategoriIdText.toLongOrNull() ?: produkAwal.kategori_id,
                            harga_modal = hargaModalText.toDoubleOrNull() ?: produkAwal.harga_modal,
                            harga_jual = hargaJualText.toDoubleOrNull() ?: produkAwal.harga_jual,
                            stok = stokText.toIntOrNull() ?: produkAwal.stok,
                            satuan = satuan.trim().ifEmpty { produkAwal.satuan },
                            status = status
                        )
                        onSimpanPerubahan(produkBaru)
                    }
                },
                enabled = !isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(strokeWidth = 2.dp)
                } else {
                    Text(
                        text = "Simpan Perubahan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(name = "Edit Produk - Split Mode", showBackground = true, showSystemUi = true)
@Composable
private fun EditProdukScreenPreview() {
    MaterialTheme {
        EditProdukForm(
            produkAwal = Produk(
                id = 1L,
                usaha_id = 1L,
                kategori_id = 1L,
                kode_produk = "MK-001",
                barcode = "899123456001",
                nama = "Es Kopi Susu Gula Aren",
                harga_modal = 9000.0,
                harga_jual = 18000.0,
                stok = 24,
                satuan = "Cup",
                status = "AKTIF"
            )
        )
    }
}