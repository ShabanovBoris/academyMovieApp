package com.example.academyhomework.presentation.playing_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.example.academyhomework.domain.data.MovieDatabase
import com.example.academyhomework.domain.data.MovieNetwork
import com.example.academyhomework.entities.Movie
import com.example.academyhomework.entities.MovieDetails
import com.example.academyhomework.presentation.MainViewModel
import com.example.academyhomework.utils.MovieDiffHelper
import com.example.academyhomework.utils.SingleLiveEvent
import com.example.academyhomework.utils.WorkManagerHelper
import kotlinx.coroutines.*

class OnPlayingMoviesViewModel(
    private val movieDatabase: MovieDatabase,
    private val movieNetwork: MovieNetwork,
): ViewModel() {
    companion object {
        const val TAG = "Academy"

        /**
         * default loading first page range
         */
        private val mPreloadedDefaultRange: IntRange = 1..2
    }

    //current loaded and showed page
    private var mCurrentPage = 0
    val currentPage get() = mCurrentPage


    private val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        Log.e(
            MainViewModel.TAG,
            "Coroutine exception, scope active:${viewModelScope.coroutineContext}, $throwable",
            throwable
        )
        viewModelScope.coroutineContext.plus(Dispatchers.Main)

        throwable.printStackTrace()
        _errorEvent.value = throwable
    }

    //region LiveData definition
    /** Movie list*/
    private var _movieList = MutableLiveData<List<Movie>>(emptyList())
    val movieList: LiveData<List<Movie>> get() = _movieList
    /** Loading state*/
    private var _loadingState = MutableLiveData<Boolean>(false)
    val loadingState: LiveData<Boolean> get() = _loadingState
    /** Exception event*/
    private var _errorEvent = SingleLiveEvent<Throwable>()
    val errorEvent: LiveData<Throwable> get() = _errorEvent
    // endregion





    /** Loading on playing movies
     *
     * @param pagesRangeValue 1..2 by default
     */
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
                        MainViewModel.TAG,
                        "VIEWMODEL pages ${mPreloadedDefaultRange} MovieDiff.Relevance.OutOfDate ${diff.newListIndies.size} diff.size ${diff.newListIndies.size} "
                    )

                    uploadMoviesCache(list)
                    _movieList.postValue(list)
                    _loadingState.value = false
                }
                // database has not out of date
                MovieDiffHelper.Relevance.FreshData ->
                    Log.d(
                        MainViewModel.TAG,
                        " VIEWMODEL pages ${mPreloadedDefaultRange} MovieDiff.Relevance.FreshData ${list.size} and ${oldList.size}"
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
                MainViewModel.TAG,
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
            Log.d(MainViewModel.TAG, "loadMovieCache() load Movies from Cache")
            _movieList.postValue(movieDatabase.getMovieList())
            if (_movieList.value != null) _loadingState.value = false
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
            Log.d(MainViewModel.TAG + "Homework", "loadMovieCacheFromWorkManager()")
        }
    }


    /**
     * Initialization viewmodel with preload movies
     */
    init {
        loadMovieCache()
        loadMovieList()
    }



//
// TODO 1. move paging logic here
//      in MainViewModel keep only cache logic
//      2. network query in MainActivity move to OnPlayingFragment
//      3. add Router in parameter constructor
//

    //TODO implement pattern MVI

     
    //TODO Activity shouldn't talk VM when to load data


}