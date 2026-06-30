package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.components.TitleTopBar

@Composable
fun RateScreen(onBack: () -> Unit, onOpenStore: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2B2B))
            .padding(16.dp)
    ) {
        TitleTopBar(title = "Calificar", onBack = onBack)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Para calificar, te llevamos a la app de Nequi en Google Play.",
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onOpenStore,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A00))
        ) {
            Text(text = "Ir a Google Play", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

