package com.example.academyhomework.domain.data.database

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Transaction
import com.example.academyhomework.model.Actor
import com.example.academyhomework.model.Genre
import com.example.academyhomework.model.Movie
import com.example.academyhomework.model.MovieDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface DbRepository {
    suspend fun getMovieList(): List<Movie>
    suspend fun insertMovies(movies: List<Movie>)
    suspend fun clearMovies()
    suspend fun getMovieDetails(id: Int): MovieDetails?
    suspend fun insertMovieDetails(movieDetails: MovieDetails)
    suspend fun getActorById(id: Int): Actor?
    suspend fun insertActors(actors: List<Actor>)
}

class DataBaseRepository(applicationContext: Context) : DbRepository {


    private val dataBase = DataBaseMovie.create(applicationContext)

    override suspend fun getMovieDetails(id: Int): MovieDetails? = withContext(Dispatchers.IO) {
        Log.d("AcademyHomework", "GETTING: getMovieDetails $id")
        val data = dataBase.movieDao.getDetailsById(id.toLong())

        data?.let {
            toMovieDetailsModel(data)
        }

    }

    override suspend fun insertMovieDetails(movieDetails: MovieDetails):Unit =
        withContext(Dispatchers.IO) {
            Log.d("AcademyHomework", "GETTING: insertMovieDetails ${movieDetails.id}")
            dataBase.movieDao.insertDetails(toEntityMovieDetails(movieDetails))
        }

    override suspend fun getActorById(id: Int): Actor? = withContext(Dispatchers.IO) {
        Log.d("AcademyHomework", "getActorById($id)")
        val data = dataBase.movieDao.getActorById(id.toLong())

        data?.let {
            toActor(data)
        }

    }

    @Transaction
    override suspend fun insertActors(actors: List<Actor>) = withContext(Dispatchers.IO) {
        Log.d("AcademyHomework", "insertActors $actors")
        dataBase.movieDao.insertActors(actors.map { toActorEntity(it) })
    }


    override suspend fun getMovieList(): List<Movie> = withContext(Dispatchers.IO) {
        Log.d("AcademyHomework", "GETTING: Movies")
        dataBase.movieDao.getAll().map { toMovieModel(it) }
    }

    @Transaction
    override suspend fun insertMovies(movies: List<Movie>) = withContext(Dispatchers.IO) {
        Log.d("AcademyHomework", "insertMovies: ${movies.size}")
        dataBase.movieDao.insert(movies = movies.map { toEntityMovie(it) })
    }

    override suspend fun clearMovies(): Unit = withContext(Dispatchers.IO) {
        dataBase.movieDao.clear()
        Log.d("AcademyHomework", "clearMovies: ----->")
        dataBase.movieDao.getAll().map { Log.d("DbRepository", "DeleteMovie: $it") }
    }


    private fun toEntityMovie(movie: Movie): MovieEntity = MovieEntity(
        id = movie.id.toLong(),
        title = movie.title,
        genres = movie.genres.joinToString(",") { it.name },
        rating = movie.rating,
        imageUrl = movie.imageUrl,
        releaseDate = movie.releaseDate
    )

    private fun toMovieModel(entity: MovieEntity): Movie = Movie(
        id = entity.id.toInt(),
        title = entity.title,
        genres = entity.genres.split(",").map { Genre(id = 0, name = it) },
        imageUrl = entity.imageUrl,
        releaseDate = entity.releaseDate,
        rating = entity.rating
    )

    private suspend fun toMovieDetailsModel(entity: MovieDetailsEntity): MovieDetails =
        MovieDetails(
            id = entity.id.toInt(),
            title = entity.title,
            overview = entity.overview,
            runtime = entity.runtime,
            imageBackdrop = entity.imageBackdrop,
            genres = entity.genres.split(",").map { Genre(id = 0, name = it) },
            actors = entity.actorsId.split(",").map { getActorById(it.toInt())!! },
            votes = entity.votes
        )

    private fun toEntityMovieDetails(movie: MovieDetails): MovieDetailsEntity = MovieDetailsEntity(
        id = movie.id.toLong(),
        title = movie.title,
        overview = movie.overview,
        runtime = movie.runtime,
        imageBackdrop = movie.imageBackdrop,
        genres = movie.genres.joinToString(",") { it.name },
        actorsId = movie.actors.joinToString(",") { it.id.toString() },
        votes = movie.votes
    )

    private fun toActor(entity: ActorEntity): Actor = Actor(
        id = entity.id,
        name = entity.name,
        imageUrl = entity.imageUrl
    )

    private fun toActorEntity(actor: Actor): ActorEntity = ActorEntity(
        id = actor.id,
        name = actor.name,
        imageUrl = actor.imageUrl
    )
}