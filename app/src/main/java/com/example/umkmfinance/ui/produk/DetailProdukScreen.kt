package com.example.umkmfinance.ui.produk

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailProdukScreen(
    produkId: Long,
    viewModel: ProdukViewModel,
    onNavigateBack: () -> Unit = {},
    onEditProdukClick: (Long) -> Unit = {},
    onProdukDeleted: () -> Unit = {}
) {
    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(produkId) {
        viewModel.loadProdukById(produkId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.errorMessage ?: "Terjadi kesalahan")
                }
            }

            uiState.data != null -> {
                val produk = uiState.data!!
                DetailProdukView(
                    id = produk.id,
                    nama = produk.nama,
                    kodeProduk = produk.kode_produk,
                    barcode = produk.barcode,
                    kategoriId = produk.kategori_id.toString(),
                    hargaModal = produk.harga_modal,
                    hargaJual = produk.harga_jual,
                    stokAwal = produk.stok,
                    satuan = produk.satuan,
                    status = produk.status,
                    onNavigateBack = onNavigateBack,
                    onEditClick = { onEditProdukClick(produk.id) },
                    onDeleteClick = {
                        // TODO: Panggil fungsi delete di viewModel
                        onProdukDeleted()
                    },
                    onAdjustStock = { _, _ ->
                        // TODO: Panggil fungsi update stok di viewModel
                    }
                )
            }
        }
    }
}

// =============================================================
// CONTENT UI (STATELESS & AMAN UNTUK PREVIEW)
// =============================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailProdukView(
    id: Long,
    nama: String,
    kodeProduk: String,
    barcode: String?,
    kategoriId: String,
    hargaModal: Double,
    hargaJual: Double,
    stokAwal: Int,
    satuan: String,
    status: String,
    onNavigateBack: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onAdjustStock: (jumlahPerubahan: Int, catatan: String) -> Unit = { _, _ -> }
) {
    var stokSekarang by remember { mutableIntStateOf(stokAwal) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAdjustStockDialog by remember { mutableStateOf(false) }
    var isTambahStokMode by remember { mutableStateOf(true) }

    // Hitung margin laba
    val labaSatuan = (hargaJual - hargaModal).coerceAtLeast(0.0)
    val persentaseMargin = if (hargaJual > 0) (labaSatuan / hargaJual) * 100 else 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Produk",
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
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Produk",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus Produk",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column {
                    // WADAH GAMBAR / AVATAR PRODUK
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Foto Produk",
                                modifier = Modifier.size(56.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Foto Produk",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    // KONTEN INFORMASI PRODUK
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = nama,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Kode: $kodeProduk",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            Text(
                                text = status.uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (status.equals("ACTIVE", true) || status.equals("AKTIF", true)) Color(0xFF1B5E20) else Color(0xFF616161),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (status.equals("ACTIVE", true) || status.equals("AKTIF", true)) Color(0xFFE8F5E9) else Color(0xFFEEEEEE)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        DetailItem(label = "Barcode", value = barcode ?: "-")
                        DetailItem(label = "Kategori ID", value = kategoriId)
                        DetailItem(label = "Harga Modal", value = formatRupiah(hargaModal))
                        DetailItem(label = "Harga Jual", value = formatRupiah(hargaJual), isHighlight = true)
                        DetailItem(
                            label = "Margin / Laba",
                            value = "${formatRupiah(labaSatuan)} (${String.format(Locale.US, "%.1f", persentaseMargin)}%)",
                            isHighlight = true
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        // SECTION STOK & QUICK ADJUSTMENT
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Stok Tersedia",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.outline
                                )
                                Text(
                                    text = "$stokSekarang $satuan",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (stokSekarang <= 5) Color(0xFFC62828) else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // TOMBOL AKSI CEPAT STOK (+ / -)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    isTambahStokMode = false
                                    showAdjustStockDialog = true
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC62828))
                            ) {
                                Icon(imageVector = Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.size(4.dp))
                                Text("Kurangi Stok")
                            }

                            Button(
                                onClick = {
                                    isTambahStokMode = true
                                    showAdjustStockDialog = true
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.size(4.dp))
                                Text("Tambah Stok")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // ==========================================
    // DIALOG KONFIRMASI HAPUS PRODUK
    // ==========================================
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(text = "Hapus Produk?")
            },
            text = {
                Text(text = "Apakah Anda yakin ingin menghapus \"$nama\"? Tindakan ini tidak dapat dibatalkan.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Ya, Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // ==========================================
    // DIALOG QUICK STOCK ADJUSTMENT
    // ==========================================
    if (showAdjustStockDialog) {
        var jumlahText by remember { mutableStateOf("") }
        var catatan by remember { mutableStateOf("") }
        var inputError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAdjustStockDialog = false },
            title = {
                Text(text = if (isTambahStokMode) "Tambah Stok Cepat" else "Kurangi Stok (Koreksi/Rusak)")
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = jumlahText,
                        onValueChange = {
                            jumlahText = it.filter { char -> char.isDigit() }
                            if (it.isNotBlank()) inputError = false
                        },
                        label = { Text("Jumlah ($satuan)") },
                        placeholder = { Text("0") },
                        isError = inputError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = catatan,
                        onValueChange = { catatan = it },
                        label = { Text("Keterangan / Alasan") },
                        placeholder = { Text(if (isTambahStokMode) "Penambahan langsung" else "Barang rusak / susut") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val qty = jumlahText.toIntOrNull() ?: 0
                        if (qty > 0) {
                            if (isTambahStokMode) {
                                stokSekarang += qty
                                onAdjustStock(qty, catatan)
                            } else {
                                stokSekarang = (stokSekarang - qty).coerceAtLeast(0)
                                onAdjustStock(-qty, catatan)
                            }
                            showAdjustStockDialog = false
                        } else {
                            inputError = true
                        }
                    }
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdjustStockDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun formatRupiah(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

// =============================================================
// PREVIEW ANDROID STUDIO
// =============================================================

@Preview(
    name = "Detail Produk - Tampilan Baru Lengkap",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DetailProdukScreenPreview() {
    MaterialTheme {
        DetailProdukView(
            id = 1L,
            nama = "Es Teh Manis Jumbo",
            kodeProduk = "PRD-001",
            barcode = "89900000001",
            kategoriId = "2 (Minuman)",
            hargaModal = 2500.0,
            hargaJual = 5000.0,
            stokAwal = 25,
            satuan = "Gelas",
            status = "ACTIVE"
        )
    }
}