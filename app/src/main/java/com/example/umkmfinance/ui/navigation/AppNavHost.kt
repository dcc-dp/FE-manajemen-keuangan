package com.example.umkmfinance.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.umkmfinance.ui.auth.LoginScreen
import com.example.umkmfinance.ui.auth.LoginViewModel
import com.example.umkmfinance.ui.dashboard.DashboardViewModel
import com.example.umkmfinance.ui.dashboard.DashboardViewModelFactory

@Composable
fun AppNavHost(
    loginViewModel: LoginViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { role ->

                    when (role) {

                        "OWNER" -> {
                            navController.navigate("main/OWNER") {
                                popUpTo("login") {
                                    inclusive = true
                                }
                            }
                        }

                        "ADMIN" -> {
                            navController.navigate("main/ADMIN") {
                                popUpTo("login") {
                                    inclusive = true
                                }
                            }
                        }

                        "KASIR" -> {
                            navController.navigate("main/KASIR") {
                                popUpTo("login") {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            )
        }

        composable("main/{role}") { backStackEntry ->

            val role = backStackEntry.arguments
                ?.getString("role")
                ?: "UNKNOWN"

            val context = LocalContext.current

            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModelFactory(
                    context
                )
            )

            MainScreen(
                role = role,
                dashboardViewModel = dashboardViewModel
            )
        }
    }
}