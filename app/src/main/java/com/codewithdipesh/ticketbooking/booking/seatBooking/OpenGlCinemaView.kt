package com.codewithdipesh.ticketbooking.booking.seatBooking

import android.opengl.GLSurfaceView
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.codewithdipesh.ticketbooking.booking.seatBooking.gl.CinemaRenderer

@Composable
fun OpenGLCinemaView(
    videoUri: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val (glView, renderer) = remember(videoUri) {
        val view = GLSurfaceView(context)
        val r = CinemaRenderer(context, videoUri, requestRender = { view.requestRender() })
        view.setEGLContextClientVersion(2)
        view.setRenderer(r)
        view.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        view to r
    }

    var showCinema by remember { mutableStateOf(false) }
    LaunchedEffect(videoUri) {
        showCinema = true
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> glView.onResume()
                Lifecycle.Event.ON_PAUSE -> glView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            glView.queueEvent { renderer.release() }
        }
    }

    val animatedHeight by animateDpAsState(
        targetValue = if (showCinema) 220.dp else 0.dp,
        animationSpec = tween(durationMillis = 1500),
        label = "cinemaHeight"
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = if (showCinema) 1f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "cinemaAlpha"
    )

    Box(
        modifier = modifier
            .size(400.dp,220.dp),
        contentAlignment = Alignment.BottomCenter
    ){
        AndroidView(
            factory = { glView },
            modifier = modifier
                .width(400.dp)
                .height(animatedHeight)
                .alpha(animatedAlpha),
        )
    }
}