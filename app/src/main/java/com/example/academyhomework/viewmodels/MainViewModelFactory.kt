package com.example.academyhomework.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.academyhomework.domain.data.database.MovieDatabaseRepositry
import com.example.academyhomework.domain.data.network.JsonMovieRepository


class MainViewModelFactory(
    private var applicationContext: Context
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {

        MainViewModelMovie::class.java -> MainViewModelMovie(
            movieDatabase = MovieDatabaseRepositry(applicationContext),
            movieNetwork = JsonMovieRepository(),
            workManager = WorkManager.getInstance(applicationContext)
        )

        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}