package com.codewithdipesh.ticketbooking.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.codewithdipesh.ticketbooking.booking.BookingScreen
import com.codewithdipesh.ticketbooking.booking.TheatreScreen
import com.codewithdipesh.ticketbooking.booking.components.DayItem
import com.codewithdipesh.ticketbooking.home.HomeScreen
import com.codewithdipesh.ticketbooking.model.movies
import com.codewithdipesh.ticketbooking.tickets.TicketsScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    SharedTransitionLayout(modifier = modifier) {
        NavHost(
            navController = navController,
            startDestination = HomeRoute
        ) {
            composable<HomeRoute> {
                HomeScreen(
                    movies = movies,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onMovieClicked = { movie ->
                        navController.navigate(BookingRoute(movie.id))
                    }
                )
            }

            composable<BookingRoute>{ backStackEntry ->
                val route = backStackEntry.toRoute<BookingRoute>()

                val movie = movies.first { it.id == route.movieId }

                BookingScreen(
                    movie = movie,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onPreBookingConfirmed = { seats, day, time ->
                        navController.navigate(
                            TheatreRoute(
                                movieId = movie.id,
                                seatNumber = seats,
                                dayNumber = day.number,
                                dayLabel = day.label,
                                dayFullLabel = day.fullLabel,
                                time = time
                            )
                        )
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable<TheatreRoute>{ backStackEntry ->
                val route = backStackEntry.toRoute<TheatreRoute>()

                val movie = movies.first { it.id == route.movieId }
                val day = DayItem(
                    number = route.dayNumber,
                    label = route.dayLabel,
                    fullLabel = route.dayFullLabel
                )

                TheatreScreen(
                    seats = route.seatNumber,
                    movie = movie,
                    day = day,
                    time = route.time,
                    onContinue = { selectedSeats ->
                        val seatsString = selectedSeats.joinToString(";") { "${it.first},${it.second}" }

                        navController.navigate(
                            TicketsRoute(
                                seatListString = seatsString,
                                movieId = route.movieId,
                                dayNumber = route.dayNumber,
                                dayLabel = route.dayLabel,
                                dayFullLabel = route.dayFullLabel,
                                time = route.time
                            )
                        )
                    },
                    onBack = { navController.navigateUp() }
                )
            }

            composable<TicketsRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<TicketsRoute>()

                val movie = movies.first { it.id == route.movieId }
                val time = route.time
                val day = DayItem(
                    number = route.dayNumber,
                    label = route.dayLabel,
                    fullLabel = route.dayFullLabel
                )

                val seats = route.seatListString.split(";").map {
                    val (row, col) = it.split(",")
                    row.toInt() to col.toInt()
                }

                TicketsScreen(
                    movie = movie,
                    seats = seats,
                    onBack = {
                        navController.navigateUp()
                    },
                    day = day,
                    time = time
                )

            }

        }
    }
}
