package com.codewithdipesh.ticketbooking.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.codewithdipesh.ticketbooking.booking.BookingScreen
import com.codewithdipesh.ticketbooking.home.HomeScreen
import com.codewithdipesh.ticketbooking.model.movies

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
                    onBookingConfirmed = {},
                    onBack = { navController.popBackStack() }
                )
            }

        }
    }
}
