package com.example.academyhomework.di


import androidx.work.Worker
import com.example.academyhomework.MainActivity
import com.example.academyhomework.domain.data.MovieNetwork
import com.example.academyhomework.domain.data.network.NetworkMovieRepository
import com.example.academyhomework.presentation.FragmentMovieList
import dagger.Binds
import dagger.Component
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class, ContextModule::class])
interface ApplicationComponent {


    fun inject(fragmentMovieList: FragmentMovieList)
    fun inject(act: MainActivity)
    fun inject(work: Worker)


}