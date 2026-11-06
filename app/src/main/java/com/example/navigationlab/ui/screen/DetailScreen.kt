package com.example.navigationlab.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.navigationlab.ui.detail.DetailUiState

@Composable
fun DetailScreen(uiState: DetailUiState, onBack: () -> Unit, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(24.dp).testTag("detail_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Detail Student", style = MaterialTheme.typography.headlineMedium)
        when {
            uiState.isLoading -> CircularProgressIndicator(Modifier.testTag("detail_loading"))
            uiState.errorMessage != null -> Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Data tidak tersedia", style = MaterialTheme.typography.titleLarge)
                    Text(uiState.errorMessage)
                    Button(onClick = onRetry, modifier = Modifier.testTag("detail_retry")) { Text("Coba Lagi") }
                }
            }
            uiState.student != null -> {
                val student = uiState.student
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("ID: ${student.id}", modifier = Modifier.testTag("detail_id"))
                        Text("Nama: ${student.name}", modifier = Modifier.testTag("detail_name"))
                        Text("Email: ${student.email}")
                        Text("University: ${student.university}")
                    }
                }
            }
        }
        Button(onClick = onBack, modifier = Modifier.testTag("back_button")) {
            Text(if (uiState.errorMessage != null) "Kembali ke Home" else "Kembali")
        }
    }
}
