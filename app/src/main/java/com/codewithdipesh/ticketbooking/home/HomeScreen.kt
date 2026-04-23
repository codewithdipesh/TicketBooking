package com.codewithdipesh.ticketbooking.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.codewithdipesh.ticketbooking.R
import com.codewithdipesh.ticketbooking.home.elements.customClickable
import com.codewithdipesh.ticketbooking.home.elements.sharedElementIf
import com.codewithdipesh.ticketbooking.model.Movie
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    movies : List<Movie>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope : AnimatedVisibilityScope,
    onMovieClicked: (Movie) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(0,pageCount = { movies.size })

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.Black)
                    .padding(horizontal = 26.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Now Showing",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 23.sp
                )

                Image(
                    painter = painterResource(R.drawable.pfp),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 26.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Icon(
                    painter = painterResource(R.drawable.home),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(Modifier.width(64.dp))
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White.copy(0.7f)
                )
                Spacer(Modifier.width(64.dp))
                Icon(
                    painter = painterResource(R.drawable.tickets),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White.copy(0.7f)
                )
            }
        },
        contentWindowInsets = WindowInsets(0)
    ){ innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            HorizontalPager(
                state = pagerState,
                pageSpacing = 16.dp,
                key = { movies[it].id },
                beyondViewportPageCount = 1,
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight()
            ) { page ->
                val isCurrentPage = page == pagerState.settledPage
                Box {
                    Box(
                        modifier = Modifier.wrapContentSize()
                    ) {
                        AsyncImage(
                            model = movies[page].posterUrl,
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .sharedElementIf(
                                    enabled = isCurrentPage,
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    key = "movie-poster-${movies[page].id}"
                                )
                                .clip(RoundedCornerShape(30.dp))
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.7f),
                                            Color.Black
                                        ),
                                        startY = 600f // tweak this based on your image height
                                    )
                                )
                        )
                    }
                    AsyncImage(
                        model = movies[page].titleUrl,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .height(100.dp)
                            .sharedElementIf(
                                enabled = isCurrentPage,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope,
                                key = "movie-title-${movies[page].id}"
                            )
                    )

                }
            }
            //release date and rating
            val currentMovie = movies[pagerState.currentPage]
            Spacer(Modifier.height(32.dp))
            Text(
                text = "${currentMovie.releaseYear} · ${currentMovie.genres.first()} · ${currentMovie.durationMinutes} min",
                color = Color.White.copy(0.7f),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.imdb),
                    contentDescription = null,
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = "${currentMovie.rating}",
                    color = Color.White.copy(0.7f),
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(60.dp))

            //buy tickets button
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                Box(
                    modifier = Modifier
                        .customClickable { onMovieClicked(currentMovie) }
                        .height(50.dp)
                        .background(Color(0xFFF5C518),RoundedCornerShape(30.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Buy Tickets",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .customClickable({})
                        .size(50.dp)
                        .background(Color.DarkGray, CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = Color.White
                    )
                }
            }
        }

    }

}
