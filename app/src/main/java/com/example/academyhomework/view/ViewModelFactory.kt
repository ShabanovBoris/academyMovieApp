package com.example.academyhomework.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.academyhomework.di.scopes.AppScope
import com.example.academyhomework.domain.data.MovieDatabase
import com.example.academyhomework.domain.data.MovieNetwork
import com.example.academyhomework.view.playing_list.OnPlayingMoviesViewModel
import com.example.academyhomework.view.search.SearchViewModel
import javax.inject.Inject

@AppScope
class ViewModelFactory @Inject constructor(
    private var applicationContext: Context,
    private val movieNetwork: MovieNetwork,
    private val movieDatabase: MovieDatabase
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {

        MainViewModel::class.java -> MainViewModel(
            movieDatabase = movieDatabase,
            movieNetwork = movieNetwork,
            workManager = WorkManager.getInstance(applicationContext)
        )

        SearchViewModel::class.java -> SearchViewModel(
            movieNetwork = movieNetwork
        )

        OnPlayingMoviesViewModel::class.java -> OnPlayingMoviesViewModel(
           movieDatabase = movieDatabase,
           movieNetwork = movieNetwork
        )

        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}