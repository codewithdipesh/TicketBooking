package com.codewithdipesh.ticketbooking.tickets.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dottedBackground(
    dotColor: Color = Color.White.copy(alpha = 0.18f),
    dotRadius: Dp = 1.dp,
    spacing: Dp = 14.dp
): Modifier = drawBehind {
    val radiusPx = dotRadius.toPx()
    val spacingPx = spacing.toPx()
    val cols = (size.width / spacingPx).toInt()
    val rows = (size.height / spacingPx).toInt()
    val offsetX = (size.width - cols * spacingPx) / 2f
    val offsetY = (size.height - rows * spacingPx) / 2f
    for (i in 0..cols) {
        for (j in 0..rows) {
            drawCircle(
                color = dotColor,
                radius = radiusPx,
                center = Offset(offsetX + i * spacingPx, offsetY + j * spacingPx)
            )
        }
    }
}