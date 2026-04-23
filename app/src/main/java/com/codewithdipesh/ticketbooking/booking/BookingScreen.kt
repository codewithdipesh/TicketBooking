package com.codewithdipesh.ticketbooking.booking

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.codewithdipesh.ticketbooking.R
import com.codewithdipesh.ticketbooking.booking.components.AmountChooser
import com.codewithdipesh.ticketbooking.booking.components.WhenToWatchScreen
import com.codewithdipesh.ticketbooking.model.Movie

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    movie : Movie,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onPreBookingConfirmed: (seats: Int, day: Int, time: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    var subScreenNumber by rememberSaveable { mutableStateOf(1) }
    var selectedDay by rememberSaveable { mutableStateOf(0) }
    var selectedTime by rememberSaveable { mutableStateOf("") }

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
                    onClick = {
                        if(subScreenNumber > 1) subScreenNumber--
                        else onBack()
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    if(subScreenNumber > 1){
                        Icon(
                            painter = painterResource(R.drawable.back_arrow),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }else{
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
        ){
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.TopCenter)
            ) {
                //Movie Header Row

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    with(sharedTransitionScope) {
                        Image(
                            painter = rememberAsyncImagePainter(movie.posterUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .sharedElement(
                                    rememberSharedContentState(key = "movie-poster-${movie.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .size(width = 140.dp, height = 100.dp)
                                .clip(RoundedCornerShape(22.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    // Movie Details
                    Column(verticalArrangement = Arrangement.Center){
                        with(sharedTransitionScope){
                            Image(
                                painter = rememberAsyncImagePainter(movie.titleUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .sharedElement(
                                        rememberSharedContentState(key = "movie-title-${movie.id}"),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    ),
                                contentScale = ContentScale.FillHeight,
                                alpha = 0.7f
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${movie.releaseYear} · ${movie.genres.first()} · ${movie.durationMinutes} min",
                            color = Color.White.copy(0.7f),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // IMDb Rating Badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.imdb),
                                contentDescription = null,
                                modifier = Modifier.height(16.dp)
                            )

                            Text(
                                text = "${movie.rating}",
                                color = Color.White.copy(0.7f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = Color.White.copy(0.3f),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Slide between WhenToWatch, AmountChooser and Theatre
                AnimatedContent(
                    targetState = subScreenNumber,
                    transitionSpec = {
                        when {
                            targetState > initialState -> {
                                // Forward (horizontal)
                                slideInHorizontally { it } + fadeIn() togetherWith
                                        slideOutHorizontally { -it } + fadeOut()
                            }
                            else -> {
                                // Back (horizontal)
                                slideInHorizontally { -it } + fadeIn() togetherWith
                                        slideOutHorizontally { it } + fadeOut()
                            }
                        }
                    },
                    modifier = modifier,
                    label = "bookingFlow"
                ) { screen ->
                    when (screen) {
                        1 -> WhenToWatchScreen(
                            onContinue = { day, time ->
                                selectedDay = day
                                selectedTime = time
                                subScreenNumber = 2
                            }
                        )
                        2 -> AmountChooser(
                            onContinue = { seats ->
                                onPreBookingConfirmed(seats, selectedDay, selectedTime)
                            }
                        )
                    }
                }
            }
        }
    }
}