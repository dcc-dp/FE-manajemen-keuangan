package com.example.umkmfinance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.umkmfinance.theme.UmkmFinanceTheme
import com.example.umkmfinance.ui.auth.LoginViewModel
import com.example.umkmfinance.ui.auth.LoginViewModelFactory
import com.example.umkmfinance.ui.navigation.AppNavHost

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Menampilkan Splash Screen terlebih dahulu
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {

            UmkmFinanceTheme {

                val loginViewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(
                        applicationContext
                    )
                )

                AppNavHost(
                    loginViewModel = loginViewModel
                )
            }
        }
    }
}