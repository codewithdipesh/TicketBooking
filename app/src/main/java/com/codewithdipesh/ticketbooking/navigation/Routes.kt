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
data class TheatreRoute(
    val movieId: String,
    val seatNumber: Int,
    val dayNumber: Int,
    val dayLabel: String,
    val dayFullLabel: String,
    val time: String
)

@Serializable
data class TicketsRoute(
    val movieId: String,
    val seatListString: String,
    val dayNumber: Int,
    val dayLabel: String,
    val dayFullLabel: String,
    val time: String
)
