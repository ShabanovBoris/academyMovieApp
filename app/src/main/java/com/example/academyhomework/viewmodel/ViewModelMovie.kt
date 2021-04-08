package com.example.academyhomework.viewmodel


import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.academyhomework.domain.data.database.DataBaseRepository
import com.example.academyhomework.domain.data.network.MovieRepository
import com.example.academyhomework.model.Movie
import com.example.academyhomework.model.MovieDetails
import com.example.academyhomework.services.WorkRepository
import com.example.academyhomework.utils.MovieDiff
import com.example.academyhomework.utils.SingleLiveEvent
import io.reactivex.Observable
import kotlinx.coroutines.*
import kotlinx.coroutines.rx2.rxObservable

class ViewModelMovie(
    private val dataBaseRepository: DataBaseRepository,
    private val jsonMovieRepository: MovieRepository,
    workManager: WorkManager
) : ViewModel() {

    private var job: Job? = null

    companion object {
        const val TAG = "Academy"
    }
    private val workRepository: WorkRepository = WorkRepository()


    /** WorkManager state observer*/
    val wmObservable: LiveData<WorkInfo> = workRepository.initWorkManagerWithPeriodWork(workManager)


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(
            TAG,
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
    private var _details = SingleLiveEvent<MovieDetails>()
    val details: LiveData<MovieDetails> get() = _details

    /** Exception event*/
    private var _errorEvent = SingleLiveEvent<Throwable>()
    val errorEvent: LiveData<Throwable> get() = _errorEvent

    @ExperimentalCoroutinesApi
    @SuppressLint("CheckResult")
    fun loadDetails(id: Int) {
        Observable.just(id)
            .doOnSubscribe {
                if (id != -1) {
                    _loadingState.value = true
                }
            }
            .flatMap {
                rxObservable {
                    if (loadMovieDetailCache(it)) {
                        _loadingState.postValue(false)
                    } else {
                        send(jsonMovieRepository.loadMovieDetails(it))
                    }
                }
            }
            .doOnComplete {
                _loadingState.postValue(false)
            }
            .subscribe({
                Log.d(TAG, "loadDetailsOnSubscribe: $it")
                _details.postValue(it)
                uploadMoviesDetailsCache(it)
            }, {
                _errorEvent.postValue(it)
            })
    }

    @ExperimentalCoroutinesApi
    @SuppressLint("CheckResult")
    fun loadMovieList() {
        rxObservable {
            Log.d(TAG, " VIEWMODEL ")
            send(dataBaseRepository.getMovieList())
        }
            .switchMap { oldList ->
                rxObservable { send(jsonMovieRepository.loadMovies()) }
                    .map { it to oldList }
            }
            .map {
                val list = it.first
                val oldList = it.second
                val diff = MovieDiff.getDiff(list, oldList)
                if (diff.isNotEmpty()) {
                    Log.d(
                        TAG,
                        "VIEWMODEL diff.isNotEmpty() ${diff.isNotEmpty()} diff.size ${diff.size} "
                    )
                    uploadMoviesCache(list)
                    return@map list
                } else {
                    Log.d(
                        TAG,
                        " VIEWMODEL have not changes ${list.size} and ${oldList.size} diff ${diff.toString()}"
                    )
                    return@map emptyList()
                }
            }
            //.observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _loadingState.postValue(true) }
            .doOnComplete {
                Log.d(TAG, "loadMovieList: ******rx completed")
                _loadingState.postValue(false)
            }
            .subscribe({
                if (it.isNotEmpty()) {
                    _movieList.postValue(it)
                }
            },
                {
                    _errorEvent.postValue(it)
                })
    }

    fun loadMovieCache() {

        //if ((movieList.value ?: emptyList()).isEmpty()) {
            viewModelScope.launch(exceptionHandler) {
                _loadingState.value = true
                Log.d(TAG, "loadMovieCache() ")
                _movieList.postValue(dataBaseRepository.getMovieList())
                _loadingState.value = false

        }
    }


    private suspend fun loadMovieDetailCache(id: Int): Boolean {

        val data = dataBaseRepository.getMovieDetails(id)
        return if (data != null) {
            _details.postValue(data!!)
            true
        } else {
            false
        }
    }

    private fun uploadMoviesDetailsCache(details: MovieDetails) {
        if (_details.value != null) {
            viewModelScope.launch(exceptionHandler) {
                dataBaseRepository.insertMovieDetails(details)
                dataBaseRepository.insertActors(details.actors)
            }
        }
    }


    private fun loadMovieCacheFromBack(movies: List<Movie>) {

        viewModelScope.launch {

            _movieList.postValue(movies)
            Log.d(TAG + "Homework", "loadMovieCacheFromBack()")
        }
    }


    private fun uploadMoviesCache(list: List<Movie>) {

//        if ((movieList.value ?: emptyList()).isEmpty()) {
            viewModelScope.launch {
                dataBaseRepository.clearMovies()
                dataBaseRepository.insertMovies(list)


        }
    }

    fun workManagerHandler(workInfo: WorkInfo) {
        workInfo.state?.let {
            when (it) {
                WorkInfo.State.RUNNING -> {
                    Log.d(TAG, "WorkInfo.State.RUNNING")
                }
                WorkInfo.State.ENQUEUED -> {
                    Log.d(TAG, "WorkInfo.State.ENQUEUED")
                    CoroutineScope(Dispatchers.Default).launch {
                        /**
                         *   10 seconds will be enough to load movie list from background =)
                         */
                        delay(10000)

                       loadMovieCacheFromBack(dataBaseRepository.getMovieList())

                    }

                }
                WorkInfo.State.FAILED -> {
                    Log.d(TAG, "WorkInfo.State.FAILED")
                }

                else -> Log.e(TAG, "WorkInfo.State.ELSE BRANCH")
            }
        }


    }


}