package com.example.academyhomework.viewmodel

import android.app.Notification
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.academyhomework.Router

class DetailsViewModel(private val router: Router): ViewModel() {

    private var _openState = MutableLiveData<WebResult>()
    val openSate get() = _openState


    fun openWebPage(movieId: Int){
        when(router.openWebPage(movieId)){
            true -> _openState.value = WebResult.Success
            false -> _openState.value = WebResult.Fail
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val router: Router) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailsViewModel(router) as T
        }
    }
}


sealed class WebResult{
    object Success:WebResult()
    object Fail:WebResult()
}



