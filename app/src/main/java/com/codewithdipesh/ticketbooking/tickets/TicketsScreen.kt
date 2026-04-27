package com.codewithdipesh.ticketbooking.tickets

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.codewithdipesh.ticketbooking.R
import com.codewithdipesh.ticketbooking.booking.components.DayItem
import com.codewithdipesh.ticketbooking.home.elements.sharedElementIf
import com.codewithdipesh.ticketbooking.model.Movie
import com.codewithdipesh.ticketbooking.tickets.components.CutCornerCircleShape
import com.codewithdipesh.ticketbooking.tickets.components.DetailCard
import com.codewithdipesh.ticketbooking.tickets.components.TicketCard
import kotlinx.coroutines.delay

@Composable
fun TicketsScreen(
    onBack: () -> Unit,
    movie: Movie,
    seats : List<Pair<Int,Int>>,
    day : DayItem,
    time : String,
    modifier: Modifier = Modifier
) {
    var showDetails by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    BackHandler {
        if(showDetails){
            onBack()
        }
    }

    LaunchedEffect(Unit) {
        if (!showDetails) {
            Toast.makeText(context, "Generating your ticket", Toast.LENGTH_LONG).show()
        }
        delay(4800)
        showDetails = true
    }

    Scaffold(
        topBar = {
            if(showDetails){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color(0xFF101010)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(
                        onClick = {
                            onBack()
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Back",
                            tint = Color.White.copy(0.5f)
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFF101010)
    ){ innerPadding ->

        Box(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
        ){
            TicketCard(
                pfpUrl = movie.posterUrl,
                modifier = Modifier.align(Alignment.Center)
            )
            AnimatedVisibility(
                visible = showDetails,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = fadeIn(tween(400)).plus(
                    slideInVertically(
                        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                        initialOffsetY = { it }
                    )
                )
            ) {
                DetailCard(
                    name = movie.title,
                    row = seats.first().first,
                    dateTime = day.fullLabel + " at $time",
                    seatString = "${seats.first().second} - ${seats.last().second}"
                )
            }
        }

    }
}


