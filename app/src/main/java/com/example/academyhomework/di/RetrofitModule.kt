package com.example.academyhomework.di

import com.example.academyhomework.di.scopes.AppScope
import com.example.academyhomework.domain.data.MovieNetwork
import com.example.academyhomework.domain.data.network.ApiKeyInterceptor
import com.example.academyhomework.domain.data.network.NetworkMovieApi
import com.example.academyhomework.domain.data.network.NetworkMovieRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


@Module
class RetrofitModule {

    /**
     * Provide network repository for ViewModel
     */
    @Provides
    fun provideMovieNetworkImpl(impl: NetworkMovieRepository): MovieNetwork = impl
//    == alternative
//    @Binds
//    fun provideMovieNetworkInterface(impl: NetworkMovieRepository): MovieNetwork
//    *module must be interface or abstract

    /**
     * Api usage only in [NetworkMovieApi]
     */
    @Provides
    fun provideTheMovieApi(retrofit: Retrofit): NetworkMovieApi.TheMovieApi =
        retrofit.create(NetworkMovieApi.TheMovieApi::class.java)

    @AppScope
    @Provides
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder().apply {
            client(okHttpClient)
            baseUrl(baseUrl)
            addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        }.build()

    /**
     *  OkHttpClient for add [interceptor]
     */
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient().newBuilder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            addInterceptor(interceptor)
        }.build()

    /**
     *  Return [ApiKeyInterceptor] with ApiToken
     */
    @Provides
    fun provideInterceptor(): Interceptor = ApiKeyInterceptor()

    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    fun baseUrlString(): String = "https://api.themoviedb.org/3/"
}




