package com.example.academyhomework.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.academyhomework.domain.data.database.DataBaseRepository
import com.example.academyhomework.domain.data.network.JsonMovieRepository
import com.example.academyhomework.domain.data.network.MovieRepository
import com.example.academyhomework.model.Actor
import com.example.academyhomework.model.Movie
import com.example.academyhomework.model.MovieDetails
import com.example.academyhomework.services.WorkRepository
import com.example.academyhomework.utils.SingleLiveEvent
import kotlinx.coroutines.*

class ViewModelMovie(
    private val dataBaseRepository: DataBaseRepository,
    private val jsonMovieRepository: MovieRepository,
    private val workManager: WorkManager
) : ViewModel() {

    init {
        if ( workManager.getWorkInfosByTag("DpUpdateService")
                .get().isEmpty()
//                workManager.getWorkInfosByTag("DpUpdateService")
//                .get()[0].state != WorkInfo.State.ENQUEUED
        ) {
            workManager.enqueue(WorkRepository().periodRequest)
            Log.d(
                "AcademyHomework",
                "new DpUpdateService was launched" + workManager.getWorkInfosByTag("DpUpdateService")
                    .get().toString() + workManager.getWorkInfosByTag("DpUpdateService").get().size
            )
        }else {
            Log.d(
                "AcademyHomework",
                """DpUpdateService info: size ${workManager.getWorkInfosByTag("DpUpdateService").get().size}
                    | state : ${workManager.getWorkInfosByTag("DpUpdateService")
                    .get()[0].state}
                    |${workManager.getWorkInfosByTag("DpUpdateService")}
                """.trimMargin()
            )

        }
    }
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(
            "ViewModelMovie",
            "Coroutine exception, scope active:${viewModelScope.isActive}, $throwable",
            throwable
        )
        throwable.printStackTrace()
        _errorEvent.value = throwable
    }
    /** Movie list*/
    private var _movieList = MutableLiveData<List<Movie>>(emptyList())
    val movieList: LiveData<List<Movie>> get() = _movieList
    /** Loading state*/
    private var _loadingState = MutableLiveData<Boolean>(false)
    val loadingState: LiveData<Boolean> get() = _loadingState
    /** Movie Details*/
    private var _details = MutableLiveData<MovieDetails>()
    val details: LiveData<MovieDetails> get() = _details
    /** Exception event*/
    private var _errorEvent = SingleLiveEvent<Throwable>()
    val errorEvent: LiveData<Throwable> get() = _errorEvent

    fun loadDetails(id: Int) {


        viewModelScope.launch(exceptionHandler) {
            _loadingState.value = true
            withContext(Dispatchers.Default){
                loadMovieDetailCache(id)
            }
            val data = jsonMovieRepository.loadMovieDetails(id)
            _details.value = data
            uploadMoviesDetailsCache(data)
            _loadingState.value = false
        }
    }

    fun loadMovieList() {

        viewModelScope.launch(exceptionHandler) {
            _loadingState.value = true

            val list = jsonMovieRepository.loadMovies()
            _movieList.postValue(list)

            uploadMoviesCache(list)

            _loadingState.value = false
        }

    }


    private fun loadMovieDetailCache(id: Int) {
        viewModelScope.launch(exceptionHandler) {
            val data = dataBaseRepository.getMovieDetails(id)
            if(data != null) {
                _details.postValue(data!!)
            }
        }
    }

    private fun uploadMoviesDetailsCache(details: MovieDetails) {
        if(_details.value != null) {
            viewModelScope.launch {
                dataBaseRepository.insertMovieDetails(details)
                dataBaseRepository.insertActors(details.actors)
            }
        }
    }


    fun loadMovieCache() {
        if ((movieList.value ?: emptyList()).isEmpty()) {
            viewModelScope.launch {
                _movieList.postValue(dataBaseRepository.getMovieList())
            }
        }
    }


    private fun uploadMoviesCache(list: List<Movie>) {

        if ((movieList.value ?: emptyList()).isEmpty()) {
            viewModelScope.launch {
                dataBaseRepository.clearMovies()
                dataBaseRepository.insertMovies(list)
            }

        }
    }

}