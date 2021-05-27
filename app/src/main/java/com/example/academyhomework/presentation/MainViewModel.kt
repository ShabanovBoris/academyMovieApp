package com.example.academyhomework.presentation


import android.util.Log
import androidx.lifecycle.*
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.academyhomework.domain.data.MovieDatabase
import com.example.academyhomework.domain.data.MovieNetwork
import com.example.academyhomework.entities.Movie
import com.example.academyhomework.entities.MovieDetails
import com.example.academyhomework.services.db_update_work_manager.UpdateDBWorkRepository
import com.example.academyhomework.utils.MovieDiffHelper
import com.example.academyhomework.utils.SingleLiveEvent
import com.example.academyhomework.utils.WorkManagerHelper
import kotlinx.coroutines.*

class MainViewModel(
    private val movieDatabase: MovieDatabase,
    private val movieNetwork: MovieNetwork,
    workManager: WorkManager
) : ViewModel() {
    companion object {
        const val TAG = "Academy"

        /**
         * default loading first page range
         */
        private val mPreloadedDefaultRange: IntRange = 1..2
    }

    //current loaded page
    private var mCurrentPage = 0
    val currentPage get() = mCurrentPage

    //loading movie list job
    private var job: Job? = null

    private val updateDBWorkRepository: UpdateDBWorkRepository = UpdateDBWorkRepository()

    //---val repositoryObservable get() = dataBaseRepository.getObserver()
    /** WorkManager state observer*/
    val wmObservable: LiveData<WorkInfo> =
        updateDBWorkRepository.initWorkManagerWithPeriodWork(workManager)


    private val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        Log.e(
            TAG,
            "Coroutine exception, scope active:${viewModelScope.coroutineContext}, $throwable",
            throwable
        )
        viewModelScope.coroutineContext.plus(Dispatchers.Main)

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




    fun loadDetails(id: Int) {

        if (id == -1) return
        job?.cancel()

        //view model scope have *supervisor job*
        job = viewModelScope.launch(exceptionHandler) {
            _loadingState.value = true


            if (loadMovieDetailCache(id)) {
                _loadingState.value = false
                return@launch
            }
            val data = movieNetwork.loadMovieDetails(id)
            _details.value = data
            uploadMoviesDetailsCache(data)
            _loadingState.value = false
        }
    }

    fun loadMovieList(pagesRangeValue: IntRange = mPreloadedDefaultRange) {

        viewModelScope.launch(exceptionHandler) {
            _loadingState.value = true
            val list = movieNetwork.loadMovies(pagesRangeValue)

            val oldList = movieDatabase.getMovieList()
            val diff = MovieDiffHelper.getDiff(list, oldList)

            when (diff) {
                // database out of date
                is MovieDiffHelper.Relevance.StaleData -> {
                    Log.d(
                        TAG,
                        "VIEWMODEL pages $mPreloadedDefaultRange MovieDiff.Relevance.OutOfDate ${diff.newListIndies.size} diff.size ${diff.newListIndies.size} "
                    )

                    uploadMoviesCache(list)
                    _movieList.postValue(list)
                    _loadingState.value = false
                }
                // database has not out of date
                MovieDiffHelper.Relevance.FreshData ->
                    Log.d(
                        TAG,
                        " VIEWMODEL pages $mPreloadedDefaultRange MovieDiff.Relevance.FreshData ${list.size} and ${oldList.size}"
                    )
            }
        }
    }

    fun loadMore() {
        mCurrentPage += mPreloadedDefaultRange.last
        val newRange =
            (mPreloadedDefaultRange.first + mCurrentPage)..(mPreloadedDefaultRange.last + mCurrentPage)
        additionalLoadPaging(newRange)
    }

    private fun additionalLoadPaging(pagesRangeValue: IntRange) {
        viewModelScope.launch(exceptionHandler) {
            val list = movieNetwork.loadMovies(pagesRangeValue)
            Log.d(
                TAG,
                "VIEWMODEL pages $pagesRangeValue LoadMore() "
            )
            val oldList = _movieList.value
            _movieList.postValue(oldList?.plus(list))
        }
    }


    fun loadMovieCache() {
        //if ((movieList.value ?: emptyList()).isEmpty()) {
        viewModelScope.launch(exceptionHandler) {
            _loadingState.value = true
            Log.d(TAG, "loadMovieCache() load Movies from Cache")
            _movieList.postValue(movieDatabase.getMovieList())
            if (_movieList.value != null) _loadingState.value = false
        }
    }


    private suspend fun loadMovieDetailCache(id: Int): Boolean {

        val data = movieDatabase.getMovieDetails(id)
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
                movieDatabase.insertMovieDetails(details)
                movieDatabase.insertActors(details.actors)
            }
        }
    }


    private fun uploadMoviesCache(list: List<Movie>) {
        viewModelScope.launch {
            movieDatabase.clearMovies()
            movieDatabase.insertMovies(list)
        }
    }

    fun workManagerStatesHandler(workInfo: WorkInfo) {
        if (WorkManagerHelper.statesHandler(workInfo)) {
            viewModelScope.launch {
                /**
                 *   10 seconds would be enough to load movie list from background =)
                 */
                delay(10000)
                loadMovieCacheFromWorkManager(movieDatabase.getMovieList())
            }
        }
    }
    private fun loadMovieCacheFromWorkManager(movies: List<Movie>) {
        viewModelScope.launch {
            _movieList.postValue(movies)
            Log.d(TAG + "Homework", "loadMovieCacheFromWorkManager()")
        }
    }

    init {
        loadMovieCache()
        loadMovieList()
    }
}