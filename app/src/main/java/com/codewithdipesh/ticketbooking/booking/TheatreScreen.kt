package com.codewithdipesh.ticketbooking.booking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.ticketbooking.R
import com.codewithdipesh.ticketbooking.booking.components.CheckoutBottomSheet
import com.codewithdipesh.ticketbooking.booking.components.DayItem
import com.codewithdipesh.ticketbooking.booking.components.SeatHall
import com.codewithdipesh.ticketbooking.booking.seatBooking.OpenGLCinemaView
import com.codewithdipesh.ticketbooking.home.elements.customClickable
import com.codewithdipesh.ticketbooking.model.Movie
import com.codewithdipesh.ticketbooking.model.movies
import kotlinx.coroutines.delay
import kotlin.Int
import kotlin.Pair


private const val SAMPLE_TRAILER_URL =
    "https://res.cloudinary.com/daw9ly1fj/video/upload/v1776793845/the_bad_guys_trailor_fkm4iv.mp4"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheatreScreen(
    modifier: Modifier = Modifier,
    seats: Int,
    movie: Movie,
    day: DayItem,
    time: String,
    videoUri: String = SAMPLE_TRAILER_URL,
    onContinue : (List<Pair<Int, Int>>) -> Unit,
    onBack : () -> Unit
) {
    var showCheckout by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedSeats by remember { mutableStateOf<List<Pair<Int, Int>>>(emptyList()) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Black,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.Black),
                contentAlignment = Alignment.CenterStart
            ){
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back_arrow),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    ){
        if (showCheckout) {
            CheckoutBottomSheet(
                movie = movie,
                dateTime = day.fullLabel + " at $time",
                seats = selectedSeats,
                onDismiss = { showCheckout = false },
                onContinue = { onContinue(selectedSeats) },
                sheetState = sheetState
            )
        }

        Column(
            modifier = modifier.fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Where to sit?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    text = "Select seats",
                    color = Color.White.copy(0.5f),
                    fontSize = 12.sp
                )
                OpenGLCinemaView(videoUri = videoUri)
                SeatHall(
                    seatCount = seats,
                    onSelectionChange = { picked -> selectedSeats = picked }
                )
                Spacer(Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Box(
                        modifier = Modifier.wrapContentSize()
                            .clip(RoundedCornerShape(30.dp))
                            .background(Color.White.copy(0.1f)),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "$seats Seats" + if(seats > 1) "Together" else "",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                    Text(
                        text = "Showing where you can sit",
                        color = Color.White.copy(0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .customClickable(onClick = {
                        showCheckout = true
                    })
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFFF5C800)),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Continue",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
