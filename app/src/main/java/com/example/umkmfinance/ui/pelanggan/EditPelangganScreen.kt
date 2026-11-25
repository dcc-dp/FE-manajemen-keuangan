package com.example.umkmfinance.ui.pelanggan

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
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.umkmfinance.data.model.Pelanggan

@Composable
fun EditPelangganScreen(
    pelangganId: Long,
    viewModel: PelangganViewModel,
    onNavigateBack: () -> Unit,
    onPerbaruiPelanggan: (Pelanggan, Double?, String?) -> Unit
) {
    val detailState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(pelangganId) {
        viewModel.loadPelangganById(pelangganId)
    }

    when {
        detailState.isLoading && detailState.data == null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        detailState.errorMessage != null && detailState.data == null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = detailState.errorMessage ?: "Gagal memuat data pelanggan")
            }
        }

        detailState.data != null -> {
            EditPelangganForm(
                pelangganAwal = detailState.data!!,
                onNavigateBack = onNavigateBack,
                onPerbaruiPelanggan = onPerbaruiPelanggan
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPelangganForm(
    pelangganAwal: Pelanggan,
    onNavigateBack: () -> Unit,
    onPerbaruiPelanggan: (Pelanggan, Double?, String?) -> Unit
) {
    var nama by remember(pelangganAwal.id) { mutableStateOf(pelangganAwal.nama) }
    var telepon by remember(pelangganAwal.id) { mutableStateOf<String?>(pelangganAwal.telepon) }
    var email by remember(pelangganAwal.id) { mutableStateOf(pelangganAwal.email ?: "") }
    var alamat by remember(pelangganAwal.id) { mutableStateOf(pelangganAwal.alamat ?: "") }
    var catatanKhusus by remember(pelangganAwal.id) { mutableStateOf("") }

    var isLimitPiutangAktif by remember(pelangganAwal.id) { mutableStateOf(false) }
    var limitPiutangText by remember(pelangganAwal.id) { mutableStateOf("") }

    var namaError by remember { mutableStateOf(false) }
    var teleponError by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Data Pelanggan",
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
                isError = namaError,
                supportingText = {
                    if (namaError) Text("Nama pelanggan wajib diisi")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = telepon.orEmpty(),
                onValueChange = { input ->
                    val filtered = input.filter { it.isDigit() || it == '+' }
                    telepon = filtered
                    if (filtered.isNotBlank()) teleponError = false
                },
                label = { Text("Nomor WhatsApp / HP *") },
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = alamat,
                onValueChange = { alamat = it },
                label = { Text("Alamat Pengiriman / Domisili") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = catatanKhusus,
                onValueChange = { catatanKhusus = it },
                label = { Text("Catatan Khusus (Preferensi / Acuan)") },
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
//
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
                    val cleanTelepon = telepon?.trim().orEmpty()
                    namaError = nama.isBlank()
                    teleponError = cleanTelepon.isEmpty()

                    if (!namaError && !teleponError) {
                        val limitNominal = if (isLimitPiutangAktif) limitPiutangText.toDoubleOrNull() else null

                        val pelangganDiubah = pelangganAwal.copy(
                            nama = nama.trim(),
                            telepon = cleanTelepon.ifEmpty { null },
                            email = email.trim().ifEmpty { null },
                            alamat = alamat.trim().ifEmpty { null }
                        )

                        onPerbaruiPelanggan(pelangganDiubah, limitNominal, catatanKhusus.trim().ifEmpty { null })
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Perbarui Pelanggan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(name = "Edit Data Pelanggan", showBackground = true, showSystemUi = true)
@Composable
private fun EditPelangganScreenPreview() {
    val dummy = Pelanggan(
        id = 1L,
        usaha_id = 1L,
        nama = "Budi Santoso",
        telepon = "081234567890",
        email = "budi@gmail.com",
        alamat = "Jl. Merdeka No. 12, Makassar",
        poin = 150
    )

    MaterialTheme {
        EditPelangganForm(
            pelangganAwal = dummy,
            onNavigateBack = {},
            onPerbaruiPelanggan = { _, _, _ -> }
        )
    }
}