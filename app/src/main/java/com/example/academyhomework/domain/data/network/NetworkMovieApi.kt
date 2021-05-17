package com.example.academyhomework.domain.data.network


import com.example.academyhomework.di.scopes.AppScope
import com.example.academyhomework.domain.data.network.models.*
import kotlinx.coroutines.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 	[TheMovieDb] API
 *
 *
 */


class NetworkMovieApi @Inject constructor(private val movieApi: TheMovieApi ) {

    suspend fun getMovieDetail(movieId: String) = withContext(Dispatchers.IO) {
        return@withContext movieApi.getDetails(movieId = movieId)
    }


    suspend fun getActors(movieId: String) = withContext(Dispatchers.IO) {
        return@withContext movieApi.getCredits(movieId = movieId)
    }

    suspend fun getGenresList() = withContext(Dispatchers.IO) {
        return@withContext movieApi.getGenres()
    }


    suspend fun getMovieResponse(page: Int = 1) = withContext(Dispatchers.IO) {
        return@withContext movieApi.getOnPlayingMovies(page)
    }

    //search method
    suspend fun getMovieBySearch(query: String) = withContext(Dispatchers.IO) {
        return@withContext movieApi.getMovieBySearch(query)
    }

     interface TheMovieApi {
        @GET("configuration?")
        suspend fun getImagesConfigurationInfo(): ConfigurationInfoClass

        @GET("movie/now_playing?")
        suspend fun getOnPlayingMovies(
            @Query("page") page: Int
        ): ResponseClass

        @GET("genre/movie/list?")
        suspend fun getGenres(): ResponseGenreClass

        @GET("movie/{movie_id}?")
        suspend fun getDetails(
            @Path("movie_id") movieId: String
        ): JsonMovieDetails

        @GET("movie/{movie_id}/credits?")
        suspend fun getCredits(
            @Path("movie_id") movieId: String
        ): CreditResponse

        //search
         @GET("search/movie?")
         suspend fun getMovieBySearch(
             @Query("query") searchQuery: String
         ): ResponseClass

    }

}



