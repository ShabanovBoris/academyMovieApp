package com.example.academyhomework.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.academyhomework.domain.data.MovieDatabase
import com.example.academyhomework.domain.data.MovieNetwork
import com.example.academyhomework.domain.data.database.MovieDatabaseRepository
import javax.inject.Inject


class MainViewModelFactory @Inject constructor(
    private var applicationContext: Context,
    private val movieNetwork: MovieNetwork,
    private val movieDatabase: MovieDatabase
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {

        MainViewModelMovie::class.java -> MainViewModelMovie(
            movieDatabase = movieDatabase,
            movieNetwork = movieNetwork,
            workManager = WorkManager.getInstance(applicationContext)
        )

        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}