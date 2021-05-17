package com.example.academyhomework.domain.data

import com.example.academyhomework.entities.Movie
import com.example.academyhomework.entities.MovieDetails
import java.time.temporal.TemporalQuery

interface MovieNetwork {
    suspend fun loadMovies(pages: IntRange): List<Movie>
    suspend fun loadMovieDetails(id: Int): MovieDetails
    suspend fun search(query: String): List<Movie>
}