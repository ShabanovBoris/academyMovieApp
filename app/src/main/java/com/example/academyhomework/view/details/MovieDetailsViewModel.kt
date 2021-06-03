package com.example.academyhomework.view.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.academyhomework.Router
import com.example.academyhomework.di.scopes.DetailsScope
import javax.inject.Inject

class DetailsViewModel(
    /****/
) : ViewModel() {

    private var _openState = MutableLiveData<WebResult>()
    val openSate get() = _openState


    fun openWebPage(movieId: Int, router: Router) {
        when (router.openWebPage(movieId)) {
            true -> _openState.value = WebResult.Success
            false -> _openState.value = WebResult.Fail
        }
    }


    @DetailsScope
    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        /****/
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailsViewModel(/****/) as T
        }
    }
}


sealed class WebResult {
    object Success : WebResult()
    object Fail : WebResult()
}



