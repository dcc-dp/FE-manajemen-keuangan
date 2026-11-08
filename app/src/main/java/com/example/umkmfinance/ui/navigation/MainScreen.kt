package com.example.umkmfinance.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.umkmfinance.ui.dashboard.DashboardScreen
import com.example.umkmfinance.ui.dashboard.DashboardViewModel
import com.example.umkmfinance.ui.hutang.DetailHutangScreen
import com.example.umkmfinance.ui.hutang.HutangScreen
import com.example.umkmfinance.ui.hutang.HutangViewModel
import com.example.umkmfinance.ui.hutang.HutangViewModelFactory
import com.example.umkmfinance.ui.pelanggan.DetailPelangganScreen
import com.example.umkmfinance.ui.pelanggan.PelangganScreen
import com.example.umkmfinance.ui.pelanggan.PelangganViewModel
import com.example.umkmfinance.ui.pelanggan.PelangganViewModelFactory
import com.example.umkmfinance.ui.pelanggan.TambahPelangganScreen
import com.example.umkmfinance.ui.pembelian.DetailPembelianScreen
import com.example.umkmfinance.ui.pembelian.PembelianScreen
import com.example.umkmfinance.ui.pembelian.PembelianViewModel
import com.example.umkmfinance.ui.pembelian.PembelianViewModelFactory
import com.example.umkmfinance.ui.pengeluaran.DetailPengeluaranScreen
import com.example.umkmfinance.ui.pengeluaran.PengeluaranScreen
import com.example.umkmfinance.ui.pengeluaran.PengeluaranViewModel
import com.example.umkmfinance.ui.pengeluaran.PengeluaranViewModelFactory
import com.example.umkmfinance.ui.penjualan.DetailPenjualanScreen
import com.example.umkmfinance.ui.penjualan.PenjualanScreen
import com.example.umkmfinance.ui.penjualan.PenjualanViewModel
import com.example.umkmfinance.ui.penjualan.PenjualanViewModelFactory
import com.example.umkmfinance.ui.piutang.DetailPiutangScreen
import com.example.umkmfinance.ui.piutang.PiutangScreen
import com.example.umkmfinance.ui.piutang.PiutangViewModel
import com.example.umkmfinance.ui.piutang.PiutangViewModelFactory
import com.example.umkmfinance.ui.produk.DetailProdukScreen
import com.example.umkmfinance.ui.produk.ProdukScreen
import com.example.umkmfinance.ui.produk.ProdukViewModel
import com.example.umkmfinance.ui.produk.ProdukViewModelFactory
import com.example.umkmfinance.ui.produk.TambahProdukScreen
import com.example.umkmfinance.ui.rekening.DetailRekeningScreen
import com.example.umkmfinance.ui.rekening.RekeningScreen
import com.example.umkmfinance.ui.rekening.RekeningViewModel
import com.example.umkmfinance.ui.rekening.RekeningViewModelFactory
import com.example.umkmfinance.ui.supplier.DetailSupplierScreen
import com.example.umkmfinance.ui.supplier.SupplierScreen
import com.example.umkmfinance.ui.supplier.SupplierViewModel
import com.example.umkmfinance.ui.supplier.SupplierViewModelFactory

@Composable
fun MainScreen(
    role: String,
    dashboardViewModel: DashboardViewModel
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "dashboard"

    MainContentLayout(
        currentRoute = currentRoute,
        onNavigateToRoute = { route ->
            if (route == "dashboard") {
                navController.navigate("dashboard") {
                    popUpTo("dashboard")
                    launchSingleTop = true
                }
            } else {
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(paddingValues)
        ) {
            // DASHBOARD
            composable("dashboard") {
                DashboardScreen(
                    role = role,
                    viewModel = dashboardViewModel
                )
            }

            // TRANSAKSI (MENGARAH LANGSUNG KE MODUL PENJUALAN / KASIR)
            composable("transaksi") {
                val context = LocalContext.current
                val penjualanViewModel: PenjualanViewModel = viewModel(
                    factory = PenjualanViewModelFactory(context)
                )

                PenjualanScreen(
                    viewModel = penjualanViewModel,
                    onPenjualanClick = { penjualan ->
                        navController.navigate("detail_penjualan/${penjualan.id}")
                    }
                )
            }

            // LAPORAN
            composable("laporan") {
                Text(text = "Halaman Laporan")
            }

            // MENU UTAMA
            composable("menu") {
                MenuScreen(
                    role = role,
                    onMenuClick = { menuKey ->
                        when (menuKey) {
                            "Penjualan" -> navController.navigate("transaksi")
                            "Pembelian" -> navController.navigate("pembelian")
                            "Produk" -> navController.navigate("produk")
                            "Pelanggan" -> navController.navigate("pelanggan")
                            "Supplier" -> navController.navigate("supplier")
                            "Pengeluaran" -> navController.navigate("pengeluaran")
                            "Piutang" -> navController.navigate("piutang")
                            "Hutang" -> navController.navigate("hutang")
                            "Rekening" -> navController.navigate("rekening")
                        }
                    }
                )
            }

            // ==========================================
            // MODUL PRODUK
            // ==========================================
            composable("produk") {
                val context = LocalContext.current
                val produkViewModel: ProdukViewModel = viewModel(
                    factory = ProdukViewModelFactory(context)
                )

                ProdukScreen(
                    viewModel = produkViewModel,
                    onProdukClick = { produk ->
                        navController.navigate("detail_produk/${produk.id}")
                    },
                    onTambahProdukClick = {
                        navController.navigate("tambah_produk")
                    }
                )
            }

            // FORM TAMBAH PRODUK BARU
            composable("tambah_produk") {
                TambahProdukScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onSimpanProduk = { _ ->
                        navController.popBackStack()
                    }
                )
            }

            // DETAIL PRODUK (AKSI EDIT & HAPUS)
            composable("detail_produk/{produkId}") { backStackEntry ->
                val produkId = backStackEntry.arguments?.getString("produkId")?.toLongOrNull()
                val context = LocalContext.current
                val produkViewModel: ProdukViewModel = viewModel(
                    factory = ProdukViewModelFactory(context)
                )

                if (produkId != null) {
                    DetailProdukScreen(
                        produkId = produkId,
                        viewModel = produkViewModel,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onEditProdukClick = { id ->
                            navController.navigate("edit_produk/$id")
                        },
                        onProdukDeleted = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    Text(text = "ID produk tidak valid")
                }
            }

            // FORM EDIT PRODUK
            composable("edit_produk/{produkId}") {
                TambahProdukScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onSimpanProduk = { _ ->
                        navController.popBackStack()
                    }
                )
            }

            // ==========================================
            // MODUL PELANGGAN
            // ==========================================
            composable("pelanggan") {
                val context = LocalContext.current
                val pelangganViewModel: PelangganViewModel = viewModel(
                    factory = PelangganViewModelFactory(context)
                )

                PelangganScreen(
                    viewModel = pelangganViewModel,
                    onPelangganClick = { pelanggan ->
                        navController.navigate("detail_pelanggan/${pelanggan.id}")
                    },
                    onTambahPelangganClick = {
                        navController.navigate("tambah_pelanggan")
                    }
                )
            }

            // FORM TAMBAH PELANGGAN BARU
            composable("tambah_pelanggan") {
                TambahPelangganScreen(
                    pelangganToEdit = null,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onSimpanPelanggan = { _, _, _ ->
                        navController.popBackStack()
                    }
                )
            }

            // DETAIL PELANGGAN
            composable("detail_pelanggan/{pelangganId}") { backStackEntry ->
                val pelangganId = backStackEntry.arguments?.getString("pelangganId")?.toLongOrNull()
                val context = LocalContext.current
                val pelangganViewModel: PelangganViewModel = viewModel(
                    factory = PelangganViewModelFactory(context)
                )

                if (pelangganId != null) {
                    DetailPelangganScreen(
                        pelangganId = pelangganId,
                        viewModel = pelangganViewModel,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onEditPelangganClick = { id ->
                            navController.navigate("edit_pelanggan/$id")
                        },
                        onPelangganDeleted = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    Text(text = "ID pelanggan tidak valid")
                }
            }

            // FORM EDIT PELANGGAN
            composable("edit_pelanggan/{pelangganId}") {
                val context = LocalContext.current
                val pelangganViewModel: PelangganViewModel = viewModel(
                    factory = PelangganViewModelFactory(context)
                )
                val detailState by pelangganViewModel.detailUiState.collectAsState()

                TambahPelangganScreen(
                    pelangganToEdit = detailState.data,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onSimpanPelanggan = { _, _, _ ->
                        navController.popBackStack()
                    }
                )
            }

            // ==========================================
            // MODUL PENJUALAN (DETAIL TRANSAKSI / STRUK)
            // ==========================================
            composable("detail_penjualan/{penjualanId}") { backStackEntry ->
                val penjualanId = backStackEntry.arguments?.getString("penjualanId")?.toLongOrNull()
                val context = LocalContext.current
                val penjualanViewModel: PenjualanViewModel = viewModel(
                    factory = PenjualanViewModelFactory(context)
                )

                if (penjualanId != null) {
                    DetailPenjualanScreen(
                        penjualanId = penjualanId,
                        viewModel = penjualanViewModel
                    )
                } else {
                    Text(text = "ID penjualan tidak valid")
                }
            }

            // ==========================================
            // MODUL PEMBELIAN
            // ==========================================
            composable("pembelian") {
                val context = LocalContext.current
                val pembelianViewModel: PembelianViewModel = viewModel(
                    factory = PembelianViewModelFactory(context)
                )

                PembelianScreen(
                    viewModel = pembelianViewModel,
                    onPembelianClick = { pembelian ->
                        navController.navigate("detail_pembelian/${pembelian.id}")
                    }
                )
            }

            composable("detail_pembelian/{pembelianId}") { backStackEntry ->
                val pembelianId = backStackEntry.arguments?.getString("pembelianId")?.toLongOrNull()
                val context = LocalContext.current
                val pembelianViewModel: PembelianViewModel = viewModel(
                    factory = PembelianViewModelFactory(context)
                )

                if (pembelianId != null) {
                    DetailPembelianScreen(
                        pembelianId = pembelianId,
                        viewModel = pembelianViewModel
                    )
                } else {
                    Text(text = "ID pembelian tidak valid")
                }
            }

            // ==========================================
            // MODUL SUPPLIER
            // ==========================================
            composable("supplier") {
                val context = LocalContext.current
                val supplierViewModel: SupplierViewModel = viewModel(
                    factory = SupplierViewModelFactory(context)
                )

                SupplierScreen(
                    viewModel = supplierViewModel,
                    onSupplierClick = { supplier ->
                        navController.navigate("detail_supplier/${supplier.id}")
                    }
                )
            }

            composable("detail_supplier/{supplierId}") { backStackEntry ->
                val supplierId = backStackEntry.arguments?.getString("supplierId")?.toLongOrNull()
                val context = LocalContext.current
                val supplierViewModel: SupplierViewModel = viewModel(
                    factory = SupplierViewModelFactory(context)
                )

                if (supplierId != null) {
                    DetailSupplierScreen(
                        supplierId = supplierId,
                        viewModel = supplierViewModel
                    )
                } else {
                    Text(text = "ID supplier tidak valid")
                }
            }

            // ==========================================
            // MODUL PENGELUARAN
            // ==========================================
            composable("pengeluaran") {
                val context = LocalContext.current
                val pengeluaranViewModel: PengeluaranViewModel = viewModel(
                    factory = PengeluaranViewModelFactory(context)
                )

                PengeluaranScreen(
                    viewModel = pengeluaranViewModel,
                    onPengeluaranClick = { pengeluaran ->
                        navController.navigate("detail_pengeluaran/${pengeluaran.id}")
                    }
                )
            }

            composable("detail_pengeluaran/{pengeluaranId}") { backStackEntry ->
                val pengeluaranId = backStackEntry.arguments?.getString("pengeluaranId")?.toLongOrNull()
                val context = LocalContext.current
                val pengeluaranViewModel: PengeluaranViewModel = viewModel(
                    factory = PengeluaranViewModelFactory(context)
                )

                if (pengeluaranId != null) {
                    DetailPengeluaranScreen(
                        pengeluaranId = pengeluaranId,
                        viewModel = pengeluaranViewModel
                    )
                } else {
                    Text(text = "ID pengeluaran tidak valid")
                }
            }

            // ==========================================
            // MODUL PIUTANG
            // ==========================================
            composable("piutang") {
                val context = LocalContext.current
                val piutangViewModel: PiutangViewModel = viewModel(
                    factory = PiutangViewModelFactory(context)
                )

                PiutangScreen(
                    viewModel = piutangViewModel,
                    onPiutangClick = { piutang ->
                        navController.navigate("detail_piutang/${piutang.id}")
                    }
                )
            }

            composable("detail_piutang/{piutangId}") { backStackEntry ->
                val piutangId = backStackEntry.arguments?.getString("piutangId")?.toLongOrNull()
                val context = LocalContext.current
                val piutangViewModel: PiutangViewModel = viewModel(
                    factory = PiutangViewModelFactory(context)
                )

                if (piutangId != null) {
                    DetailPiutangScreen(
                        piutangId = piutangId,
                        viewModel = piutangViewModel
                    )
                } else {
                    Text(text = "ID piutang tidak valid")
                }
            }

            // ==========================================
            // MODUL HUTANG
            // ==========================================
            composable("hutang") {
                val context = LocalContext.current
                val hutangViewModel: HutangViewModel = viewModel(
                    factory = HutangViewModelFactory(context)
                )

                HutangScreen(
                    viewModel = hutangViewModel,
                    onHutangClick = { hutang ->
                        navController.navigate("detail_hutang/${hutang.id}")
                    }
                )
            }

            composable("detail_hutang/{hutangId}") { backStackEntry ->
                val hutangId = backStackEntry.arguments?.getString("hutangId")?.toLongOrNull()
                val context = LocalContext.current
                val hutangViewModel: HutangViewModel = viewModel(
                    factory = HutangViewModelFactory(context)
                )

                if (hutangId != null) {
                    DetailHutangScreen(
                        hutangId = hutangId,
                        viewModel = hutangViewModel
                    )
                } else {
                    Text(text = "ID hutang tidak valid")
                }
            }

            // ==========================================
            // MODUL REKENING / KAS
            // ==========================================
            composable("rekening") {
                val context = LocalContext.current
                val rekeningViewModel: RekeningViewModel = viewModel(
                    factory = RekeningViewModelFactory(context)
                )

                RekeningScreen(
                    viewModel = rekeningViewModel,
                    onRekeningClick = { rekening ->
                        navController.navigate("detail_rekening/${rekening.id}")
                    }
                )
            }

            composable("detail_rekening/{rekeningId}") { backStackEntry ->
                val rekeningId = backStackEntry.arguments?.getString("rekeningId")?.toLongOrNull()
                val context = LocalContext.current
                val rekeningViewModel: RekeningViewModel = viewModel(
                    factory = RekeningViewModelFactory(context)
                )

                if (rekeningId != null) {
                    DetailRekeningScreen(
                        rekeningId = rekeningId,
                        viewModel = rekeningViewModel
                    )
                } else {
                    Text(text = "ID rekening tidak valid")
                }
            }
        }
    }
}

// =============================================================
// UI SHELL BOTTOM BAR
// =============================================================
@Composable
private fun MainContentLayout(
    currentRoute: String,
    onNavigateToRoute: (String) -> Unit,
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                // DASHBOARD
                NavigationBarItem(
                    selected = currentRoute == "dashboard",
                    onClick = { onNavigateToRoute("dashboard") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Dashboard"
                        )
                    },
                    label = { Text("Dashboard") }
                )

                // TRANSAKSI
                NavigationBarItem(
                    selected = currentRoute == "transaksi",
                    onClick = { onNavigateToRoute("transaksi") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Transaksi"
                        )
                    },
                    label = { Text("Transaksi") }
                )

                // LAPORAN
                NavigationBarItem(
                    selected = currentRoute == "laporan",
                    onClick = { onNavigateToRoute("laporan") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Laporan"
                        )
                    },
                    label = { Text("Laporan") }
                )

                // MENU
                NavigationBarItem(
                    selected = currentRoute == "menu",
                    onClick = { onNavigateToRoute("menu") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    },
                    label = { Text("Menu") }
                )
            }
        },
        content = content
    )
}

// =============================================================
// PREVIEW ANDROID STUDIO
// =============================================================
@Preview(
    name = "Main Screen - Dashboard Active",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun MainScreenPreview() {
    MaterialTheme {
        MainContentLayout(
            currentRoute = "dashboard",
            onNavigateToRoute = {}
        ) { paddingValues ->
            Text(
                text = "Halaman Aktif: Dashboard",
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}