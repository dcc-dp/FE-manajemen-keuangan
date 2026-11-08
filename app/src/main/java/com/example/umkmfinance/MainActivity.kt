package com.example.umkmfinance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

        // Mengaktifkan tampilan layar penuh (edge-to-edge)
        enableEdgeToEdge()

        setContent {
            UmkmFinanceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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
}

// =============================================================
// PREVIEW UNTUK SPLIT MODE DI MAINACTIVITY
// =============================================================
@Preview(
    name = "Main Activity Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun MainActivityPreview() {
    UmkmFinanceTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Menampilkan representasi layout awal aplikasi
            androidx.compose.material3.Text(
                text = "UMKM Finance App",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}