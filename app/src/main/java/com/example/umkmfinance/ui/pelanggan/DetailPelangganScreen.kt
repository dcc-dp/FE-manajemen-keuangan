package com.example.umkmfinance.ui.pelanggan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DetailPelangganScreen(
    pelangganId: Long,
    viewModel: PelangganViewModel
) {

    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(pelangganId) {
        viewModel.loadPelangganById(pelangganId)
    }

    when {

        // ==========================================
        // LOADING
        // ==========================================

        uiState.isLoading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }
        }


        // ==========================================
        // ERROR
        // ==========================================

        uiState.errorMessage != null -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = uiState.errorMessage
                        ?: "Terjadi kesalahan"
                )
            }
        }


        // ==========================================
        // DATA BERHASIL
        // ==========================================

        uiState.data != null -> {

            val pelanggan = uiState.data!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),

                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Detail Pelanggan",
                    style =
                        MaterialTheme.typography
                            .headlineMedium,

                    fontWeight =
                        FontWeight.Bold
                )


                // ==================================
                // CARD DETAIL
                // ==================================

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = pelanggan.nama,

                            style =
                                MaterialTheme.typography
                                    .titleLarge,

                            fontWeight =
                                FontWeight.Bold
                        )

                        DetailItem(
                            label = "ID Pelanggan",
                            value = pelanggan.id.toString()
                        )

                        DetailItem(
                            label = "Nomor Telepon",
                            value =
                                pelanggan.telepon
                                    ?: "-"
                        )

                        DetailItem(
                            label = "Email",
                            value =
                                pelanggan.email
                                    ?: "-"
                        )

                        DetailItem(
                            label = "Alamat",
                            value =
                                pelanggan.alamat
                                    ?: "-"
                        )

                        DetailItem(
                            label = "Poin",
                            value =
                                pelanggan.poin.toString()
                        )
                    }
                }
            }
        }
    }
}


// ==============================================
// DETAIL ITEM
// ==============================================

@Composable
private fun DetailItem(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = label,

            style =
                MaterialTheme.typography
                    .bodyMedium
        )

        Text(
            text = value,

            style =
                MaterialTheme.typography
                    .bodyLarge,

            fontWeight =
                FontWeight.SemiBold
        )
    }
}