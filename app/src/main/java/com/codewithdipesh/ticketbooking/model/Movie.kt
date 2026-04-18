package com.codewithdipesh.ticketbooking.model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: String,
    val title: String,
    val titleUrl: String, // styled title png or whatever
    val posterUrl: String,
    val description: String,
    val genres: List<String>,
    val rating: Float,
    val durationMinutes: Int,
    val releaseYear: Int,
    val language: String,
    val ticketPrice : Float,
)

val movies = listOf(
    Movie(
        id = "1",
        title = "The Bad Guys",
        titleUrl = "https://res.cloudinary.com/daw9ly1fj/image/upload/v1776432130/bad_guys_title_kseiih.png",
        posterUrl = "https://res.cloudinary.com/daw9ly1fj/image/upload/v1776432134/image_1_ohu2hy.png",
        description = "A group of notorious animal criminals attempt their most challenging con yet—becoming model citizens.",
        genres = listOf("Animation"),
        rating = 7.7f,
        durationMinutes = 96,
        releaseYear = 2025,
        language = "English",
        ticketPrice = 10.5f
    ),
    Movie(
        id = "2",
        title = "Zootopia",
        titleUrl = "https://res.cloudinary.com/daw9ly1fj/image/upload/v1776432125/zootopia_title_kl0rqp.png",
        posterUrl = "https://res.cloudinary.com/daw9ly1fj/image/upload/v1776432121/zootopia_poster_hr6r2r.png",
        description = "In a city of anthropomorphic animals, a rookie bunny cop and a cynical fox con artist uncover a conspiracy.",
        genres = listOf("Comedy"),
        rating = 8.0f,
        durationMinutes = 108,
        releaseYear = 2016,
        language = "English",
        ticketPrice = 11.0f
    ),
)