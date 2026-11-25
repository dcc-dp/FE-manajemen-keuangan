package com.example.umkmfinance.ui.pelanggan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.example.umkmfinance.data.model.Pelanggan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahPelangganScreen(
    onNavigateBack: () -> Unit,
    onSimpanPelanggan: (Pelanggan, Double?, String?) -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var telepon by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    var catatanKhusus by remember { mutableStateOf("") }

    var isLimitPiutangAktif by remember { mutableStateOf(false) }
    var limitPiutangText by remember { mutableStateOf("") }

    var namaError by remember { mutableStateOf(false) }
    var teleponError by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tambah Pelanggan Baru",
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

            OutlinedTextField(
                value = nama,
                onValueChange = {
                    nama = it
                    if (it.isNotBlank()) namaError = false
                },
                label = { Text("Nama Lengkap Pelanggan *") },
                placeholder = { Text("Contoh: Pak Budi / Bu Siti") },
                isError = namaError,
                supportingText = {
                    if (namaError) Text("Nama pelanggan wajib diisi")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = telepon,
                onValueChange = { input ->
                    telepon = input.filter { it.isDigit() || it == '+' }
                    if (telepon.isNotBlank()) teleponError = false
                },
                label = { Text("Nomor WhatsApp / HP *") },
                placeholder = { Text("081234567890") },
                isError = teleponError,
                supportingText = {
                    if (teleponError) {
                        Text("Nomor kontak diperlukan untuk konfirmasi transaksi & kasbon")
                    } else {
                        Text("Dapat langsung dihubungi via WhatsApp dari aplikasi")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email (Opsional)") },
                placeholder = { Text("pelanggan@email.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = alamat,
                onValueChange = { alamat = it },
                label = { Text("Alamat Pengiriman / Domisili") },
                placeholder = { Text("Jl. Merdeka No. 12, Kelurahan / Meja Toko") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = catatanKhusus,
                onValueChange = { catatanKhusus = it },
                label = { Text("Catatan Khusus (Preferensi / Acuan)") },
                placeholder = { Text("Misal: Langganan kopi meja 4, sering titip uang") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
//                )
//            ) {
//                Column(
//                    modifier = Modifier.padding(14.dp),
//                    verticalArrangement = Arrangement.spacedBy(10.dp)
//                ) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text(
//                                text = "Batas Kasbon (Credit Limit)",
//                                style = MaterialTheme.typography.titleMedium,
//                                fontWeight = FontWeight.SemiBold
//                            )
//                            Text(
//                                text = "Batasi maksimal piutang pelanggan untuk mencegah macet",
//                                style = MaterialTheme.typography.bodySmall,
//                                color = MaterialTheme.colorScheme.outline
//                            )
//                        }
//                        Switch(
//                            checked = isLimitPiutangAktif,
//                            onCheckedChange = { isLimitPiutangAktif = it }
//                        )
//                    }

//                    if (isLimitPiutangAktif) {
//                        OutlinedTextField(
//                            value = limitPiutangText,
//                            onValueChange = { input ->
//                                limitPiutangText = input.filter { it.isDigit() }
//                            },
//                            label = { Text("Maksimal Piutang (Rp)") },
//                            placeholder = { Text("500000") },
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            singleLine = true,
//                            modifier = Modifier.fillMaxWidth(),
//                            shape = RoundedCornerShape(10.dp)
//                        )
//
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier.padding(top = 2.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Info,
//                                contentDescription = null,
//                                modifier = Modifier.size(14.dp),
//                                tint = MaterialTheme.colorScheme.primary
//                            )
//                            Spacer(modifier = Modifier.size(4.dp))
//                            Text(
//                                text = "Kasir akan diingatkan jika kasbon pelanggan melebihi batas ini.",
//                                style = MaterialTheme.typography.labelSmall,
//                                color = MaterialTheme.colorScheme.primary
//                            )
//                        }
//                    }
//                }
//            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    namaError = nama.isBlank()
                    teleponError = telepon.isBlank()

                    if (!namaError && !teleponError) {
                        val limitNominal = if (isLimitPiutangAktif) limitPiutangText.toDoubleOrNull() else null

                        val pelangganBaru = Pelanggan(
                            id = 0L,
                            usaha_id = 1L,
                            nama = nama.trim(),
                            telepon = telepon.trim(),
                            email = email.trim().ifEmpty { null },
                            alamat = alamat.trim().ifEmpty { null },
                            poin = 0
                        )

                        onSimpanPelanggan(pelangganBaru, limitNominal, catatanKhusus.trim().ifEmpty { null })
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Simpan Pelanggan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(name = "Tambah Pelanggan Baru", showBackground = true, showSystemUi = true)
@Composable
private fun TambahPelangganScreenPreview() {
    MaterialTheme {
        TambahPelangganScreen(
            onNavigateBack = {},
            onSimpanPelanggan = { _, _, _ -> }
        )
    }
}