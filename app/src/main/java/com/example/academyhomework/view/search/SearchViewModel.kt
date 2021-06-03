package com.example.academyhomework.view.search

import android.text.Editable
import android.util.Log
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.academyhomework.Router
import com.example.academyhomework.domain.data.MovieNetwork
import com.example.academyhomework.entities.Movie
import com.example.academyhomework.entities.MovieDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(private val movieNetwork: MovieNetwork) : ViewModel() {
    companion object {
        const val TAG = "Academy"
    }
    private var loadDetailsJob: Job? = null

    private val _searchResult = MutableSharedFlow<List<Movie>>()
    private val _searchState = MutableStateFlow(false)

    val searchResult get() = _searchResult.asSharedFlow()
    val searchState get() = _searchState.asStateFlow()

    private fun searchMovie(query: String) {
        viewModelScope.launch {
            _searchState.value = true
            if (query.isNotEmpty())
                _searchResult.emit(movieNetwork.search(query))
            _searchState.value = false
        }
    }


    @FlowPreview
    @ExperimentalCoroutinesApi
    fun setSearchFlow(editText: EditText, lifecycleOwner: LifecycleOwner) {
        callbackFlow<String> {
            val actionCallBack: (Editable?) -> Unit = { editable ->
                offer(editable.toString())
            }
            editText.doAfterTextChanged(actionCallBack)
            awaitClose { editText.doAfterTextChanged {} }
        }
            .debounce(900)
            .distinctUntilChanged()
            .buffer(Channel.CONFLATED)
            .onEach (::searchMovie)
            .launchIn(lifecycleOwner.lifecycleScope)

    }


    fun loadDetails(id: Int, router: Router?) {

        if (id == -1) return
        loadDetailsJob?.cancel()

        //view model scope have *supervisor job*
        loadDetailsJob = viewModelScope.launch {
            _searchState.value = true

            router?.moveToDetails(
                try {
                    movieNetwork.loadMovieDetails(id)
                }catch (e: Throwable){
                    Log.e(TAG, "loadDetails: ${e.stackTrace}" )
                } as MovieDetails
            )

            _searchState.value = false
        }
    }

}