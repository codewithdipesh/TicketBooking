package com.codewithdipesh.ticketbooking.booking.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TicketCounter(
    amount: Int,
    onAmountChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    min: Int = 1,
    max: Int = 3
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { if (amount > min) onAmountChange(amount - 1) },
            modifier = Modifier
                .size(52.dp)
                .background(
                    color = Color(0xFF2A2A2A),
                    shape = CircleShape
                )
        ) {
            Text(
                text = "−",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.width(32.dp))


        AnimatedNumber(
            value = amount,
            modifier = Modifier.width(48.dp)
        )

        Spacer(Modifier.width(32.dp))


        IconButton(
            onClick = { if (amount < max) onAmountChange(amount + 1) },
            modifier = Modifier
                .size(52.dp)
                .background(
                    color = Color(0xFF2A2A2A),
                    shape = CircleShape
                )
        ) {
            Text(
                text = "+",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AnimatedNumber(
    value: Int,
    modifier: Modifier = Modifier
) {
    //previous value to determine scroll direction
    var previousValue by remember { mutableIntStateOf(value) }
    val scrollUp = value > previousValue   // going up → new number scrolls in from below


    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(value) {
        if (value != previousValue) {
            offsetY.snapTo(if (scrollUp) 52f else -52f)
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 220,
                    easing = FastOutSlowInEasing
                )
            )
            previousValue = value
        }
    }

    Box(
        modifier = modifier.height(52.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outgoing number =
        Text(
            text = previousValue.toString(),
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .graphicsLayer {
                    translationY = if (scrollUp) offsetY.value - 52f else offsetY.value + 52f
                    alpha = 1f - (kotlin.math.abs(
                        if (scrollUp) offsetY.value - 52f else offsetY.value + 52f
                    ) / 52f).coerceIn(0f, 1f)
                }
        )
        // Incoming number (scrolls in)
        Text(
            text = value.toString(),
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .graphicsLayer {
                    translationY = offsetY.value
                    alpha = 1f - (kotlin.math.abs(offsetY.value) / 52f).coerceIn(0f, 1f)
                }
        )
    }
}