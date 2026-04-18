package com.codewithdipesh.ticketbooking.home.elements

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.customClickable(
    onClick : () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val clicked by  interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if(clicked) 0.85f else 1f
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
}