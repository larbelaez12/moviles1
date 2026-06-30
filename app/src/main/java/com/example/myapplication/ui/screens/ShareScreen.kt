package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.components.TitleTopBar

@Composable
fun ShareScreen(onBack: () -> Unit, onShare: () -> Unit) {
    LaunchedEffect(Unit) {
        onShare()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2B2B))
            .padding(16.dp)
    ) {
        TitleTopBar(title = "Compartir", onBack = onBack)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Selecciona un medio para compartir la app.",
            color = Color.White
        )
    }
}

