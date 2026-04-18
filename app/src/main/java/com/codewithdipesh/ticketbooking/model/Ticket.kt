package com.codewithdipesh.ticketbooking.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Ticket(
    val id: String,
    val movieId: String,
    val movieTitle: String,
    val posterUrl: String,
    val showTime: LocalTime,
    val showDate: LocalDate,
    val seats: List<String>,
    val row : Int,
    val screenNumber : Int,
    val seatCount : Int,
    val price : Float,
    val bookedAt: LocalDateTime,
)