package com.example.academyhomework.services.db_update

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.academyhomework.domain.data.database.DataBaseRepository
import com.example.academyhomework.domain.data.network.JsonMovieRepository
import com.example.academyhomework.model.Movie
import com.example.academyhomework.utils.MovieDiff
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
    private val notification: Notification = NotificationsNewMovie(appContext)

    override fun doWork(): Result {
        notification.initialize()
//
        Log.d("AcademyHomework", "doWork: is running, $runAttemptCount")
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val list = jsonMovieRepository.loadMovies()
            val oldList = dataBaseRepository.getMovieList()
//
//
            val diff = MovieDiff.getDiff(list,oldList)
            if (diff.isNotEmpty()) {
                Log.d("AcademyHomework", "diff.isNotEmpty() ${diff.isNotEmpty()} diff.size ${diff.size} ")
                dataBaseRepository.clearMovies()
                dataBaseRepository.insertMovies(list)
                val newMovie = jsonMovieRepository.loadMovieDetails(diff.last())
                notification.showNotification(newMovie)
            }else
            {
                Log.d("AcademyHomework", "have not changes ${list.size} and ${oldList.size} diff ${diff.toString()}")
            }
//
        }


        return Result.success()
    }





}