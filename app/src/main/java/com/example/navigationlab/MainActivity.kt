package com.example.navigationlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.navigationlab.ui.theme.NavigationLabTheme
import com.example.navigationlab.navigation.NavigationLabApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NavigationLabTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavigationLabApp()
                }
            }
        }
    }
}
