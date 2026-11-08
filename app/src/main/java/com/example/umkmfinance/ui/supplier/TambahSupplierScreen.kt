package com.example.umkmfinance.ui.supplier

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
import com.example.umkmfinance.data.model.Supplier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahSupplierScreen(
    supplierToEdit: Supplier? = null,
    onNavigateBack: () -> Unit,
    onSimpanSupplier: (Supplier) -> Unit
) {
    val isEditMode = supplierToEdit != null

    var nama by remember { mutableStateOf(supplierToEdit?.nama ?: "") }
    var namaKontak by remember { mutableStateOf(supplierToEdit?.nama_kontak ?: "") }
    var telepon by remember { mutableStateOf(supplierToEdit?.telepon ?: "") }
    var email by remember { mutableStateOf(supplierToEdit?.email ?: "") }
    var alamat by remember { mutableStateOf(supplierToEdit?.alamat ?: "") }
    var catatan by remember { mutableStateOf(supplierToEdit?.catatan ?: "") }

    var namaError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "Edit Data Supplier" else "Tambah Supplier Baru",
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
                label = { Text("Nama Toko / Distributor / Supplier *") },
                placeholder = { Text("Contoh: Agen Kopi Nusantara") },
                isError = namaError,
                supportingText = {
                    if (namaError) Text("Nama supplier wajib diisi")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = namaKontak,
                    onValueChange = { namaKontak = it },
                    label = { Text("Nama Sales / PIC") },
                    placeholder = { Text("Pak Rian") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = telepon,
                    onValueChange = { input ->
                        telepon = input.filter { it.isDigit() || it == '+' }
                    },
                    label = { Text("No. WhatsApp") },
                    placeholder = { Text("08123456789") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email (Opsional)") },
                placeholder = { Text("sales@supplier.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = alamat,
                onValueChange = { alamat = it },
                label = { Text("Alamat Gudang / Kantor Fisik") },
                placeholder = { Text("Jl. Perintis Kemerdekaan KM 9") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = catatan,
                onValueChange = { catatan = it },
                label = { Text("Info Rekening & Ketentuan Supplier") },
                placeholder = { Text("BCA 12345678 a/n PT ABC, kirim tiap Selasa & Jumat") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    namaError = nama.isBlank()
                    if (!namaError) {
                        val supplierResult = Supplier(
                            id = supplierToEdit?.id ?: 0L,
                            usaha_id = supplierToEdit?.usaha_id ?: 1L,
                            nama = nama.trim(),
                            nama_kontak = namaKontak.trim().ifEmpty { null },
                            telepon = telepon.trim().ifEmpty { null },
                            email = email.trim().ifEmpty { null },
                            alamat = alamat.trim().ifEmpty { null },
                            catatan = catatan.trim().ifEmpty { null }
                        )
                        onSimpanSupplier(supplierResult)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (isEditMode) "Perbarui Data Supplier" else "Simpan Supplier",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(name = "Tambah Supplier Screen", showBackground = true, showSystemUi = true)
@Composable
private fun TambahSupplierScreenPreview() {
    MaterialTheme {
        TambahSupplierScreen(
            supplierToEdit = null,
            onNavigateBack = {},
            onSimpanSupplier = {}
        )
    }
}