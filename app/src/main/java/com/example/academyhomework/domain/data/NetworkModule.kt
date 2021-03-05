package com.example.academyhomework.domain.data

import com.example.academyhomework.domain.data.NetworkModule.RetrofitModule.json
import com.example.academyhomework.domain.data.NetworkModule.RetrofitModule.movieApi
import com.example.academyhomework.domain.data.NetworkModule.RetrofitModule.okHttpClient
import com.example.academyhomework.domain.data.NetworkModule.RetrofitModule.retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 	[TheMovieDb] API
 *
 *
 */


class NetworkModule {

    suspend fun getMovieDetail(movieId:String) = withContext(Dispatchers.IO){
        return@withContext RetrofitModule.movieApi.getDetails(movieId = movieId)
    }


    suspend fun getActors(movieId:String) = withContext(Dispatchers.IO){
        return@withContext RetrofitModule.movieApi.getCredits(movieId = movieId)
    }

    suspend fun getGenresList() = withContext(Dispatchers.IO) {
        return@withContext RetrofitModule.movieApi.getGenres()
    }


    suspend fun getMovieResponse(page:Int = 1) = withContext(Dispatchers.IO) {
        return@withContext RetrofitModule.movieApi.getOnPlayingMovies(page)
    }

    private interface TheMovieApi {
        @GET("configuration?")
        suspend fun getImagesConfigurationInfo(): ConfigurationInfoClass

        @GET("movie/now_playing?")
        suspend fun getOnPlayingMovies(
            @Query("page") page:Int
        ): ResponseClass

        @GET("genre/movie/list?")
        suspend fun getGenres(): ResponseGenreClass

        @GET("movie/{movie_id}?")
        suspend fun getDetails(
            @Path("movie_id") movieId:String
        ):JsonMovieDetails

        @GET("movie/{movie_id}/credits?")
        suspend fun getCredits(
            @Path("movie_id") movieId:String
        ):CreditResponse

    }

    /** [RetrofitModule] with
     *
     * [json]
     * [okHttpClient] with [HttpLoggingInterceptor]
     *[retrofit] and create Api [movieApi]
     *
     * */
    private object RetrofitModule {

        private val json = Json {
            ignoreUnknownKeys = true
        }

        private var okHttpClient = OkHttpClient().newBuilder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            addInterceptor(ApiKeyInterceptor())
        }.build()

        private val retrofit = Retrofit.Builder().apply {
            client(okHttpClient)
            baseUrl(baseUrlString)

            addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        }.build()

        val movieApi: TheMovieApi = retrofit.create(TheMovieApi::class.java)
    }


    companion object {
        private const val baseUrlString = "https://api.themoviedb.org/3/"
        var baseImagePosterUrl =
            "https://image.tmdb.org/t/p/w500" // todo instead w500 realize GET configuration
        var baseImageBackdropUrl = "https://image.tmdb.org/t/p/w780"
    }


}



