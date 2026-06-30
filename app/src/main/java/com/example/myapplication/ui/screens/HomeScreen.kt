package com.example.myapplication.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.ui.components.HomeToolbar
import com.example.myapplication.util.SpinUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    isAudioOn: Boolean,
    onRate: () -> Unit,
    onToggleAudio: () -> Unit,
    onInstructions: () -> Unit,
    onChallenges: () -> Unit,
    onShare: () -> Unit,
    onPauseBackground: () -> Unit,
    onResumeBackground: () -> Unit,
    onPlaySpinSound: (Long) -> Unit,
    loadRandomChallenge: suspend () -> String,
    loadRandomPokemonUrl: suspend () -> String?
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val rotation = remember { Animatable(0f) }
    val countdownScale = remember { Animatable(0.6f) }
    val countdownAlpha = remember { Animatable(0f) }
    var isSpinning by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf<Int?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var challengeText by remember { mutableStateOf("") }
    var pokemonUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(countdown) {
        if (countdown != null) {
            countdownScale.snapTo(0.6f)
            countdownAlpha.snapTo(0f)
            countdownScale.animateTo(1.18f, tween(130))
            countdownScale.animateTo(1.0f, tween(120))
            countdownAlpha.animateTo(1f, tween(90))
        } else {
            countdownScale.snapTo(0.6f)
            countdownAlpha.snapTo(0f)
        }
    }

    val blinkTransition = rememberInfiniteTransition(label = "blink")
    val blinkAlpha by blinkTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink-alpha"
    )

    val ringScale by blinkTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring-scale"
    )

    val ringAlpha by blinkTransition.animateFloat(
        initialValue = 0.42f,
        targetValue = 0.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring-alpha"
    )

    if (showDialog) {
        ChallengeDialog(
            challengeText = challengeText,
            pokemonUrl = pokemonUrl,
            onClose = {
                showDialog = false
                onResumeBackground()
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HomeBackground(context = context, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(34.dp))

            HomeToolbar(
                isAudioOn = isAudioOn,
                onRate = onRate,
                onToggleAudio = onToggleAudio,
                onInstructions = onInstructions,
                onChallenges = onChallenges,
                onShare = onShare
            )

            Spacer(modifier = Modifier.height(56.dp))

            Box(
                modifier = Modifier
                    .size(364.dp)
                    .offset(y = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = bottlePainter(context),
                    contentDescription = "Botella",
                    modifier = Modifier
                        .size(332.dp)
                        .rotate(-28f + rotation.value)
                )

                if (countdown != null) {
                    Text(
                        text = countdown.toString(),
                        color = Color(0xFFFF8A00),
                        fontWeight = FontWeight.Bold,
                        fontSize = 54.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(y = (-8).dp)
                            .scale(countdownScale.value)
                            .alpha(countdownAlpha.value)
                    )
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            if (!isSpinning && countdown == null) {
                PressMePulseButton(
                    enabled = true,
                    blinkAlpha = blinkAlpha,
                    ringScale = ringScale,
                    ringAlpha = ringAlpha,
                    onClick = {
                        isSpinning = true
                        countdown = null
                        onPauseBackground()
                        val spinDuration = 3_500L
                        onPlaySpinSound(spinDuration)
                        scope.launch {
                            val target = SpinUtils.nextTargetRotation(rotation.value)
                            rotation.animateTo(
                                targetValue = target,
                                animationSpec = tween(
                                    durationMillis = spinDuration.toInt(),
                                    easing = LinearEasing
                                )
                            )
                            countdown = 3
                            while (countdown != null && countdown!! >= 0) {
                                delay(800)
                                val next = countdown!! - 1
                                countdown = if (next >= 0) next else null
                            }
                            challengeText = loadRandomChallenge()
                            pokemonUrl = loadRandomPokemonUrl()
                            showDialog = true
                            isSpinning = false
                        }
                    }
                )
            } else {
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@Composable
private fun PressMePulseButton(
    enabled: Boolean,
    blinkAlpha: Float,
    ringScale: Float,
    ringAlpha: Float,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .alpha(if (enabled) 1f else 0.72f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size((84 * ringScale).dp)
                    .background(Color(0x66FF5A00).copy(alpha = ringAlpha), CircleShape)
            )

            Box(
                modifier = Modifier
                    .size((58 * ringScale).dp)
                    .background(Color(0x99FF5A00).copy(alpha = (ringAlpha + 0.08f).coerceAtMost(0.95f)), CircleShape)
            )

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color(0xFFFF4D00), CircleShape)
                    .clickable(enabled = enabled) { onClick() }
            )
        }

        Text(
            text = "Presióname",
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            modifier = Modifier.alpha(blinkAlpha)
        )
    }
}

@Composable
private fun HomeBackground(
    context: android.content.Context,
    modifier: Modifier = Modifier
) {
    val drawableId = rememberDrawableId(context, "fondo_madera", R.drawable.ic_launcher_background)
    if (drawableId == R.drawable.ic_launcher_background) {
        WoodBackground(modifier = modifier)
    } else {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "Fondo de madera",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun bottlePainter(context: android.content.Context) =
    painterResource(id = rememberDrawableId(context, "botella_verde", R.drawable.ic_bottle))

private fun rememberDrawableId(
    context: android.content.Context,
    name: String,
    fallback: Int
): Int {
    val drawableId = context.resources.getIdentifier(name, "drawable", context.packageName)
    return if (drawableId != 0) drawableId else fallback
}

@Composable
private fun WoodBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.background(Color(0xFF8A6543))) {
        val planks = 5
        val plankWidth = size.width / planks
        val baseColors = listOf(
            Color(0xFF8C6A46),
            Color(0xFF7B5B3A),
            Color(0xFF93714A),
            Color(0xFF785338),
            Color(0xFF8F6945)
        )

        for (i in 0 until planks) {
            val left = plankWidth * i
            drawRect(
                color = baseColors[i % baseColors.size],
                topLeft = Offset(left, 0f),
                size = androidx.compose.ui.geometry.Size(plankWidth + 1f, size.height)
            )

            val grainYStep = size.height / 14f
            for (g in 0..14) {
                val y = grainYStep * g + (i * 7f)
                drawLine(
                    color = Color(0x22FFFFFF),
                    start = Offset(left + 8f, y),
                    end = Offset(left + plankWidth - 8f, y + (if (g % 2 == 0) 2f else -2f)),
                    strokeWidth = 2f
                )
            }

            if (i < planks - 1) {
                drawLine(
                    color = Color(0x55000000),
                    start = Offset(left + plankWidth, 0f),
                    end = Offset(left + plankWidth, size.height),
                    strokeWidth = 4f
                )
            }
        }

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color(0x44000000))
            )
        )
    }
}

@Composable
private fun ChallengeDialog(
    challengeText: String,
    pokemonUrl: String?,
    onClose: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnClickOutside = false, usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            // Caja principal del diálogo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 46.dp, bottom = 30.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xEE1A1A1A), Color(0xEE0D0D0D))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(1.5.dp, Color.White, RoundedCornerShape(20.dp))
                    .padding(start = 20.dp, end = 20.dp, top = 56.dp, bottom = 24.dp)
            ) {
                Text(
                    text = challengeText,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Círculo con Pokémon sobresaliendo por arriba
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .align(Alignment.TopCenter)
                    .background(Color.Black, CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (pokemonUrl != null) {
                    AsyncImage(
                        model = pokemonUrl,
                        contentDescription = "Pokemon",
                        modifier = Modifier
                            .size(76.dp)
                            .padding(4.dp)
                    )
                } else {
                    Text(text = "?", color = Color.White, fontSize = 24.sp)
                }
            }

            // Botón Cerrar sobresaliendo por abajo
            Button(
                onClick = onClose,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
            ) {
                Text(
                    text = "Cerrar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}


