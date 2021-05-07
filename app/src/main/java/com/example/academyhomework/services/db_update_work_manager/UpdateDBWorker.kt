package com.example.academyhomework.services.db_update_work_manager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.academyhomework.di.DaggerApplicationComponent
import com.example.academyhomework.domain.data.database.MovieDatabaseRepositry
import com.example.academyhomework.domain.data.network.NetworkMovieRepository
import com.example.academyhomework.domain.data.network.NetworkModule
import com.example.academyhomework.utils.MovieDiffHelper
import kotlinx.coroutines.*
import javax.inject.Inject

class UpdateDBWorker(appContext: Context, params: WorkerParameters) : Worker(appContext, params) {

    init {
        val component = DaggerApplicationComponent.builder().build()
        component.inject(this)
    }

    @Inject
    lateinit var networkModule: NetworkModule

    private var attempt = 0
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val dataBaseRepository = MovieDatabaseRepositry(appContext)
    private val jsonMovieRepository = NetworkMovieRepository(networkModule)
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

            val list = jsonMovieRepository.loadMovies(1..2)
            if (list.isEmpty()){
                isError = true
            }

            val oldList = dataBaseRepository.getMovieList()
//
//
            val diff = MovieDiffHelper.getDiff(list, oldList)
            when (diff) {
                is MovieDiffHelper.Relevance.OutOfDate -> {
                    Log.d(
                        "AcademyHomework",
                        "worker diff.isNotEmpty() ${diff.newListIndies.size} diff.size ${diff.newListIndies.size} "
                    )
                    dataBaseRepository.clearMovies()
                    dataBaseRepository.insertMovies(list)
                    val newMovie = jsonMovieRepository.loadMovieDetails(diff.newListIndies.last())
                    notification.showNotification(newMovie)
                }
                MovieDiffHelper.Relevance.FreshData -> {
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