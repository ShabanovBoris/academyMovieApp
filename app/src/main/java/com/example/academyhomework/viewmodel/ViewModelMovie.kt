package com.example.academyhomework.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*

import com.example.academyhomework.data.JsonMovieRepository
import com.example.academyhomework.model.Movie
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable

class ViewModelMovie(arg: String): ViewModel() ,Serializable{

    private var _movieList = MutableLiveData<List<Movie>>(emptyList())
    val movieList:LiveData<List<Movie>> get() = _movieList



    fun loadMovieList(context: Context) {
        viewModelScope.launch {
             val list = JsonMovieRepository(context).loadMovies()
            _movieList.postValue(list)
        }
    }
}