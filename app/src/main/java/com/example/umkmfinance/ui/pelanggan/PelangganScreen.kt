package com.example.umkmfinance.ui.pelanggan

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FilledTonalIconButton
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
import com.example.umkmfinance.data.model.Pelanggan
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PelangganScreen(
    viewModel: PelangganViewModel,
    onPelangganClick: (Pelanggan) -> Unit,
    onTambahPelangganClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPelanggan()
    }

    PelangganContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        pelangganList = uiState.data,
        onPelangganClick = onPelangganClick,
        onTambahPelangganClick = onTambahPelangganClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PelangganContent(
    isLoading: Boolean,
    errorMessage: String?,
    pelangganList: List<Pelanggan>,
    onPelangganClick: (Pelanggan) -> Unit,
    onTambahPelangganClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    // Hitung estimasi piutang (kasbon dummy/simulasi jika properti piutang belum ada di model entity)
    val totalPiutang = remember(pelangganList) {
        pelangganList.sumOf { hitungPiutangPelanggan(it) }
    }
    val jumlahAdaPiutang = remember(pelangganList) {
        pelangganList.count { hitungPiutangPelanggan(it) > 0.0 }
    }

    // Filter dan Search Logic
    val filteredList = remember(pelangganList, searchQuery, selectedFilter) {
        pelangganList.filter { pelanggan ->
            val matchesSearch = pelanggan.nama.contains(searchQuery, ignoreCase = true) ||
                    (pelanggan.telepon?.contains(searchQuery, ignoreCase = true) == true) ||
                    (pelanggan.email?.contains(searchQuery, ignoreCase = true) == true)

            val matchesFilter = when (selectedFilter) {
                "Ada Piutang" -> hitungPiutangPelanggan(pelanggan) > 0.0
                "Loyal / VIP" -> (pelanggan.poin ?: 0) >= 100
                else -> true
            }

            matchesSearch && matchesFilter
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onTambahPelangganClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Pelanggan"
                )
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
                                text = "Buku Pelanggan",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // RINGKASAN PIUTANG & TOTAL PELANGGAN
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
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
                                            text = "Total Piutang Belum Terbayar",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                        Text(
                                            text = formatRupiah(totalPiutang),
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFC62828)
                                        )
                                        Text(
                                            text = "$jumlahAdaPiutang orang memiliki kasbon aktif",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Total Pelanggan",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                        Text(
                                            text = "${pelangganList.size} orang",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer
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
                                placeholder = { Text("Cari nama, WhatsApp, atau email...") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Hapus teks"
                                            )
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // FILTER CHIP TAB
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val filters = listOf("Semua", "Ada Piutang", "Loyal / VIP")
                                filters.forEach { filter ->
                                    FilterChip(
                                        selected = selectedFilter == filter,
                                        onClick = { selectedFilter = filter },
                                        label = { Text(filter) }
                                    )
                                }
                            }
                        }

                        // JUMLAH DATA TERFILTER
                        item {
                            Text(
                                text = "Menampilkan ${filteredList.size} dari ${pelangganList.size} kontak pelanggan",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        // LIST ITEM
                        if (filteredList.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (searchQuery.isNotEmpty()) "Kontak tidak ditemukan." else "Belum ada pelanggan pada kategori ini.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        } else {
                            items(filteredList) { pelanggan ->
                                val saldoPiutang = hitungPiutangPelanggan(pelanggan)
                                PelangganCard(
                                    pelanggan = pelanggan,
                                    piutang = saldoPiutang,
                                    onClick = { onPelangganClick(pelanggan) },
                                    onCallClick = {
                                        bukaDialerTelepon(context, pelanggan.telepon)
                                    },
                                    onWhatsAppClick = {
                                        bukaWhatsApp(context, pelanggan.telepon, pelanggan.nama, saldoPiutang)
                                    }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp)) // Padding agar tidak tertutup FAB
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PelangganCard(
    pelanggan: Pelanggan,
    piutang: Double,
    onClick: () -> Unit,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit
) {
    val poin = pelanggan.poin ?: 0
    val isVIP = poin >= 100
    val hasPiutang = piutang > 0

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
            // Baris Atas: Nama & Badge (Loyal / Piutang)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pelanggan.nama,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (isVIP) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFF57F17),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.size(2.dp))
                            Text(
                                text = "Pelanggan Loyal ($poin poin)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFF57F17)
                            )
                        }
                    }
                }

                // BADGE PIUTANG / KASBON
                if (hasPiutang) {
                    SuggestionChip(
                        onClick = onClick,
                        label = {
                            Text(
                                text = "Kasbon: ${formatRupiah(piutang)}",
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

            // Informasi Kontak & Alamat
            if (!pelanggan.telepon.isNullOrBlank()) {
                Text(
                    text = "No. HP: ${pelanggan.telepon}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            if (!pelanggan.email.isNullOrBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = pelanggan.email!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            if (!pelanggan.alamat.isNullOrBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = pelanggan.alamat!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1
                    )
                }
            }

            // QUICK ACTIONS (TOMBOL TELEPON & WHATSAPP)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!pelanggan.telepon.isNullOrBlank()) {
                    // Tombol Telepon Biasa
                    FilledTonalIconButton(
                        onClick = onCallClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Panggil",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.size(8.dp))

                    // Tombol Kirim WhatsApp
                    FilledTonalIconButton(
                        onClick = onWhatsAppClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Message,
                            contentDescription = "WhatsApp",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================
// HELPER LOGIKA & INTENT KOMUNIKASI
// =============================================================

// Fungsi kalkulasi piutang (bisa disesuaikan dengan field dari API jika tersedia)
private fun hitungPiutangPelanggan(pelanggan: Pelanggan): Double {
    return when (pelanggan.id) {
        1L -> 150000.0
        3L -> 420000.0
        else -> 0.0
    }
}

private fun bukaDialerTelepon(context: Context, nomor: String?) {
    if (nomor.isNullOrBlank()) {
        Toast.makeText(context, "Nomor telepon tidak tersedia", Toast.LENGTH_SHORT).show()
        return
    }
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$nomor"))
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "Tidak dapat membuka dialer", Toast.LENGTH_SHORT).show()
    }
}

private fun bukaWhatsApp(context: Context, nomor: String?, nama: String, piutang: Double) {
    if (nomor.isNullOrBlank()) {
        Toast.makeText(context, "Nomor WhatsApp tidak tersedia", Toast.LENGTH_SHORT).show()
        return
    }

    // Normalisasi awalan nomor ke format internasional (misal 08 -> 628)
    val sanitizedNumber = if (nomor.startsWith("0")) {
        "62" + nomor.substring(1)
    } else {
        nomor.replace("+", "").replace("-", "").replace(" ", "")
    }

    val pesan = if (piutang > 0) {
        "Halo Kak $nama, kami dari UMKM Finance ingin konfirmasi mengenai catatan transaksi kasbon sebesar ${formatRupiah(piutang)}. Terima kasih."
    } else {
        "Halo Kak $nama, terima kasih telah berbelanja di tempat kami!"
    }

    try {
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$sanitizedNumber&text=${Uri.encode(pesan)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "Aplikasi WhatsApp tidak terpasang di perangkat", Toast.LENGTH_SHORT).show()
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
    name = "Pelanggan Screen - Berisi Data & Piutang",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun PelangganScreenPreview() {
    val dummyPelanggan = listOf(
        Pelanggan(
            id = 1L,
            usaha_id = 1L,
            nama = "Budi Santoso",
            telepon = "081234567890",
            email = "budi@gmail.com",
            alamat = "Jl. Merdeka No. 12, Makassar",
            poin = 120
        ),
        Pelanggan(
            id = 2L,
            usaha_id = 1L,
            nama = "Siti Aminah",
            telepon = "085298765432",
            email = null,
            alamat = "Kompleks Perintis Blok C",
            poin = 45
        ),
        Pelanggan(
            id = 3L,
            usaha_id = 1L,
            nama = "Toko Berkah Bersama",
            telepon = "082111223344",
            email = "berkah@store.id",
            alamat = "Pasar Terong Makassar",
            poin = 350
        )
    )

    MaterialTheme {
        PelangganContent(
            isLoading = false,
            errorMessage = null,
            pelangganList = dummyPelanggan,
            onPelangganClick = {},
            onTambahPelangganClick = {}
        )
    }
}