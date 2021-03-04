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
import retrofit2.http.Query

/**
 * 	[TheMovieDb] API
 *
 *
 */


class NetworkModule() {
    /**
     * TODO [apiKey] inserting in the GET request body
     */

//	init {
//	    CoroutineScope(Dispatchers.Default).launch{
//			val info = RetrofitModule.movieApi.getImagesConfigurationInfo()
//			baseImagePosterUrl = info.images.base_url.toString()
//
//		}
//	}




    suspend fun getGenresList() = withContext(Dispatchers.IO) {
        return@withContext RetrofitModule.movieApi.getGenres()
    }


    suspend fun getMovieResponse(page:Int = 1) = withContext(Dispatchers.IO) {
        return@withContext RetrofitModule.movieApi.getOnPlayingMovies(page)
    }

    private interface TheMovieApi {
        @GET("configuration?$apiKey")
        suspend fun getImagesConfigurationInfo(): ConfigurationInfoClass

        @GET("movie/now_playing?$apiKey")
        suspend fun getOnPlayingMovies(
            @Query("page") page:Int
        ): ResponseClass



        @GET("genre/movie/list?$apiKey&language=en-US")
        suspend fun getGenres(): ResponseGenreClass


    }

    /** [RetrofitModule] with
     *
     * [json]
     * [okHttpClient] with [HttpLoggingInterceptor]
     *[retrofit] and create Api [movieApi]
     *
     * */
    private object RetrofitModule {
        var page:Int = 1

        private val json = Json {
            ignoreUnknownKeys = true
        }

        private var okHttpClient = OkHttpClient().newBuilder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            //addInterceptor(ApiKeyInterceptor(page))
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
        private const val apiKey = "api_key=4638f9d08c5772da23d03dc27501af71"
    }


}

@Serializable
data class ResponseClass(
    @SerialName("dates")
	val dates: Dates? = null,
    @SerialName("page")
	val page: Int? = null,
    @SerialName("total_pages")
	val totalPages: Int,
    @SerialName("results")
	val results: List<JsonMovie>,
    @SerialName("total_results")
	val totalResults: Int? = null
)

@Serializable
data class Dates(
	@SerialName("maximum")
	val maximum: String? = null,
	@SerialName("minimum")
	val minimum: String? = null
)

@Serializable
data class ResponseGenreClass(
	val genres: List<JsonGenre>
)

@Serializable
data class ConfigurationInfoClass(
	val images: ListOfConfigs
)

@Serializable
data class ListOfConfigs(
	val base_url: String?,
	val secure_base_url: String?,
	val backdrop_sizes: List<String?>?,
	val logo_sizes: List<String?>?,
	val poster_sizes: List<String?>?,
	val profile_sizes: List<String?>?,
	val still_sizes: List<String?>?,
)

//class ApiKeyInterceptor(var page: Int) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val original = chain.request()
//
//        val request = original.newBuilder()
//            .header("api_key", "4638f9d08c5772da23d03dc27501af71")
//            .url(original.url.toString().plus("&page=${page.toString()}"))
//            .build()
//
//        return chain.proceed(request)
//    }
//
//}