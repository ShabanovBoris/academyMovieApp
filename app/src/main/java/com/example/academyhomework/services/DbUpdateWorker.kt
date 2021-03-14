package com.example.academyhomework.services

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.WorkInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.academyhomework.domain.data.database.DataBaseRepository
import com.example.academyhomework.domain.data.network.JsonMovieRepository
import com.example.academyhomework.model.Movie
import kotlinx.coroutines.*

class DbUpdateWorker(appContext: Context, params: WorkerParameters): Worker(appContext,params) {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(
            "ViewModelMovie",
            "Coroutine exception in Work Manager, scope active: $throwable",
            throwable
        )
    }

    private val dataBaseRepository = DataBaseRepository(appContext)
    private val jsonMovieRepository = JsonMovieRepository()


    override fun doWork(): Result {

        var list = listOf<Movie>()
        Log.d("AcademyHomework", "doWork: is running, $runAttemptCount")
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            list = jsonMovieRepository.loadMovies()
            dataBaseRepository.clearMovies()
            dataBaseRepository.insertMovies(list)
        }


        return Result.success()
    }




}