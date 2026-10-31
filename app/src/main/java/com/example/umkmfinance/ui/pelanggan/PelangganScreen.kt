package com.example.umkmfinance.ui.pelanggan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.Pelanggan

@Composable
fun PelangganScreen(
    viewModel: PelangganViewModel,
    onPelangganClick: (Pelanggan) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPelanggan()
    }

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

                Text(
                    text = uiState.errorMessage
                        ?: "Terjadi kesalahan"
                )
            }
        }

        else -> {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                item {

                    Text(
                        text = "Pelanggan",
                        style =
                            MaterialTheme.typography
                                .headlineMedium,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            "${uiState.data.size} pelanggan",
                        style =
                            MaterialTheme.typography
                                .bodyMedium
                    )
                }

                if (uiState.data.isEmpty()) {

                    item {

                        Text(
                            text =
                                "Belum ada pelanggan."
                        )
                    }

                } else {

                    items(uiState.data) { pelanggan ->

                        PelangganCard(
                            pelanggan = pelanggan,
                            onClick = {
                                onPelangganClick(pelanggan)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PelangganCard(
    pelanggan: Pelanggan,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = pelanggan.nama,
                style =
                    MaterialTheme.typography
                        .titleLarge,
                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text =
                    "Telepon: ${
                        pelanggan.telepon ?: "-"
                    }"
            )

            Text(
                text =
                    "Email: ${
                        pelanggan.email ?: "-"
                    }"
            )

            Text(
                text =
                    "Alamat: ${
                        pelanggan.alamat ?: "-"
                    }"
            )

            Text(
                text =
                    "Poin: ${pelanggan.poin}"
            )
        }
    }
}