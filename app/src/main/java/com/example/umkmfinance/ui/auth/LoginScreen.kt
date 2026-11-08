package com.example.umkmfinance.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.umkmfinance.R

private val GreenPrimary = Color(0xFF005A3C)
private val GreenDark = Color(0xFF00452E)
private val GreenLight = Color(0xFFE7F5EE)
private val BackgroundColor = Color(0xFFF8F8FC)
private val SoftGreen = Color(0xFFEAF7EF)
private val TextPrimary = Color(0xFF10201A)
private val TextSecondary = Color(0xFF66736D)
private val ErrorBackground = Color(0xFFFFEBEE)
private val ErrorText = Color(0xFFB3261E)

// =============================================================
// SCREEN UTAMA (MENGGUNAKAN VIEWMODEL)
// =============================================================
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess, uiState.role) {
        val role = uiState.role
        if (uiState.isSuccess && role != null) {
            onLoginSuccess(role)
            viewModel.resetState()
        }
    }

    LoginScreenContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onLoginClick = { username, password ->
            viewModel.login(username, password)
        },
        onRegisterClick = {
            // Aksi ke Register
        },
        onForgotPasswordClick = {
            // Aksi ke Lupa Kata Sandi
        }
    )
}

// =============================================================
// CONTENT UI (STATELESS - BISA DITAMPILKAN DI PREVIEW SPLIT)
// =============================================================
@Composable
fun LoginScreenContent(
    isLoading: Boolean,
    errorMessage: String?,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedAccount by remember { mutableStateOf("owner") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // =========================================================
            // LOGO & JUDUL APLIKASI
            // =========================================================
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "UMKM Finance",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // =========================================================
            // BADGE
            // =========================================================
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFEAF0FF))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "🛡  Sistem Keuangan UMKM",
                    color = GreenPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // =========================================================
            // GAMBAR LOGO UTAMA
            // =========================================================
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE0E5E2),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = "Logo UMKM Finance"
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(SoftGreen)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "📈  Kelola Keuangan UMKM Lebih Mudah",
                    color = GreenPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // =========================================================
            // TEKS SAMBUTAN
            // =========================================================
            Text(
                text = "Selamat Datang Kembali!",
                color = TextPrimary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kelola pembukuan, arus kas, dan keuangan usaha Anda dengan mudah.",
                color = TextSecondary,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(26.dp))

            // =========================================================
            // PILIHAN JENIS AKUN
            // =========================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFEEF1FB))
                    .padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AccountTypeButton(
                    title = "Pemilik / Admin",
                    selected = selectedAccount == "owner",
                    modifier = Modifier.weight(1f),
                    onClick = { selectedAccount = "owner" }
                )

                AccountTypeButton(
                    title = "Kasir / Staf",
                    selected = selectedAccount == "kasir",
                    modifier = Modifier.weight(1f),
                    onClick = { selectedAccount = "kasir" }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // =========================================================
            // FORM LOGIN
            // =========================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Text(
                    text = "Username",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Masukkan username Anda",
                            color = Color(0xFF9AA39F)
                        )
                    },
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Kata Sandi",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Masukkan kata sandi Anda",
                            color = Color(0xFF9AA39F)
                        )
                    },
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        Text(
                            text = if (passwordVisible) "Sembunyikan" else "Lihat",
                            color = GreenPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { passwordVisible = !passwordVisible }
                                .padding(6.dp)
                        )
                    },
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // INGAT SAYA & LUPA SANDI
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "☑", color = GreenPrimary, fontSize = 23.sp)
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(text = "Ingat Saya", color = TextPrimary, fontSize = 14.sp)
                    }

                    TextButton(onClick = onForgotPasswordClick) {
                        Text(
                            text = "Lupa Kata Sandi?",
                            color = GreenPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ERROR NOTIFIKASI
                errorMessage?.let { message ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ErrorBackground)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = message,
                            color = ErrorText,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // TOMBOL LOGIN
                Button(
                    onClick = { onLoginClick(username, password) },
                    enabled = !isLoading && username.isNotBlank() && password.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenPrimary,
                        disabledContainerColor = Color(0xFFB8C8C0)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Memproses...",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "Masuk Sekarang  →",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // PEMBATAS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFE1E6E3)
                    )
                    Text(
                        text = "  ATAU  ",
                        color = Color(0xFF737D77),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFE1E6E3)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // BIOMETRIK DUMMY
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF0F3FF))
                        .padding(17.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔐  Masuk dengan Sidik Jari / Face ID",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // DAFTAR
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Belum punya akun usaha?",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                TextButton(onClick = onRegisterClick) {
                    Text(
                        text = "Daftar Akun Baru",
                        color = GreenPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // KEAMANAN
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFEFF3FF))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFDDE7FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🛡", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "KEAMANAN DATA",
                        color = GreenPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Data login dan informasi keuangan dilindungi oleh sistem autentikasi aplikasi.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "☎  Butuh bantuan menggunakan aplikasi?",
                color = GreenPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "▣  Hubungi Pendamping UMKM",
                color = GreenPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "© 2026 UMKM Finance",
                color = Color(0xFF737D77),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

// =============================================================
// KOMPONEN TOMBOL PILIHAN AKUN
// =============================================================
@Composable
private fun AccountTypeButton(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Color.White else Color.Transparent
    val textColor = if (selected) GreenPrimary else TextSecondary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

// =============================================================
// PREVIEW UNTUK SPLIT MODE ANDROID STUDIO
// =============================================================
@Preview(
    name = "Login Screen - Normal",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreenContent(
            isLoading = false,
            errorMessage = null,
            onLoginClick = { _, _ -> },
            onRegisterClick = {},
            onForgotPasswordClick = {}
        )
    }
}