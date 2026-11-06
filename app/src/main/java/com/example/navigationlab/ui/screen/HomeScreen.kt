package com.example.navigationlab.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.navigationlab.ui.home.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    searchQuery: String,
    onOpenDetail: (Int) -> Unit,
    onQueryChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenAbout: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    Column(Modifier.fillMaxSize().imePadding().padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Home", style = MaterialTheme.typography.headlineMedium)
        Text("Pencarian Profil", style = MaterialTheme.typography.titleLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onOpenProfile, modifier = Modifier.weight(1f)) { Text("Profile") }
            OutlinedButton(onClick = onOpenAbout, modifier = Modifier.weight(1f)) { Text("About") }
        }
        OutlinedTextField(
            value = searchQuery, onValueChange = onQueryChange,
            label = { Text("Nama profil") }, placeholder = { Text("Contoh: John") },
            singleLine = true, modifier = Modifier.fillMaxWidth().testTag("search_query")
        )
        Button(onClick = { keyboard?.hide(); onRefresh() }, enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth().testTag("refresh_button")) { Text("Refresh") }
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(Modifier.testTag("home_loading"))
                Text("Memuat profil...")
            }
            uiState.errorMessage != null -> {
                Text("Gagal memuat profil", style = MaterialTheme.typography.titleMedium)
                Text(uiState.errorMessage, Modifier.testTag("home_error"))
                Button(onClick = onRetry, modifier = Modifier.testTag("home_retry")) { Text("Coba Lagi") }
            }
            else -> {
                Text(if (uiState.resultQuery.isEmpty()) "Daftar awal (${uiState.students.size} profil)"
                    else "Hasil untuk \"${uiState.resultQuery}\" (${uiState.students.size} profil)",
                    style = MaterialTheme.typography.titleMedium, modifier = Modifier.testTag("result_summary"))
                if (uiState.students.isEmpty()) Text("Tidak ada profil yang cocok. Coba kata lain.", Modifier.testTag("empty_results"))
                LazyColumn(Modifier.weight(1f).testTag("student_list"), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(uiState.students, key = { it.id }) { student ->
                        Card(onClick = { onOpenDetail(student.id) }, modifier = Modifier.fillMaxWidth().testTag("student_${student.id}")) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("ID: ${student.id}", style = MaterialTheme.typography.labelLarge)
                                Text(student.name, style = MaterialTheme.typography.titleMedium)
                                Text(student.email, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
