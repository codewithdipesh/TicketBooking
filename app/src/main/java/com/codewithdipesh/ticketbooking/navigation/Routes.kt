package com.codewithdipesh.ticketbooking.navigation

import com.codewithdipesh.ticketbooking.model.Movie
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

@Serializable
data class DetailsRoute(val eventId: String)

@Serializable
data class BookingRoute(val movieId: String)

@Serializable
data object TicketsRoute
