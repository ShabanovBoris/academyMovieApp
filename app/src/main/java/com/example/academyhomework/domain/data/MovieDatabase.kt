package com.example.academyhomework.domain.data

import com.example.academyhomework.entities.Actor
import com.example.academyhomework.entities.Movie
import com.example.academyhomework.entities.MovieDetails
/** functions on dispatcher IO*/
interface MovieDatabase {
    suspend fun getMovieList(): List<Movie>
    suspend fun insertMovies(movies: List<Movie>)
    suspend fun clearMovies()
    suspend fun getMovieDetails(id: Int): MovieDetails?
    suspend fun insertMovieDetails(movieDetails: MovieDetails)
    suspend fun getActorById(id: Int): Actor?
    suspend fun insertActors(actors: List<Actor>)
}