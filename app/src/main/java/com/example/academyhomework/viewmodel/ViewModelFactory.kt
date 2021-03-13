package com.example.academyhomework.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.academyhomework.domain.data.database.DataBaseRepository
import com.example.academyhomework.domain.data.network.JsonMovieRepository


class ViewModelFactory(
    private var applicationContext: Context
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {

        ViewModelMovie::class.java -> ViewModelMovie(
            dataBaseRepository = DataBaseRepository(applicationContext),
            jsonMovieRepository = JsonMovieRepository(),
            workManager = WorkManager.getInstance(applicationContext)
        )

        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}