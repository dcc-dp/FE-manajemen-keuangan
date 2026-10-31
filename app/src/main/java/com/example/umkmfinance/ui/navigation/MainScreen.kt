package com.example.umkmfinance.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.umkmfinance.ui.dashboard.DashboardScreen
import com.example.umkmfinance.ui.dashboard.DashboardViewModel
import com.example.umkmfinance.ui.produk.DetailProdukScreen
import com.example.umkmfinance.ui.produk.ProdukScreen
import com.example.umkmfinance.ui.produk.ProdukViewModel
import com.example.umkmfinance.ui.pelanggan.PelangganScreen
import com.example.umkmfinance.ui.pelanggan.PelangganViewModel
import com.example.umkmfinance.ui.pelanggan.PelangganViewModelFactory
import com.example.umkmfinance.ui.produk.ProdukViewModelFactory
import com.example.umkmfinance.ui.pelanggan.DetailPelangganScreen
import com.example.umkmfinance.ui.pelanggan.DetailPelangganScreen
import com.example.umkmfinance.ui.pelanggan.PelangganScreen
import com.example.umkmfinance.data.model.Pelanggan

@Composable
fun MainScreen(
    role: String,
    dashboardViewModel: DashboardViewModel
) {

    val navController = rememberNavController()

    val navBackStackEntry by navController
        .currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry?.destination?.route
            ?: "dashboard"

    Scaffold(

        bottomBar = {

            NavigationBar {

                // ==============================
                // DASHBOARD
                // ==============================

                NavigationBarItem(
                    selected = currentRoute == "dashboard",
                    onClick = {
                        navController.navigate("dashboard") {
                            popUpTo("dashboard")
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Dashboard"
                        )
                    },
                    label = {
                        Text("Dashboard")
                    }
                )

                // ==============================
                // TRANSAKSI
                // ==============================

                NavigationBarItem(
                    selected = currentRoute == "transaksi",
                    onClick = {
                        navController.navigate("transaksi") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Transaksi"
                        )
                    },
                    label = {
                        Text("Transaksi")
                    }
                )

                // ==============================
                // LAPORAN
                // ==============================

                NavigationBarItem(
                    selected = currentRoute == "laporan",
                    onClick = {
                        navController.navigate("laporan") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Laporan"
                        )
                    },
                    label = {
                        Text("Laporan")
                    }
                )

                // ==============================
                // MENU
                // ==============================

                NavigationBarItem(
                    selected = currentRoute == "menu",
                    onClick = {
                        navController.navigate("menu") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    },
                    label = {
                        Text("Menu")
                    }
                )
            }
        }

    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(paddingValues)
        ) {

            // ==============================
            // DASHBOARD
            // ==============================

            composable("dashboard") {

                DashboardScreen(
                    role = role,
                    viewModel = dashboardViewModel
                )
            }

            // ==============================
            // TRANSAKSI
            // ==============================

            composable("transaksi") {

                Text(
                    text = "Halaman Transaksi"
                )
            }

            // ==============================
            // LAPORAN
            // ==============================

            composable("laporan") {

                Text(
                    text = "Halaman Laporan"
                )
            }



            // ==============================
            // MENU
            // ==============================

            composable("menu") {

                MenuScreen(
                    role = role,
                    onMenuClick = { menu ->

                        when (menu) {

                            "Produk" -> {
                                navController.navigate("produk")
                            }

                            "Pelanggan" -> {
                                navController.navigate("pelanggan")
                            }
                        }
                    }
                )
            }

            // ==============================
            // DAFTAR PRODUK
            // ==============================

            composable("produk") {

                val context =
                    androidx.compose.ui.platform.LocalContext.current

                val produkViewModel: ProdukViewModel = viewModel(
                    factory = ProdukViewModelFactory(context)
                )

                ProdukScreen(
                    viewModel = produkViewModel,

                    onProdukClick = { produk ->

                        navController.navigate(
                            "detail_produk/${produk.id}"
                        )
                    }
                )
            }

            // ==============================
            // DETAIL PRODUK
            // ==============================

            composable("detail_produk/{produkId}") { backStackEntry ->

                val produkId = backStackEntry
                    .arguments
                    ?.getString("produkId")
                    ?.toLongOrNull()

                val context =
                    androidx.compose.ui.platform.LocalContext.current

                val produkViewModel: ProdukViewModel = viewModel(
                    factory = ProdukViewModelFactory(context)
                )

                if (produkId != null) {

                    DetailProdukScreen(
                        produkId = produkId,
                        viewModel = produkViewModel
                    )

                } else {

                    Text(
                        text = "ID produk tidak valid"
                    )
                }
            }
            composable("pelanggan") {

                val context =
                    androidx.compose.ui.platform.LocalContext.current

                val pelangganViewModel: PelangganViewModel =
                    viewModel(
                        factory =
                            PelangganViewModelFactory(context)
                    )

                PelangganScreen(
                    viewModel = pelangganViewModel,

                    onPelangganClick = { pelanggan ->

                        navController.navigate(
                            "detail_pelanggan/${pelanggan.id}"
                        )
                    }
                )
            }
            composable("detail_pelanggan/{pelangganId}") { backStackEntry ->

                val pelangganId =
                    backStackEntry.arguments
                        ?.getString("pelangganId")
                        ?.toLongOrNull()

                val context =
                    androidx.compose.ui.platform.LocalContext.current

                val pelangganViewModel: PelangganViewModel =
                    viewModel(
                        factory =
                            PelangganViewModelFactory(context)
                    )

                if (pelangganId != null) {

                    DetailPelangganScreen(
                        pelangganId = pelangganId,
                        viewModel = pelangganViewModel
                    )

                } else {

                    Text(
                        text = "ID pelanggan tidak valid"
                    )
                }
            }
        }
    }
}