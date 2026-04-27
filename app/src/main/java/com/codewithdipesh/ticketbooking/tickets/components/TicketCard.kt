package com.codewithdipesh.ticketbooking.tickets.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.codewithdipesh.ticketbooking.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private fun isBackFacing(angleDeg: Float): Boolean {
    val n = ((angleDeg % 360f) + 360f) % 360f
    return n in 90f..270f
}

@Composable
fun TicketCard(
    pfpUrl: String,
    modifier: Modifier = Modifier
) {
    var isLoading by rememberSaveable { mutableStateOf(true) }
    val radiusPx = with(LocalDensity.current) { 16.dp.toPx() }

    val scale = remember { Animatable(1f) }
    val offsetY = remember { Animatable(0f) }
    val rotationY = remember { Animatable(0f) } // for the spin
    val rotationX = remember { Animatable(0f) } // small back-lean for 3D depth
    val rotationZ = remember { Animatable(0f) } // the actual left-lean (hypotenuse look)

    LaunchedEffect(Unit) {
        delay(3000)
        isLoading = false

        // 1) Pop
        coroutineScope {
           launch {
               scale.animateTo(1.2f, tween(350, easing = FastOutSlowInEasing))

           }
        }
        // 2) Keep your existing tilt (NO CHANGE)
        coroutineScope {
            launch {
                rotationZ.animateTo(-20f, tween(400, easing = FastOutSlowInEasing))
            }
            launch {
                rotationX.animateTo(10f, tween(400, easing = FastOutSlowInEasing))
            }
            launch {
                rotationY.animateTo(40f, tween(400, easing = FastOutSlowInEasing))
            }
        }

        // 3) FULL SPIN (from current angle, no snap)

        coroutineScope {
            launch {
                rotationY.animateTo(
                    targetValue = 360f,
                    animationSpec = tween(
                        durationMillis = 900, // faster, smooth
                        easing = LinearEasing // IMPORTANT
                    )
                )
            }
            launch {
                rotationZ.animateTo(0f, tween(1000, easing = EaseOutCubic))
            }
            launch {
                rotationX.animateTo(0f, tween(1000, easing = EaseOutCubic))
            }
        }

        // 4) Settle back
        coroutineScope {
            launch {
                scale.animateTo(1f, tween(800, easing = EaseOutCubic))
            }
            launch {
                offsetY.animateTo(-150f,tween(durationMillis = 800 , easing = FastOutSlowInEasing))
            }
        }
    }

    Column(
        modifier = modifier
            .width(230.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                this.rotationY = rotationY.value
                this.rotationX = rotationX.value
                this.rotationZ = rotationZ.value
                translationY = offsetY.value
                cameraDistance = 12f * density
                transformOrigin = TransformOrigin.Center
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(440.dp)
                .clip(CutCornerCircleShape(radiusPx))
                .background(Color(0xFF1E1E1E), CutCornerCircleShape(radiusPx))
                .dottedBackground(),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(
                targetState = isLoading,
                animationSpec = tween(300),
                modifier = Modifier.graphicsLayer {
                    alpha = if (isBackFacing(rotationY.value)) 0f else 1f
                },
                label = "imageReveal"
            ) { loading ->
                if (loading) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .shimmer())
                } else {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1E1E1E)),
                        contentAlignment = Alignment.Center
                    ){
                        AsyncImage(
                            model = pfpUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(CutCornerCircleShape(radiusPx))
                .background(Color(0xFF1E1E1E))
                .dottedBackground(),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(
                targetState = isLoading,
                animationSpec = tween(300),
                modifier = Modifier.graphicsLayer {
                    alpha = if (isBackFacing(rotationY.value)) 0f else 1f
                },
                label = "barcodeReveal"
            ) { loading ->
                if (loading) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .shimmer())
                } else {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1E1E1E)),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            painter = painterResource(R.drawable.barcode),
                            contentDescription = null,
                            tint = Color(0xFF575757),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        )
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketPreview(modifier: Modifier = Modifier) {
    val radiusPx = with(LocalDensity.current) { 16.dp.toPx() }

    val scale = remember { Animatable(1f) }
    val rotationY = remember { Animatable(360f) } // for the spin
    val rotationX = remember { Animatable(10f) } // small back-lean for 3D depth
    val rotationZ = remember { Animatable(-20f) } // the actual left-lean (hypotenuse look)


    Box(
        modifier = Modifier
            .size(1000.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = modifier
                .width(230.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    this.rotationY = rotationY.value
                    this.rotationX = rotationX.value
                    this.rotationZ = rotationZ.value
                    cameraDistance = 8f * density
                    transformOrigin = TransformOrigin.Center
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(440.dp)
                    .clip(CutCornerCircleShape(radiusPx))
                    .background(Color(0xFF1E1E1E), CutCornerCircleShape(radiusPx)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(CutCornerCircleShape(radiusPx))
                    .background(Color(0xFF1E1E1E)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmer()
                )
            }
        }
    }
}
