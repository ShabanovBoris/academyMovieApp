package com.example.academyhomework.domain.data

import com.example.academyhomework.entities.Movie
import com.example.academyhomework.entities.MovieDetails

interface MovieNetwork {
    suspend fun loadMovies(pages: IntRange): List<Movie>
    suspend fun loadMovieDetails(id: Int): MovieDetails
}