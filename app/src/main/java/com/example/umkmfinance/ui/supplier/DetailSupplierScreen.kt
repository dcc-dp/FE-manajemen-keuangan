package com.example.umkmfinance.ui.supplier

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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Store
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
import com.example.umkmfinance.data.model.Supplier
import java.text.NumberFormat
import java.util.Locale

data class FakturPembelianItem(
    val nomorFaktur: String,
    val tanggal: String,
    val total: Double,
    val status: String
)

@Composable
fun DetailSupplierScreen(
    supplierId: Long,
    viewModel: SupplierViewModel,
    onNavigateBack: () -> Unit = {},
    onEditSupplierClick: (Long) -> Unit = {},
    onSupplierDeleted: () -> Unit = {}
) {
    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(supplierId) {
        viewModel.loadSupplierById(supplierId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.errorMessage ?: "Terjadi kesalahan")
                }
            }

            uiState.data != null -> {
                DetailSupplierView(
                    supplier = uiState.data!!,
                    onNavigateBack = onNavigateBack,
                    onEditClick = { onEditSupplierClick(uiState.data!!.id) },
                    onDeleteClick = {
                        viewModel.hapusSupplier(uiState.data!!.id) {
                            onSupplierDeleted()
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailSupplierView(
    supplier: Supplier,
    onNavigateBack: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showBayarHutangDialog by remember { mutableStateOf(false) }

    var sisaHutang by remember { mutableDoubleStateOf(if (supplier.id == 1L) 1250000.0 else 0.0) }
    val totalKulakan = 5800000.0

    val riwayatFaktur = listOf(
        FakturPembelianItem("PO-20261102-01", "02 Nov 2026", 1250000.0, if (sisaHutang > 0) "TEMPO" else "LUNAS"),
        FakturPembelianItem("PO-20261015-08", "15 Okt 2026", 2100000.0, "LUNAS"),
        FakturPembelianItem("PO-20260920-12", "20 Sep 2026", 2450000.0, "LUNAS")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Supplier", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.error)
                    }
                }
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
            // PROFIL SUPPLIER
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(27.dp))
                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Store, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.size(30.dp))
                            }
                            Column {
                                Text(supplier.nama, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text("PIC: ${supplier.nama_kontak ?: "-"}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        DetailSupplierItem(Icons.Default.Call, supplier.telepon ?: "Tidak ada telepon")
                        DetailSupplierItem(Icons.Default.Email, supplier.email ?: "Tidak ada email")
                        DetailSupplierItem(Icons.Default.LocationOn, supplier.alamat ?: "Tidak ada alamat fisik")

                        if (!supplier.catatan.isNullOrBlank()) {
                            DetailSupplierItem(Icons.Default.Info, supplier.catatan!!)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    if (!supplier.telepon.isNullOrBlank()) {
                                        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${supplier.telepon}")))
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.size(4.dp))
                                Text("Panggil")
                            }

                            Button(
                                onClick = {
                                    if (!supplier.telepon.isNullOrBlank()) {
                                        val clean = if (supplier.telepon.startsWith("0")) "62" + supplier.telepon.substring(1) else supplier.telepon
                                        val msg = "Halo ${supplier.nama}, kami ingin order barang."
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$clean&text=${Uri.encode(msg)}")))
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                            ) {
                                Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.size(4.dp))
                                Text("Order WA")
                            }
                        }
                    }
                }
            }

            // KARTU FINANCIAL SUPPLIER & HUTANG
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Total Kulakan (Purchases)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                Text(formatRupiah(totalKulakan), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Sisa Tagihan / Hutang", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                Text(
                                    formatRupiah(sisaHutang),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (sisaHutang > 0) Color(0xFFC62828) else Color(0xFF2E7D32)
                                )
                            }
                        }

                        if (sisaHutang > 0) {
                            Button(
                                onClick = { showBayarHutangDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.size(6.dp))
                                Text("Catat Pembayaran Hutang")
                            }
                        }
                    }
                }
            }

            // TAB RIWAYAT PENGADAAN BARANG
            item {
                TabRow(selectedTabIndex = selectedTab, containerColor = Color.Transparent) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Faktur Pembelian (${riwayatFaktur.size})") }
                    )
                }
            }

            items(riwayatFaktur) { faktur ->
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
                                Text(faktur.nomorFaktur, fontWeight = FontWeight.Bold)
                                Text(faktur.tanggal, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(formatRupiah(faktur.total), fontWeight = FontWeight.Bold)
                            Text(
                                faktur.status,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (faktur.status == "LUNAS") Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }

    // DIALOG HAPUS
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Hapus Supplier?") },
            text = { Text("Apakah Anda yakin ingin menghapus mitra \"${supplier.nama}\"?") },
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

    // DIALOG PELUNASAN HUTANG
    if (showBayarHutangDialog) {
        var nominalBayar by remember { mutableStateOf("") }
        var isError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showBayarHutangDialog = false },
            title = { Text("Bayar Hutang Supplier") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Sisa hutang: ${formatRupiah(sisaHutang)}")
                    OutlinedTextField(
                        value = nominalBayar,
                        onValueChange = {
                            nominalBayar = it.filter { c -> c.isDigit() }
                            if (it.isNotBlank()) isError = false
                        },
                        label = { Text("Nominal Bayar (Rp)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = isError,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val nom = nominalBayar.toDoubleOrNull() ?: 0.0
                        if (nom > 0) {
                            sisaHutang = (sisaHutang - nom).coerceAtLeast(0.0)
                            showBayarHutangDialog = false
                        } else {
                            isError = true
                        }
                    }
                ) {
                    Text("Bayar Sekarang")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBayarHutangDialog = false }) { Text("Batal") }
            }
        )
    }
}

@Composable
private fun DetailSupplierItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.size(8.dp))
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

// FUNGSI LOKAL AGAR BEBAS DARI ERROR IMPORT PRIVATE
private fun formatRupiah(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

@Preview(name = "Detail Supplier Screen", showBackground = true, showSystemUi = true)
@Composable
private fun DetailSupplierScreenPreview() {
    val dummy = Supplier(
        id = 1L,
        usaha_id = 1L,
        nama = "Agen Kopi Nusantara Makassar",
        nama_kontak = "Pak Rian (Sales)",
        telepon = "081234567891",
        email = "sales@kopinusantara.id",
        alamat = "Jl. Perintis Kemerdekaan KM 9, Makassar",
        catatan = "BCA 8273648122 a/n Rian. Minimal order 10 kg."
    )
    MaterialTheme {
        DetailSupplierView(
            supplier = dummy,
            onNavigateBack = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}