package com.example.umkmfinance.ui.pengeluaran

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umkmfinance.data.model.KategoriPengeluaran
import com.example.umkmfinance.data.model.Rekening
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class FormTambahPengeluaranInput(
    val kategori: KategoriPengeluaran?,
    val rekeningId: Long,
    val tanggal: String,
    val nominal: Double,
    val keterangan: String?
)

@Composable
fun TambahPengeluaranDialog(
    kategoriAktif: KategoriPengeluaran? = null,
    rekeningList: List<Rekening> = defaultRekeningOptions(),
    onDismiss: () -> Unit,
    onSimpan: (FormTambahPengeluaranInput) -> Unit
) {
    val defaultTanggal = remember {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID")).format(Date())
    }

    var selectedRekeningId by remember(rekeningList) {
        mutableStateOf(rekeningList.firstOrNull()?.id ?: 1L)
    }
    var nominalInput by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf(defaultTanggal) }
    var keterangan by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    val selectedRekening = remember(rekeningList, selectedRekeningId) {
        rekeningList.find { it.id == selectedRekeningId }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Tambah Pengeluaran",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (kategoriAktif != null) {
                    Text(
                        text = "Kategori: ${kategoriAktif.nama}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "Rekening Sumber Dana",
                    style = MaterialTheme.typography.labelMedium
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rekeningList.forEach { rekening ->
                        FilterChip(
                            selected = selectedRekeningId == rekening.id,
                            onClick = {
                                selectedRekeningId = rekening.id
                                errorText = null
                            },
                            label = { Text("${rekening.nama_rekening} (${rekening.bank})") }
                        )
                    }
                }

                if (selectedRekening != null) {
                    Text(
                        text = "Saldo tersedia: ${formatRupiahSingkat(selectedRekening.saldo)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedTextField(
                    value = nominalInput,
                    onValueChange = {
                        nominalInput = it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' }
                        errorText = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nominal (Rp)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = tanggal,
                    onValueChange = { tanggal = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Tanggal") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = keterangan,
                    onValueChange = { keterangan = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Keterangan (Opsional)") },
                    placeholder = { Text("Contoh: Bayar listrik & air bulan ini") },
                    shape = RoundedCornerShape(10.dp)
                )

                if (errorText != null) {
                    Text(
                        text = errorText ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val nominal = nominalInput.trim().replace(",", ".").toDoubleOrNull() ?: 0.0
                    when {
                        selectedRekeningId <= 0L -> errorText = "Pilih rekening sumber dana terlebih dahulu."
                        nominal <= 0.0 -> errorText = "Nominal pengeluaran harus lebih dari 0."
                        else -> {
                            onSimpan(
                                FormTambahPengeluaranInput(
                                    kategori = kategoriAktif,
                                    rekeningId = selectedRekeningId,
                                    tanggal = tanggal.trim().ifBlank { defaultTanggal },
                                    nominal = nominal,
                                    keterangan = keterangan.trim().ifBlank { null }
                                )
                            )
                        }
                    }
                }
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

private fun formatRupiahSingkat(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

private fun defaultRekeningOptions(): List<Rekening> = listOf(
    Rekening(id = 1L, usaha_id = 1L, nama_rekening = "Kas Tunai Toko", bank = "KAS", saldo = 2450000.0),
    Rekening(id = 2L, usaha_id = 1L, nama_rekening = "Rekening Operasional", bank = "BRI", saldo = 12850000.0)
)

@Preview(showBackground = true)
@Composable
private fun TambahPengeluaranDialogPreview() {
    val kategoriAktif = KategoriPengeluaran(id = 1L, usaha_id = 1L, nama = "Listrik & Air")
    val sampleRekening = defaultRekeningOptions()

    MaterialTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Tambah Pengeluaran",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Kategori: ${kategoriAktif.nama}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Rekening Sumber Dana",
                    style = MaterialTheme.typography.labelMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sampleRekening.forEachIndexed { index, rekening ->
                        FilterChip(
                            selected = index == 0,
                            onClick = {},
                            label = { Text("${rekening.nama_rekening} (${rekening.bank})") }
                        )
                    }
                }
                OutlinedTextField(
                    value = "350000",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nominal (Rp)") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = "2026-11-28 17:07:00",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Tanggal") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = "Bayar listrik & air bulan ini",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Keterangan (Opsional)") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {}) {
                        Text("Batal")
                    }
                    TextButton(onClick = {}) {
                        Text("Simpan")
                    }
                }
            }
        }
    }
}