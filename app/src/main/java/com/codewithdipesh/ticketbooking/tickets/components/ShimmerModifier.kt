package com.codewithdipesh.ticketbooking.tickets.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.shimmer(
    highlight: Color = Color.White.copy(alpha = 0.10f),
    durationMillis: Int = 1200
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )

    drawWithCache {
        val travel = size.height * 2f
        val brush = Brush.linearGradient(
            colors = listOf(Color.Transparent, highlight, Color.Transparent),
            start = Offset(0f, -size.height + travel * progress),
            end = Offset(0f, travel * progress)
        )
        onDrawWithContent {
            drawContent()
            drawRect(brush = brush)
        }
    }
}
