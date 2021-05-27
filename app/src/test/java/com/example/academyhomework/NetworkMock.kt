package com.example.academyhomework

import com.example.academyhomework.entities.Movie
import com.example.academyhomework.entities.MovieDetails
import com.example.academyhomework.models.MovieDetailsTest
import com.example.academyhomework.models.MovieTest

class NetworkMock {
    fun loadMovies(pages: IntRange): List<Movie> {
        val list = mutableListOf<Movie>()
        for (i in pages) {
            list.add(
                movieTestToMovie(MovieTest(i, i.toDouble()))
            )
        }
        return list
    }

    fun loadMovieDetails(id: Int): MovieDetails {
        // todo stub
        return MovieDetailsTest() as MovieDetails
    }

    fun search(query: String): List<Movie> {
        // todo stub
        return listOf()
    }


        fun movieTestToMovie(movie: MovieTest)=
            Movie(
                movie.id,
                0,
                "",
                listOf(),
                0,
                null,
                0.0,
                "",
                "",
                "",
                "",
                movie.popularity
            )

}