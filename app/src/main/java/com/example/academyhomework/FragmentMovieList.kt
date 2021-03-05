package com.example.academyhomework

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academyhomework.model.Movie
import com.example.academyhomework.model.movielist.MovieListAdapter
import com.example.academyhomework.viewmodel.ViewModelMovie
import java.io.Serializable


private const val ARG_PARAM1 = "param1"


class FragmentMovieList : BaseFragment() {


    private lateinit var progressBar: ProgressBar

    private lateinit var adapter:MovieListAdapter
    private lateinit var recyclerView: RecyclerView
    private var listener: Router? = null


    private var viewModelArg: Serializable? = null
    private val viewModel:ViewModelMovie get() = viewModelArg as ViewModelMovie


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Router) {
            listener = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
        viewModelArg = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModelArg = it.getSerializable(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movie_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = view.findViewById(R.id.progressBar)
        setRecycler(view)
        viewModel.movieList.observe(this.viewLifecycleOwner,this::setList)
        viewModel.loadingState.observe(this.viewLifecycleOwner,this::showProgressBar)
    }

    private fun showProgressBar(loadingProgressBar: Boolean) {
        when (loadingProgressBar){
            true -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.GONE
        }
    }

    private fun setRecycler(view: View) {
        /** set [moveToDetails] handler with [movie] */
        adapter = MovieListAdapter {
                movie -> listener?.moveToDetails(movie)

        }
        recyclerView = view.findViewById(R.id.rv_movie_list)
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        recyclerView.adapter = adapter
    }

    private fun setList(list: List<Movie>) {
            adapter.submitList(list)
    }

    companion object {
        @JvmStatic
        fun newInstance(viewMovie: ViewModelMovie) =
            FragmentMovieList().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, viewMovie)
                }
            }
    }
}