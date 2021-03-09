package com.example.academyhomework

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academyhomework.model.Movie
import com.example.academyhomework.model.movielist.MovieListAdapter
import com.example.academyhomework.viewmodel.ViewModelFactory
import com.example.academyhomework.viewmodel.ViewModelMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable



class FragmentMovieList : BaseFragment() {


    private lateinit var progressBar: ProgressBar

    /**
     * set click on  [moveToDetails] handler with [movie] ID
     *
     * */
    private val adapter by lazy { MovieListAdapter(viewModel::loadDetails) }

    private lateinit var recyclerView: RecyclerView
    private var listener: Router? = null


    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel :ViewModelMovie


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Router) {
            listener = context
        }
        /**
         * initializing [viewModel]
         */
        viewModelFactory = ViewModelFactory(applicationContext = requireActivity().applicationContext)
        viewModel= ViewModelProvider(
            requireActivity().viewModelStore, viewModelFactory
        )
            .get(ViewModelMovie::class.java)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movie_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = view.findViewById(R.id.progressBar)
        setRecycler(view)
        /**
         * set [observers]
         */
        viewModel.movieList.observe(this.viewLifecycleOwner, this::setList)
        viewModel.loadingState.observe(this.viewLifecycleOwner, this::showProgressBar)
        viewModel.errorEvent.observe(this.viewLifecycleOwner,this::showErrorToast)
    }

    private fun showProgressBar(loadingProgressBar: Boolean) {
        when (loadingProgressBar) {
            true -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.GONE
        }
    }

    private fun setRecycler(view: View) {


        recyclerView = view.findViewById(R.id.rv_movie_list)
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        recyclerView.adapter = adapter

    }

    private fun setList(list: List<Movie>) {
        CoroutineScope(Dispatchers.Default).launch {
            adapter.submitList(list) { recyclerView.scrollToPosition(0) }
        }


    }
    private fun showErrorToast(error:Throwable){
        Toast.makeText(requireContext(), error.message?.toUpperCase(), Toast.LENGTH_LONG).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentMovieList()
    }
}