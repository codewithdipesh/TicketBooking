package com.codewithdipesh.ticketbooking.booking.seatBooking

import kotlin.collections.listOf


enum class SeatStatus {
    B,  //booked
    A,  //available ( not booked yet )
    S,  //selected by the user in this session
    NA //no seat
}


val seatArrangements = listOf(

    //1st
    listOf(
        SeatStatus.NA,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.NA,
    ),
    //2nd
    listOf(
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.B,
    ),
    //3rd
    listOf(
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.B,
        SeatStatus.B,
    ),
    //4th
    listOf(
        SeatStatus.B,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B
    ),
    //5th
    listOf(
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.B,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A
    ),
    //6th
    listOf(
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B
    ),
    //7th
    listOf(
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.B,
    ),
    //8th
    listOf(
        SeatStatus.B,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.A,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
        SeatStatus.B,
    )

)