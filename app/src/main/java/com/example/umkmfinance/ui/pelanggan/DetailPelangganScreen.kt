package com.example.umkmfinance.ui.pelanggan

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.Pelanggan
import java.text.NumberFormat
import java.util.Locale

// Data model bantuan untuk riwayat
data class TransaksiPelangganItem(
    val invoice: String,
    val tanggal: String,
    val total: Double,
    val status: String
)

data class RiwayatPiutangItem(
    val id: Long,
    val tanggal: String,
    val nominal: Double,
    val jenis: String, // "TAMBAH_PIUTANG" atau "PELUNASAN"
    val catatan: String
)

@Composable
fun DetailPelangganScreen(
    pelangganId: Long,
    viewModel: PelangganViewModel,
    onNavigateBack: () -> Unit = {},
    onEditPelangganClick: (Long) -> Unit = {},
    onPelangganDeleted: () -> Unit = {}
) {
    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(pelangganId) {
        viewModel.loadPelangganById(pelangganId)
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
                DetailPelangganView(
                    pelanggan = uiState.data!!,
                    onNavigateBack = onNavigateBack,
                    onEditClick = { onEditPelangganClick(uiState.data!!.id) },
                    onDeleteClick = { onPelangganDeleted() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailPelangganView(
    pelanggan: Pelanggan,
    onNavigateBack: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPiutangDialog by remember { mutableStateOf(false) }
    var isTambahKasbonMode by remember { mutableStateOf(true) }

    // Ringkasan Keuangan (Simulasi Dummy State)
    var sisaPiutang by remember { mutableDoubleStateOf(if (pelanggan.id % 2L != 0L) 150000.0 else 0.0) }
    val totalBelanja = 1450000.0
    val totalKunjungan = 8

    // Data Dummy Riwayat Transaksi & Piutang
    val riwayatBelanja = listOf(
        TransaksiPelangganItem("INV-20261108-01", "08 Nov 2026", 150000.0, if (sisaPiutang > 0) "PIUTANG" else "LUNAS"),
        TransaksiPelangganItem("INV-20261101-04", "01 Nov 2026", 500000.0, "LUNAS"),
        TransaksiPelangganItem("INV-20261025-11", "25 Okt 2026", 800000.0, "LUNAS")
    )

    val riwayatPiutangList = remember(sisaPiutang) {
        listOf(
            RiwayatPiutangItem(1, "08 Nov 2026", 150000.0, "TAMBAH_PIUTANG", "Kasbon belanja kopi & snack"),
            RiwayatPiutangItem(2, "02 Nov 2026", 100000.0, "PELUNASAN", "Cicilan tunai via kasir")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profil Pelanggan",
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
                            contentDescription = "Edit Pelanggan",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus Kontak",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // PROFIL & INFO DASAR
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(27.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = pelanggan.nama,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "ID: #${pelanggan.id} • ${pelanggan.poin ?: 0} Poin Loyalitas",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        DetailItemIcon(icon = Icons.Default.Call, value = pelanggan.telepon ?: "Tidak ada telepon")
                        DetailItemIcon(icon = Icons.Default.Email, value = pelanggan.email ?: "Tidak ada email")
                        DetailItemIcon(icon = Icons.Default.LocationOn, value = pelanggan.alamat ?: "Tidak ada alamat")

                        // TOMBOL KOMUNIKASI LANGSUNG
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { bukaDialer(context, pelanggan.telepon) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.size(4.dp))
                                Text("Telepon")
                            }

                            Button(
                                onClick = { bukaWhatsApp(context, pelanggan.telepon, pelanggan.nama, sisaPiutang) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                            ) {
                                Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.size(4.dp))
                                Text("WhatsApp")
                            }
                        }
                    }
                }
            }

            // KARTU RINGKASAN KEUANGAN
            item {
                Text(
                    text = "Ringkasan Finansial",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total Belanja (LTV)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                Text(formatRupiah(totalBelanja), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Frekuensi Belanja", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                Text("$totalKunjungan kali transaksi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        }

                        HorizontalDivider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Sisa Piutang Berjalan (Kasbon)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                Text(
                                    text = formatRupiah(sisaPiutang),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (sisaPiutang > 0) Color(0xFFC62828) else Color(0xFF2E7D32)
                                )
                            }
                        }

                        // TOMBOL AKSI CEPAT PIUTANG
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    isTambahKasbonMode = true
                                    showPiutangDialog = true
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.size(4.dp))
                                Text("Tambah Kasbon")
                            }

                            Button(
                                onClick = {
                                    isTambahKasbonMode = false
                                    showPiutangDialog = true
                                },
                                enabled = sisaPiutang > 0,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.size(4.dp))
                                Text("Catat Lunas")
                            }
                        }
                    }
                }
            }

            // TAB RIWAYAT TRANSAKSI & PIUTANG
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Riwayat Belanja (${riwayatBelanja.size})") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Catatan Piutang") }
                    )
                }
            }

            // ISI TAB LIST
            if (selectedTab == 0) {
                items(riwayatBelanja) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.size(8.dp))
                                Column {
                                    Text(item.invoice, fontWeight = FontWeight.Bold)
                                    Text(item.tanggal, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(formatRupiah(item.total), fontWeight = FontWeight.Bold)
                                Text(
                                    item.status,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (item.status == "LUNAS") Color(0xFF2E7D32) else Color(0xFFC62828)
                                )
                            }
                        }
                    }
                }
            } else {
                items(riwayatPiutangList) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(item.catatan, fontWeight = FontWeight.SemiBold)
                                Text(item.tanggal, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                            }
                            Text(
                                text = (if (item.jenis == "TAMBAH_PIUTANG") "+ " else "- ") + formatRupiah(item.nominal),
                                fontWeight = FontWeight.Bold,
                                color = if (item.jenis == "TAMBAH_PIUTANG") Color(0xFFC62828) else Color(0xFF2E7D32)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // ==========================================
    // DIALOG KONFIRMASI HAPUS
    // ==========================================
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Hapus Pelanggan?") },
            text = { Text("Apakah Anda yakin ingin menghapus data kontak ${pelanggan.nama}? Data transaksi yang terkait mungkin akan kehilangan referensi pelanggan.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") }
            }
        )
    }

    // ==========================================
    // DIALOG CATAT PIUTANG / PELUNASAN MANUAL
    // ==========================================
    if (showPiutangDialog) {
        var nominalText by remember { mutableStateOf("") }
        var keteranganText by remember { mutableStateOf("") }
        var inputError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showPiutangDialog = false },
            title = {
                Text(text = if (isTambahKasbonMode) "Tambah Kasbon Baru" else "Catat Pelunasan Piutang")
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nominalText,
                        onValueChange = {
                            nominalText = it.filter { char -> char.isDigit() }
                            if (it.isNotBlank()) inputError = false
                        },
                        label = { Text("Nominal (Rp)") },
                        placeholder = { Text("0") },
                        isError = inputError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = keteranganText,
                        onValueChange = { keteranganText = it },
                        label = { Text("Keterangan") },
                        placeholder = { Text(if (isTambahKasbonMode) "Belanja tempo" else "Cicilan via transfer/kasir") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val nom = nominalText.toDoubleOrNull() ?: 0.0
                        if (nom > 0) {
                            if (isTambahKasbonMode) {
                                sisaPiutang += nom
                            } else {
                                sisaPiutang = (sisaPiutang - nom).coerceAtLeast(0.0)
                            }
                            showPiutangDialog = false
                        } else {
                            inputError = true
                        }
                    }
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPiutangDialog = false }) { Text("Batal") }
            }
        )
    }
}

// =============================================================
// SUB-KOMPONEN & INTENT KOMUNIKASI
// =============================================================

@Composable
private fun DetailItemIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun bukaDialer(context: Context, nomor: String?) {
    if (nomor.isNullOrBlank()) {
        Toast.makeText(context, "Nomor telepon tidak tersedia", Toast.LENGTH_SHORT).show()
        return
    }
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$nomor"))
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "Gagal membuka aplikasi telepon", Toast.LENGTH_SHORT).show()
    }
}

private fun bukaWhatsApp(context: Context, nomor: String?, nama: String, piutang: Double) {
    if (nomor.isNullOrBlank()) {
        Toast.makeText(context, "Nomor WhatsApp tidak tersedia", Toast.LENGTH_SHORT).show()
        return
    }
    val clean = if (nomor.startsWith("0")) "62" + nomor.substring(1) else nomor.replace("+", "").replace("-", "").replace(" ", "")
    val msg = if (piutang > 0) {
        "Halo Kak $nama, kami dari UMKM Finance ingin menginfokan catatan tagihan kasbon sebesar ${formatRupiah(piutang)}. Terima kasih."
    } else {
        "Halo Kak $nama, terima kasih telah menjadi pelanggan setia kami!"
    }
    try {
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$clean&text=${Uri.encode(msg)}")
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    } catch (_: Exception) {
        Toast.makeText(context, "Aplikasi WhatsApp tidak terpasang", Toast.LENGTH_SHORT).show()
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
    name = "Detail Pelanggan Lengkap",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DetailPelangganScreenPreview() {
    val dummyPelanggan = Pelanggan(
        id = 1L,
        usaha_id = 1L,
        nama = "Budi Santoso",
        telepon = "081234567890",
        email = "budi.santoso@email.com",
        alamat = "Jl. Merdeka No. 12, Makassar",
        poin = 150
    )

    MaterialTheme {
        DetailPelangganView(
            pelanggan = dummyPelanggan,
            onNavigateBack = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}