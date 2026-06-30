package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeToolbar(
    isAudioOn: Boolean,
    onRate: () -> Unit,
    onToggleAudio: () -> Unit,
    onInstructions: () -> Unit,
    onChallenges: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.Black,
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 6.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Estrella — calificar app
            PressableIconButton(onClick = onRate) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Calificar",
                    tint = Color(0xFFFF6A00),
                    modifier = Modifier.size(28.dp)
                )
            }

            // Audio toggle — encendido/apagado
            PressableIconButton(onClick = onToggleAudio) {
                if (isAudioOn) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Audio encendido",
                        tint = Color(0xFFFF6A00),
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    // Ícono apagado con fondo naranja circular
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFFFF6A00), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeOff,
                            contentDescription = "Audio apagado",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Instrucciones — ícono gamepad
            PressableIconButton(onClick = onInstructions) {
                Icon(
                    imageVector = Icons.Filled.SportsEsports,
                    contentDescription = "Instrucciones",
                    tint = Color(0xFFFF6A00),
                    modifier = Modifier.size(28.dp)
                )
            }

            // Retos — plus en círculo naranja
            PressableIconButton(onClick = onChallenges) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFFFF6A00), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Agregar retos",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Compartir
            PressableIconButton(onClick = onShare) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Compartir",
                    tint = Color(0xFFFF6A00),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}


