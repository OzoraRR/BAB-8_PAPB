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
fun AboutScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("About", style = MaterialTheme.typography.headlineMedium)
        Text("NavigationLab", style = MaterialTheme.typography.titleLarge)
        Text("Praktikum 08 - Kotlin Coroutines dan Flow")
        Text("Aplikasi untuk mencari profil dan melihat detail dari DummyJSON.")
        Text("Pemrograman Aplikasi Perangkat Bergerak - FILKOM UB 2026")
        Button(onClick = onBack) { Text("Kembali") }
    }
}
