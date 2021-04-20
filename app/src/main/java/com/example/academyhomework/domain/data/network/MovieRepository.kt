package com.example.academyhomework.domain.data.network


import com.example.academyhomework.model.Actor
import com.example.academyhomework.model.Genre
import com.example.academyhomework.model.Movie
import com.example.academyhomework.model.MovieDetails
import kotlinx.coroutines.*

interface MovieRepository {
    suspend fun loadMovies(pages: IntRange): List<Movie>
    suspend fun loadMovieDetails(id: Int): MovieDetails
}

class JsonMovieRepository() : MovieRepository {
    /** load list of [Genres]
     *
     * */
    private suspend fun genresList(): List<JsonGenre> = NetworkModule().getGenresList().genres
    private suspend fun loadGenres(): MutableMap<Int, String> {
        val genres = genresList()
        val mutableMap: MutableMap<Int, String> = mutableMapOf()
        for (item in genres) {
            mutableMap.put(item.id, item.name)
        }
        return mutableMap
    }

    /**  get [JsonMovieList]  from Network
     *          max 10 pages
     * */
    private suspend fun getJsonMovieList(pages: IntRange): List<JsonMovie> {
        return coroutineScope {
            var range = pages
            val module = NetworkModule()
            val listOfJsonMovie = mutableListOf<JsonMovie>()
            val totalPages = module.getMovieResponse().totalPages

            if (range.first > totalPages) {
                return@coroutineScope emptyList<JsonMovie>()
            }
            if (range.last > totalPages) {
                range = range.first..totalPages
            }

            for (iterator in range) {
                listOfJsonMovie.addAll(module.getMovieResponse(iterator).results)
            }
            return@coroutineScope listOfJsonMovie
        }
    }

    /** function [loadMovies] return list of [Movie] class to [ViewModel]
     *
     * */
    override suspend fun loadMovies(pages: IntRange): List<Movie> {
        val jsonMovie: List<JsonMovie?> = getJsonMovieList(pages).orEmpty()
        val mutableMap = loadGenres()
        return jsonMovie.map {
            Movie(
                id = it!!.id,
                title = it.title ?: "",
                genres = it.genreIds.map { id -> Genre(id = id, mutableMap[id] ?: "") },
                reviewCount = it.voteCount ?: 0,
                rating = (it.voteAverage ?: 0.0) / 2,
                imageUrl = NetworkModule.baseImagePosterUrl + it.posterPath ?: "",
                detailImageUrl = NetworkModule.baseImageBackdropUrl + it.backdropPath ?: "",
                storyLine = it.overview ?: "",
                releaseDate = it.releaseDate,
                popularity = it.popularity
            )
        }
    }

    /** function [loadMovieDetails] return list of [MovieDetails] class to [ViewModel]
     *
     * */
    override suspend fun loadMovieDetails(id: Int): MovieDetails {
        val jsonDetails: JsonMovieDetails = NetworkModule().getMovieDetail("$id")
        return MovieDetails(
            id = jsonDetails.id,
            title = jsonDetails.original_title,
            overview = jsonDetails.overview ?: "Nothing",
            runtime = jsonDetails.runtime ?: 0,
            imageBackdrop = NetworkModule.baseImageBackdropUrl + jsonDetails.backdrop_path ?: "",
            genres = jsonDetails.genres.map { Genre(id = it.id, name = it.name) },
            actors = loadList(jsonDetails.id),
            votes = jsonDetails.vote_average / 2
        )
    }

    /** get [ActorsCast]  from Network
     *
     * */
    private suspend fun loadList(id: Int): List<Actor> {
        val jsonCast = NetworkModule().getActors("$id").cast
        return jsonCast.map {
            Actor(
                id = it.id,
                name = it.name,
                imageUrl = NetworkModule.baseImagePosterUrl + it.profilePicture
            )
        }
    }


}


