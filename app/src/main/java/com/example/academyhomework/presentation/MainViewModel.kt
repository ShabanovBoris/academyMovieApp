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
    //---val repositoryObservable get() = dataBaseRepository.getObserver()
    /** WorkManager init and
     *   [WorkInfo] state observer
     */
    private val updateDBWorkRepository = UpdateDBWorkRepository()
    val wmObservable: LiveData<WorkInfo> =
        updateDBWorkRepository.initWorkManagerWithPeriodWork(workManager)


    /** Movie Details*/
    private var _details = SingleLiveEvent<MovieDetails>()
    val details: LiveData<MovieDetails> get() = _details
    /** Exception event*/
    private var _errorEvent = SingleLiveEvent<Throwable>()
    val errorEvent: LiveData<Throwable> get() = _errorEvent
    /** Loading state*/
    private var _loadingState = MutableLiveData<Boolean>(false)
    val loadingState: LiveData<Boolean> get() = _loadingState



    //loading movie details job
    private var job: Job? = null

    /**
     * @param id for load details view for
     * specific movie
     */
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

    companion object {
        const val TAG = "Academy"
    }

}