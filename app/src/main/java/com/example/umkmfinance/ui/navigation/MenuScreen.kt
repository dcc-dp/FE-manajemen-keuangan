package com.example.umkmfinance.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Model data menu item
data class MenuItemData(
    val key: String,
    val title: String,
    val icon: ImageVector,
    val iconColor: Color,
    val iconBackground: Color
)

@Composable
fun MenuScreen(
    role: String,
    onMenuClick: (String) -> Unit
) {
    // Daftar semua menu dengan ikon dan skema warna yang selaras
    val allMenuItems = mapOf(
        "Penjualan" to MenuItemData("Penjualan", "Penjualan", Icons.Default.PointOfSale, Color(0xFF2E7D32), Color(0xFFE8F5E9)),
        "Pembelian" to MenuItemData("Pembelian", "Pembelian", Icons.Default.ShoppingBag, Color(0xFF1565C0), Color(0xFFE3F2FD)),
        "Produk" to MenuItemData("Produk", "Produk", Icons.Default.Inventory2, Color(0xFFE65100), Color(0xFFFFF3E0)),
        "Pelanggan" to MenuItemData("Pelanggan", "Pelanggan", Icons.Default.People, Color(0xFF6A1B9A), Color(0xFFF3E5F5)),
        "Supplier" to MenuItemData("Supplier", "Supplier", Icons.Default.Store, Color(0xFF00838F), Color(0xFFE0F7FA)),
        "Pengeluaran" to MenuItemData("Pengeluaran", "Pengeluaran", Icons.Default.MoneyOff, Color(0xFFC62828), Color(0xFFFFEBEE)),
        "Piutang" to MenuItemData("Piutang", "Piutang", Icons.Default.PendingActions, Color(0xFFF57F17), Color(0xFFFFFDE7)),
        "Hutang" to MenuItemData("Hutang", "Hutang", Icons.Default.ReceiptLong, Color(0xFFAD1457), Color(0xFFFCE4EC)),
        "Rekening" to MenuItemData("Rekening", "Rekening", Icons.Default.AccountBalance, Color(0xFF283593), Color(0xFFE8EAF6)),
        "Target Keuangan" to MenuItemData("Target Keuangan", "Target Keuangan", Icons.Default.TrackChanges, Color(0xFF00695C), Color(0xFFE0F2F1)),
        "Laporan" to MenuItemData("Laporan", "Laporan", Icons.Default.Assessment, Color(0xFF4527A0), Color(0xFFEDE7F6)),
        "Pengguna" to MenuItemData("Pengguna", "Pengguna", Icons.Default.Groups, Color(0xFF37474F), Color(0xFFECEFF1)),
        "Role & Permission" to MenuItemData("Role & Permission", "Role & Akses", Icons.Default.Security, Color(0xFF4E342E), Color(0xFFEFEBE9)),
        "Notifikasi" to MenuItemData("Notifikasi", "Notifikasi", Icons.Default.Notifications, Color(0xFFD84315), Color(0xFFFBE9E7))
    )

    val menuKeys = when (role.uppercase()) {
        "OWNER" -> listOf(
            "Penjualan", "Pembelian", "Produk", "Pelanggan", "Supplier",
            "Pengeluaran", "Piutang", "Hutang", "Rekening", "Target Keuangan",
            "Laporan", "Pengguna", "Role & Permission", "Notifikasi"
        )
        "ADMIN" -> listOf(
            "Penjualan", "Pembelian", "Produk", "Pelanggan", "Supplier",
            "Pengeluaran", "Piutang", "Hutang", "Rekening", "Laporan", "Notifikasi"
        )
        "KASIR" -> listOf(
            "Penjualan", "Pelanggan", "Piutang", "Notifikasi"
        )
        else -> emptyList()
    }

    val displayList = menuKeys.mapNotNull { allMenuItems[it] }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8F9FA)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            item(span = { GridItemSpan(2) }) {
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(
                        text = "Menu Utama",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10201A)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Akses modul aplikasi sebagai $role",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF66736D)
                    )
                }
            }

            // Grid Cards
            items(displayList) { item ->
                MenuGridCard(
                    item = item,
                    onClick = { onMenuClick(item.key) }
                )
            }

            // Bottom Spacing (agar item bawah tidak tertutup navigasi bottom bar)
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun MenuGridCard(
    item: MenuItemData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(item.iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

// =============================================================
// PREVIEW ANDROID STUDIO
// =============================================================

@Preview(
    name = "Menu Cards - OWNER",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun MenuScreenOwnerPreview() {
    MaterialTheme {
        MenuScreen(
            role = "OWNER",
            onMenuClick = {}
        )
    }
}

@Preview(
    name = "Menu Cards - KASIR",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun MenuScreenKasirPreview() {
    MaterialTheme {
        MenuScreen(
            role = "KASIR",
            onMenuClick = {}
        )
    }
}