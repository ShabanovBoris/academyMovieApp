package com.example.academyhomework.domain.data.network


import com.example.academyhomework.domain.data.MovieNetwork
import com.example.academyhomework.domain.data.network.models.JsonGenre
import com.example.academyhomework.domain.data.network.models.JsonMovie
import com.example.academyhomework.domain.data.network.models.JsonMovieDetails
import com.example.academyhomework.entities.Actor
import com.example.academyhomework.entities.Genre
import com.example.academyhomework.entities.Movie
import com.example.academyhomework.entities.MovieDetails
import kotlinx.coroutines.*

class JsonMovieRepository() : MovieNetwork {

    private val mNetworkModule = NetworkModule()
    /** load list of [Genres]
     *
     * */
    private suspend fun genresList(): List<JsonGenre> = mNetworkModule.getGenresList().genres
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

            var range = pages
            val listOfJsonMovie = mutableListOf<JsonMovie>()
            val totalPages = mNetworkModule.getMovieResponse().totalPages

            if (range.first > totalPages) {
                return emptyList<JsonMovie>()
            }
            if (range.last > totalPages) {
                range = range.first..totalPages
            }

            for (iterator in range) {
                listOfJsonMovie.addAll(mNetworkModule.getMovieResponse(iterator).results)
            }
            return listOfJsonMovie
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
        val jsonDetails: JsonMovieDetails = mNetworkModule.getMovieDetail("$id")
        return MovieDetails(
            id = jsonDetails.id,
            title = jsonDetails.original_title,
            overview = jsonDetails.overview ?: "Nothing",
            runtime = jsonDetails.runtime ?: 0,
            imageBackdrop = NetworkModule.baseImageBackdropUrl + jsonDetails.backdrop_path ?: "",
            genres = jsonDetails.genres.map { Genre(id = it.id, name = it.name) },
            actors = loadActorList(jsonDetails.id),
            votes = jsonDetails.vote_average / 2
        )
    }

    /** get [ActorsCast]  from Network
     *
     * */
    private suspend fun loadActorList(id: Int): List<Actor> {
        val jsonCast = mNetworkModule.getActors("$id").cast
        return jsonCast.map {
            Actor(
                id = it.id,
                name = it.name,
                imageUrl = NetworkModule.baseImagePosterUrl + it.profilePicture
            )
        }
    }


}


