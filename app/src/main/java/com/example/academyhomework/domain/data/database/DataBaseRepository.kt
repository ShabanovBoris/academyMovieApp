package com.example.academyhomework.domain.data.database

import android.content.Context
import android.util.Log
import com.example.academyhomework.model.Genre
import com.example.academyhomework.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface DbRepository{
    suspend fun getMovieList():List<Movie>
    suspend fun insertMovies(movies: List<Movie>)
}

class DataBaseRepository(applicationContext:Context): DbRepository {


   private val dataBase = DataBaseMovie.create(applicationContext)

    override suspend fun getMovieList(): List<Movie> = withContext(Dispatchers.IO){

        dataBase.movieDao.getAll().map { Log.d("DbRepository", "insertMovie: $it")
            toMovieModel(it) }
    }

    override suspend fun insertMovies(movies: List<Movie>) = withContext(Dispatchers.IO){

        dataBase.movieDao.insert(movies = movies.map { toEntity(it) })

    }

    private fun toEntity(movie: Movie): MovieEntity = MovieEntity(
        id = movie.id.toLong(),
        title = movie.title,
        genres = movie.genres.joinToString(",") { it.name },
        rating = movie.rating,
        imageUrl = movie.imageUrl,
        releaseDate = movie.releaseDate
    )

    private fun toMovieModel(entity:MovieEntity):Movie = Movie(
        id = entity.id.toInt(),
        title = entity.title,
        genres = entity.genres.split(",").map { Genre(id = 0, name = it) },
        imageUrl = entity.imageUrl,
        releaseDate = entity.releaseDate,
        rating = entity.rating
    )
}