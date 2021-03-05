package com.example.academyhomework.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academyhomework.domain.data.JsonMovieRepository
import com.example.academyhomework.model.Movie
import com.example.academyhomework.model.MovieDetails
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.Serializable

class ViewModelMovie(arg: String) : ViewModel(), Serializable {


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(
            "ViewModelMovie",
            "Coroutine exception, scope active:${viewModelScope.isActive}",
            throwable
        )
    }
    private var _movieList = MutableLiveData<List<Movie>>(emptyList())
    val movieList: LiveData<List<Movie>> get() = _movieList

    private var _loadingState = MutableLiveData<Boolean>(false)
    val loadingState: LiveData<Boolean> get() = _loadingState

    private var _details = MutableLiveData<MovieDetails>()
    val details: LiveData<MovieDetails> get() = _details

    fun loadDetails(id: Int) {
        viewModelScope.launch(exceptionHandler) {
            _loadingState.value = true
            _details.value = JsonMovieRepository().loadMovieDetails(id)
            _loadingState.value = false
        }
    }

    fun loadMovieList() {

        viewModelScope.launch(exceptionHandler) {
            _loadingState.value = true
            val list = JsonMovieRepository().loadMovies()
            _movieList.postValue(list)
            _loadingState.value = false
        }

    }
}