package com.example.academyhomework.domain.data


import android.util.Log
import com.example.academyhomework.model.Genre
import com.example.academyhomework.model.Movie
import kotlinx.coroutines.*

interface MovieRepository {
    suspend fun loadMovies(): List<Movie>

}

internal class JsonMovieRepository() : MovieRepository {
    // region coriutine init
    var coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Http", "Coroutine exception, scope active:${this.coroutineScope.isActive}", throwable)
        coroutineScope = CoroutineScope(Dispatchers.IO)
    }
    // endregion


    suspend fun genresList():List<JsonGenre> = NetworkModule().getGenresList().genres
    suspend fun loadGenres():MutableMap<Int,String>{
       val genres = genresList()
       val mutableMap: MutableMap<Int,String> = mutableMapOf()
       for(item in genres){
           mutableMap.put(item.id,item.name)
       }
        return mutableMap
    }

    private suspend fun getJsonMovieList(): List<JsonMovie>{

       return coroutineScope {
            val module = NetworkModule()
            val totalPages = module.getMovieResponse().totalPages

            var listOfJsonMovie = mutableListOf<JsonMovie>()
            for (iterator in 1..totalPages) {

                listOfJsonMovie.addAll(module.getMovieResponse(iterator).results)
            }
            return@coroutineScope listOfJsonMovie
        }
    }

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
                    storyLine = it.overview?:""
                )
        }

    }




}


