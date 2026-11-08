package com.example.umkmfinance.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.umkmfinance.ui.auth.LoginScreen
import com.example.umkmfinance.ui.auth.LoginViewModel
import com.example.umkmfinance.ui.dashboard.DashboardViewModel
import com.example.umkmfinance.ui.dashboard.DashboardViewModelFactory
import com.example.umkmfinance.ui.pelanggan.DetailPelangganScreen
import com.example.umkmfinance.ui.pelanggan.PelangganScreen
import com.example.umkmfinance.ui.pelanggan.PelangganViewModel
import com.example.umkmfinance.ui.pelanggan.PelangganViewModelFactory
import com.example.umkmfinance.ui.pembelian.DetailPembelianScreen
import com.example.umkmfinance.ui.pembelian.PembelianScreen
import com.example.umkmfinance.ui.pembelian.PembelianViewModel
import com.example.umkmfinance.ui.pembelian.PembelianViewModelFactory
import com.example.umkmfinance.ui.produk.DetailProdukScreen
import com.example.umkmfinance.ui.produk.ProdukScreen
import com.example.umkmfinance.ui.produk.ProdukViewModel
import com.example.umkmfinance.ui.produk.ProdukViewModelFactory
import com.example.umkmfinance.ui.supplier.DetailSupplierScreen
import com.example.umkmfinance.ui.supplier.SupplierScreen
import com.example.umkmfinance.ui.supplier.SupplierViewModel
import com.example.umkmfinance.ui.supplier.SupplierViewModelFactory

@Composable
fun AppNavHost(
    loginViewModel: LoginViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // ==========================================
        // AUTHENTICATION
        // ==========================================
        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { role ->
                    val targetRole = when (role.uppercase()) {
                        "OWNER" -> "OWNER"
                        "ADMIN" -> "ADMIN"
                        "KASIR" -> "KASIR"
                        else -> "UNKNOWN"
                    }
                    navController.navigate("main/$targetRole") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // ==========================================
        // MAIN SCREEN
        // ==========================================
        composable("main/{role}") { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "UNKNOWN"
            val context = LocalContext.current

            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModelFactory(context)
            )

            MainScreen(
                role = role,
                dashboardViewModel = dashboardViewModel
            )
        }

        // ==========================================
        // PRODUK
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
                }
            )
        }

        composable("detail_produk/{produkId}") { backStackEntry ->
            val produkId = backStackEntry.arguments?.getString("produkId")?.toLongOrNull() ?: 0L
            val context = LocalContext.current
            val produkViewModel: ProdukViewModel = viewModel(
                factory = ProdukViewModelFactory(context)
            )

            DetailProdukScreen(
                produkId = produkId,
                viewModel = produkViewModel
            )
        }

        // ==========================================
        // PELANGGAN
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
                }
            )
        }

        composable("detail_pelanggan/{pelangganId}") { backStackEntry ->
            val pelangganId = backStackEntry.arguments?.getString("pelangganId")?.toLongOrNull() ?: 0L
            val context = LocalContext.current
            val pelangganViewModel: PelangganViewModel = viewModel(
                factory = PelangganViewModelFactory(context)
            )

            DetailPelangganScreen(
                pelangganId = pelangganId,
                viewModel = pelangganViewModel
            )
        }

        // ==========================================
        // PEMBELIAN
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
            val pembelianId = backStackEntry.arguments?.getString("pembelianId")?.toLongOrNull() ?: 0L
            val context = LocalContext.current
            val pembelianViewModel: PembelianViewModel = viewModel(
                factory = PembelianViewModelFactory(context)
            )

            DetailPembelianScreen(
                pembelianId = pembelianId,
                viewModel = pembelianViewModel
            )
        }

        // ==========================================
        // SUPPLIER
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
            val supplierId = backStackEntry.arguments?.getString("supplierId")?.toLongOrNull() ?: 0L
            val context = LocalContext.current
            val supplierViewModel: SupplierViewModel = viewModel(
                factory = SupplierViewModelFactory(context)
            )

            DetailSupplierScreen(
                supplierId = supplierId,
                viewModel = supplierViewModel
            )
        }
    }
}