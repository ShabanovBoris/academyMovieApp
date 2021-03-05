package com.example.academyhomework.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*

import com.example.academyhomework.domain.data.JsonMovieRepository
import com.example.academyhomework.model.Movie
import kotlinx.coroutines.*
import java.io.Serializable

class ViewModelMovie(arg: String): ViewModel(), Serializable{

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("ViewModelMovie", "Coroutine exception, scope active:${viewModelScope.isActive}", throwable)
    }
    private var _movieList = MutableLiveData<List<Movie>>(emptyList())
    val movieList:LiveData<List<Movie>> get() = _movieList

    private var _loadingState = MutableLiveData<Boolean>(false)
    val loadingState:LiveData<Boolean> get() = _loadingState



    fun loadMovieList() {

        viewModelScope.launch(exceptionHandler) {
            _loadingState.value = true
             val list = JsonMovieRepository().loadMovies()
            _movieList.postValue(list)
            _loadingState.value = false
        }

    }
}