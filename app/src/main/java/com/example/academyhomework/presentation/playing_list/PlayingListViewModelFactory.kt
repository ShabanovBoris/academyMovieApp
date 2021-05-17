package com.example.academyhomework.presentation.playing_list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.academyhomework.domain.data.MovieDatabase
import com.example.academyhomework.domain.data.MovieNetwork
import javax.inject.Inject


class PlayingListViewModelFactory @Inject constructor(
    private var applicationContext: Context,
    private val movieNetwork: MovieNetwork,
    private val movieDatabase: MovieDatabase
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {

        PlayingListViewModelMovie::class.java -> PlayingListViewModelMovie(
            movieDatabase = movieDatabase,
            movieNetwork = movieNetwork,
            workManager = WorkManager.getInstance(applicationContext)
        )

        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}