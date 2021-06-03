package com.example.academyhomework.di

import android.app.Application
import com.example.academyhomework.domain.data.MovieNetwork
import com.example.academyhomework.domain.data.network.NetworkMovieRepository
import dagger.Module
import dagger.Provides

@Module
class AppModule(val application: Application? = null) {
    //Nothing
}