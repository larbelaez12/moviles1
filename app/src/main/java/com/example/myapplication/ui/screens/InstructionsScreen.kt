package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.example.myapplication.R
import com.example.myapplication.ui.theme.SweetestCatEveFontFamily

@Composable
fun InstructionsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2B2B))
            .padding(0.dp)
    ) {
        // Flecha y título alineados en la misma línea, con título centrado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás",
                tint = Color(0xFFFF6A00),
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.CenterStart)
                    .clickable { onBack() }
            )

            Text(
                text = "Reglas del Juego",
                color = Color(0xFFFF6A00),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                fontFamily = SweetestCatEveFontFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "¿Cómo se juega?",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Los jugadores forman un círculo y en el centro colocan el dispositivo móvil, luego tocan el botón parpadeante para girar la botella. El jugador que señale la botella debe cumplir el reto que lanza la app, de lo contrario abandona el juego.",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "¿Quién gana?",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Gana el último jugador que no abandone el juego.",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Imagen del niño con trofeo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(520.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.nino_trofeo),
                contentDescription = "Niño con trofeo",
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .scale(1.35f),
                contentScale = ContentScale.Fit
            )
        }
    }
}
