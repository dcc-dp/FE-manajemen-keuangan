package com.example.umkmfinance.ui.supplier

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.Supplier
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SupplierScreen(
    viewModel: SupplierViewModel,
    onSupplierClick: (Supplier) -> Unit,
    onTambahSupplierClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSupplier()
    }

    SupplierContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        supplierList = uiState.data,
        onSupplierClick = onSupplierClick,
        onTambahSupplierClick = onTambahSupplierClick
    )
}

@Composable
fun SupplierContent(
    isLoading: Boolean,
    errorMessage: String?,
    supplierList: List<Supplier>,
    onSupplierClick: (Supplier) -> Unit,
    onTambahSupplierClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    val totalHutang = remember(supplierList) {
        supplierList.sumOf { hitungHutangSupplier(it) }
    }
    val countHutang = remember(supplierList) {
        supplierList.count { hitungHutangSupplier(it) > 0.0 }
    }

    val filteredList = remember(supplierList, searchQuery) {
        supplierList.filter { supplier ->
            supplier.nama.contains(searchQuery, ignoreCase = true) ||
                    (supplier.nama_kontak?.contains(searchQuery, ignoreCase = true) == true) ||
                    (supplier.telepon?.contains(searchQuery, ignoreCase = true) == true)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onTambahSupplierClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Supplier")
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = errorMessage)
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Buku Supplier & Pemasok",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // RINGKASAN HUTANG KE SUPPLIER
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f)
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Total Hutang Dagang Belum Lunas",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                        Text(
                                            text = formatRupiah(totalHutang),
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Text(
                                            text = "$countHutang vendor menunggu pelunasan",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Total Supplier",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                        Text(
                                            text = "${supplierList.size} mitra",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    }
                                }
                            }
                        }

                        // SEARCH BAR
                        item {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Cari nama toko, sales, atau telepon...") },
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

                        // LIST ITEM SUPPLIER
                        if (filteredList.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (searchQuery.isNotEmpty()) "Supplier tidak ditemukan." else "Belum ada daftar supplier.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        } else {
                            items(filteredList) { supplier ->
                                val hutang = hitungHutangSupplier(supplier)
                                SupplierCard(
                                    supplier = supplier,
                                    hutang = hutang,
                                    onClick = { onSupplierClick(supplier) },
                                    onCallClick = { bukaDialerTelepon(context, supplier.telepon) },
                                    onWhatsAppClick = { bukaWhatsAppSupplier(context, supplier.telepon, supplier.nama) }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SupplierCard(
    supplier: Supplier,
    hutang: Double,
    onClick: () -> Unit,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = supplier.nama,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (!supplier.nama_kontak.isNullOrBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(13.dp), tint = MaterialTheme.colorScheme.outline)
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(
                                text = supplier.nama_kontak!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }

                if (hutang > 0) {
                    SuggestionChip(
                        onClick = onClick,
                        label = {
                            Text(
                                text = "Hutang: ${formatRupiah(hutang)}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0xFFFFEBEE),
                            labelColor = Color(0xFFC62828)
                        )
                    )
                }
            }

            if (!supplier.telepon.isNullOrBlank()) {
                Text(
                    text = "Telepon / WA: ${supplier.telepon}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            if (!supplier.alamat.isNullOrBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(13.dp), tint = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = supplier.alamat!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!supplier.telepon.isNullOrBlank()) {
                    FilledTonalIconButton(
                        onClick = onCallClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Telepon", modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.size(8.dp))

                    FilledTonalIconButton(
                        onClick = onWhatsAppClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Message, contentDescription = "Order WhatsApp", tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

private fun hitungHutangSupplier(supplier: Supplier): Double {
    return when (supplier.id) {
        1L -> 1250000.0
        2L -> 650000.0
        else -> 0.0
    }
}

private fun bukaDialerTelepon(context: Context, nomor: String?) {
    if (nomor.isNullOrBlank()) return
    try {
        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$nomor")))
    } catch (_: Exception) {
        Toast.makeText(context, "Tidak dapat membuka telepon", Toast.LENGTH_SHORT).show()
    }
}

private fun bukaWhatsAppSupplier(context: Context, nomor: String?, namaToko: String) {
    if (nomor.isNullOrBlank()) return
    val clean = if (nomor.startsWith("0")) "62" + nomor.substring(1) else nomor.replace("+", "").replace("-", "").replace(" ", "")
    val msg = "Halo $namaToko, kami ingin menanyakan stok dan melakukan pesanan pembelian barang."
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$clean&text=${Uri.encode(msg)}")))
    } catch (_: Exception) {
        Toast.makeText(context, "WhatsApp tidak terpasang", Toast.LENGTH_SHORT).show()
    }
}

private fun formatRupiah(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

@Preview(name = "Supplier Screen", showBackground = true, showSystemUi = true)
@Composable
private fun SupplierScreenPreview() {
    val dummy = listOf(
        Supplier(1L, 1L, "Agen Kopi Nusantara", "081234567891", null, "Makassar", "Pak Rian"),
        Supplier(2L, 1L, "Distributor Sirup Berkah", "085299887766", null, "Parangloe", "Ibu Maya")
    )
    MaterialTheme {
        SupplierContent(
            isLoading = false,
            errorMessage = null,
            supplierList = dummy,
            onSupplierClick = {}
        )
    }
}