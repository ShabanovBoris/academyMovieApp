package com.example.academyhomework.domain.data.network


import android.util.Log
import com.example.academyhomework.model.Actor
import com.example.academyhomework.model.Genre
import com.example.academyhomework.model.Movie
import com.example.academyhomework.model.MovieDetails
import kotlinx.coroutines.*

interface MovieRepository {
    suspend fun loadMovies(): List<Movie>
    suspend fun loadMovieDetails(id:Int): MovieDetails
}

internal class JsonMovieRepository() : MovieRepository {
    /** init coroutine*/
    // region coriutine init
    var coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Http", "Coroutine exception, scope active:${this.coroutineScope.isActive}", throwable)
        coroutineScope = CoroutineScope(Dispatchers.IO)
    }
    // endregion


    /** load list of [Genres]
     *
     * */
   private suspend fun genresList():List<JsonGenre> = NetworkModule().getGenresList().genres
   private suspend fun loadGenres():MutableMap<Int,String>{
       val genres = genresList()
       val mutableMap: MutableMap<Int,String> = mutableMapOf()
       for(item in genres){
           mutableMap.put(item.id,item.name)
       }
        return mutableMap
    }

    /**  get [JsonMovieList]  from Network
     *          max 10 pages
     * */
    private suspend fun getJsonMovieList(): List<JsonMovie>{
       return coroutineScope {
            val module = NetworkModule()
            var totalPages = module.getMovieResponse().totalPages
            if(totalPages>10)totalPages=10
            var listOfJsonMovie = mutableListOf<JsonMovie>()
            for (iterator in 1..totalPages) {
                listOfJsonMovie.addAll(module.getMovieResponse(iterator).results)
            }
            return@coroutineScope listOfJsonMovie
        }
    }

    /** function [loadMovies] return list of [Movie] class to [ViewModel]
     *
     * */
    override suspend fun loadMovies(): List<Movie> {
        val jsonMovie : List<JsonMovie?> = getJsonMovieList().orEmpty()
        val mutableMap = loadGenres()
        return jsonMovie.map{
                Movie(
                    id = it!!.id,
                    title = it.title ?:"",
                    genres = it.genreIds.map {id -> Genre(id = id,mutableMap[id]?:"") },
                    reviewCount = it.voteCount?:0,
                    rating = (it.voteAverage?:0.0)/2,
                    imageUrl = NetworkModule.baseImagePosterUrl + it.posterPath?:"",
                    detailImageUrl = NetworkModule.baseImageBackdropUrl + it.backdropPath?:"",
                    storyLine = it.overview?:"",
                    releaseDate = it.releaseDate
                )
        }
    }

    /** function [loadMovieDetails] return list of [MovieDetails] class to [ViewModel]
     *
     * */
    override suspend fun loadMovieDetails(id:Int): MovieDetails {
        val jsonDetails : JsonMovieDetails = NetworkModule().getMovieDetail("$id")
        return MovieDetails(
            title = jsonDetails.original_title,
            overview = jsonDetails.overview?:"Nothing",
            runtime = jsonDetails.runtime?:0,
            imageBackdrop = NetworkModule.baseImageBackdropUrl + jsonDetails.backdrop_path?:"",
            genres = jsonDetails.genres.map { Genre(id = it.id, name = it.name) },
            actors = loadList(jsonDetails.id),
            votes = jsonDetails.vote_average/2
        )
    }

    /** get [ActorsCast]  from Network
     *
     * */
    private suspend fun loadList(id:Int): List<Actor> {
        val jsonCast = NetworkModule().getActors("$id").cast
        return jsonCast.map { Actor(
            id = it.id,
            name = it.name,
            imageUrl = NetworkModule.baseImagePosterUrl + it.profilePicture
        ) }
    }


}


