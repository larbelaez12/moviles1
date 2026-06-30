package com.example.myapplication.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun PressableIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.75f else 1f,
        animationSpec = tween(durationMillis = 80),
        label = "press-scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (pressed) 0.55f else 1f,
        animationSpec = tween(durationMillis = 80),
        label = "press-alpha"
    )

    Box(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .alpha(alpha)
            .clickable(interactionSource = interactionSource, indication = null) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}



