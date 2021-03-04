package com.example.academyhomework.domain.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET

/**
 * 	[TheMovieDb] API
 *
 *
 */
class NetworkModule {

	var coroutineScope = CoroutineScope(Dispatchers.IO)

	private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
		Log.e("Http", "Coroutine exception, scope active:${this.coroutineScope.isActive}", throwable)
		coroutineScope = CoroutineScope(Dispatchers.IO)
	}

	fun MockupStartLog(){
		coroutineScope.launch() {

			Log.d("MockupStartLog", "${RetrofitModule.movieApi.getOnPlayingMovies().results?.map { it?.title }}: ")
		}
	}

    private interface TheMovieApi {
        @GET("configuration")
        suspend fun getImagesConfigurationInfo(): ImagesConfigurationInfoClass

        @GET("movie/now_playing$apiKey")
		suspend fun getOnPlayingMovies():ResponseClass
    }

	/** [RetrofitModule] with
	 * [json]
	 * [okHttpClient] with [HttpLoggingInterceptor]
	 *[retrofit] and create Api [movieApi]
	 *
	 * */
	private object RetrofitModule{

		private val json = Json {
			ignoreUnknownKeys = true
		}

		private val okHttpClient = OkHttpClient().newBuilder().apply {
			addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
		}.build()

		private val retrofit = Retrofit.Builder().apply {
			client(okHttpClient)
			baseUrl( baseUrlString)
			addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
		}.build()

		val movieApi: TheMovieApi = retrofit.create(TheMovieApi::class.java)

	}


companion object{

	private const val baseUrlString = "https://api.themoviedb.org/3/"
	private const val baseImageUrl = "https://image.tmdb.org/t/p/"
	private const val apiKey = "?api_key=4638f9d08c5772da23d03dc27501af71"
}


}

@Serializable
data class ResponseClass(
	@SerialName("dates")
	val dates: Dates? = null,
	@SerialName("page")
	val page: Int? = null,
	@SerialName("total_pages")
	val totalPages: Int? = null,
	@SerialName("results")
	val results: List<JsonMovieNew?>? = null,
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
data class ImagesConfigurationInfoClass(
	val images: List<ListOfConfigs>
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
