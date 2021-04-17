package com.example.academyhomework.services.db_update

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.academyhomework.domain.data.database.DataBaseRepository
import com.example.academyhomework.domain.data.network.JsonMovieRepository
import com.example.academyhomework.utils.MovieDiff
import kotlinx.coroutines.*

class DbUpdateWorker(appContext: Context, params: WorkerParameters) : Worker(appContext, params) {


    private var attempt = 0
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val dataBaseRepository = DataBaseRepository(appContext)
    private val jsonMovieRepository = JsonMovieRepository()
    private val notification: Notification = NotificationsNewMovie(appContext)

    override fun doWork(): Result {

        notification.initialize()
        var isError = false


         val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
            Log.e(
                "AcademyHomework",
                "Coroutine exception in WORK MANAGER: $throwable",
                throwable
            )
             isError = true
             context.cancelChildren()
         }

//
        Log.d("AcademyHomework", "doWork: is running, $runAttemptCount")
        scope.launch(exceptionHandler) {

            val list = jsonMovieRepository.loadMovies(1..5)
            if (list.isEmpty()){
                isError = true
            }

            val oldList = dataBaseRepository.getMovieList()
//
//
            val diff = MovieDiff.getDiff(list, oldList)
            when (diff) {
                is MovieDiff.Relevance.OutOfDate -> {
                    Log.d(
                        "AcademyHomework",
                        "worker diff.isNotEmpty() ${diff.newListIndies.size} diff.size ${diff.newListIndies.size} "
                    )
                    dataBaseRepository.clearMovies()
                    dataBaseRepository.insertMovies(list)
                    val newMovie = jsonMovieRepository.loadMovieDetails(diff.newListIndies.last())
                    notification.showNotification(newMovie)
                }
                MovieDiff.Relevance.FreshData -> {
                    Log.d(
                        "AcademyHomework",
                        "worker have not changes ${list.size} and ${oldList.size} diff ${diff.toString()}"
                    )
                }
            }

        }.invokeOnCompletion {
            if (isError) {
                doWork()
                Log.d("AcademyHomework", "worker Result.retry() / Have error attempt $attempt")
                attempt++
            }else{
                Log.d("AcademyHomework", "worker Result.success()")
            }
        }

        return Result.success()
    }

}