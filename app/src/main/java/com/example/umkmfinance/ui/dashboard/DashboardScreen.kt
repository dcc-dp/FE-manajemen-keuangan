package com.example.umkmfinance.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.theme.GreenPrimary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    role: String,
    viewModel: DashboardViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
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
                val data = uiState.data!!
                DashboardView(
                    role = role,
                    totalPendapatan = data.ringkasan.total_pendapatan,
                    labaKotor = data.ringkasan.laba_kotor,
                    labaBersih = data.ringkasan.laba_bersih,
                    totalPengeluaran = data.ringkasan.total_pengeluaran,
                    totalSaldo = data.ringkasan.total_saldo,
                    totalPiutang = data.ringkasan.total_piutang,
                    totalHutang = data.ringkasan.total_hutang,
                    stokRendah = data.stok_rendah?.map {
                        DashboardStokItem(
                            nama = it.nama,
                            stok = it.stok.toString(),
                            satuan = it.satuan
                        )
                    }.orEmpty()
                )
            }
        }
    }
}

// Model lokal khusus UI agar Preview tidak bergantung pada nama class API/Network
data class DashboardStokItem(
    val nama: String,
    val stok: String,
    val satuan: String
)

@Composable
private fun DashboardView(
    role: String,
    totalPendapatan: Double,
    labaKotor: Double,
    labaBersih: Double,
    totalPengeluaran: Double,
    totalSaldo: Double,
    totalPiutang: Double,
    totalHutang: Double,
    stokRendah: List<DashboardStokItem>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Ringkasan keuangan • $role",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            DashboardCard(
                title = "Pendapatan",
                value = formatRupiah(totalPendapatan)
            )
        }

        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                DashboardCard(
                    title = "Laba Kotor",
                    value = formatRupiah(labaKotor),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                DashboardCard(
                    title = "Laba Bersih",
                    value = formatRupiah(labaBersih),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                DashboardCard(
                    title = "Pengeluaran",
                    value = formatRupiah(totalPengeluaran),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                DashboardCard(
                    title = "Saldo",
                    value = formatRupiah(totalSaldo),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                DashboardCard(
                    title = "Piutang",
                    value = formatRupiah(totalPiutang),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                DashboardCard(
                    title = "Hutang",
                    value = formatRupiah(totalHutang),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Stok Rendah",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (stokRendah.isEmpty()) {
            item {
                Text(text = "Tidak ada produk dengan stok rendah.")
            }
        } else {
            items(stokRendah.size) { index ->
                val produk = stokRendah[index]

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = produk.nama,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Stok: ${produk.stok} ${produk.satuan}"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary
            )
        }
    }
}

private fun formatRupiah(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(
        Locale("id", "ID")
    )
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

// =============================================================
// PREVIEW ANDROID STUDIO
// =============================================================

@Preview(
    name = "Dashboard Screen Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DashboardScreenPreview() {
    MaterialTheme {
        DashboardView(
            role = "Pemilik",
            totalPendapatan = 15500000.0,
            labaKotor = 7200000.0,
            labaBersih = 4850000.0,
            totalPengeluaran = 8300000.0,
            totalSaldo = 12400000.0,
            totalPiutang = 1500000.0,
            totalHutang = 2000000.0,
            stokRendah = listOf(
                DashboardStokItem(nama = "Kopi Robusta 250g", stok = "3", satuan = "Pcs"),
                DashboardStokItem(nama = "Susu UHT 1L", stok = "2", satuan = "Karton")
            )
        )
    }
}