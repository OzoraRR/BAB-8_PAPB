package com.example.navigationlab.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium)
        Text("Ozora Nathalnael Purba", style = MaterialTheme.typography.titleLarge)
        Text("NIM: 245150207111102")
        Text("Teknik Informatika - TIF D")
        Text("Fakultas Ilmu Komputer, Universitas Brawijaya")
        Button(onClick = onBack) { Text("Kembali") }
    }
}
