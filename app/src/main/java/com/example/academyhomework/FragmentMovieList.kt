package com.example.academyhomework

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academyhomework.model.Movie
import com.example.academyhomework.model.movielist.MovieListAdapter
import com.example.academyhomework.utils.GridSpacingItemDecoration
import com.example.academyhomework.viewmodel.ViewModelFactory
import com.example.academyhomework.viewmodel.ViewModelMovie
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentMovieList : BaseFragment() {
    private lateinit var progressBar: ProgressBar
    private var router: Router? = null

    /**
     * set click on  [moveToDetails] handler with [movie] ID
     *
     * */
    private val adapter by lazy {
        MovieListAdapter { id, view ->
            router?.transitView = view
            viewModel.loadDetails(id)
        }
    }

    private lateinit var recyclerView: RecyclerView


    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ViewModelMovie


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Router) {
            router = context
        }
        /**
         * initializing [viewModel]
         */
        viewModelFactory =
            ViewModelFactory(applicationContext = requireActivity().applicationContext)
        viewModel = ViewModelProvider(
            requireActivity().viewModelStore, viewModelFactory
        )
            .get(ViewModelMovie::class.java)
    }

    override fun onDetach() {
        super.onDetach()
        router = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)

        /**
         * waiting for a recycler view will draw items
         * that [can] transition animate by [MaterialMotion]
         */
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /**
         *  [exit] and [reenter] transitions animate with elevation effect
         */
        exitTransition = MaterialElevationScale(false).apply {
            duration = 1000

        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 1000

        }
        progressBar = view.findViewById(R.id.progressBar)
        setRecycler(view)
        /**
         * set [observers] for recycler list, progressbar, errorCoroutine toast
         */
        viewModel.movieList.observe(this.viewLifecycleOwner, this::setList)
        viewModel.loadingState.observe(this.viewLifecycleOwner, this::showProgressBar)
        viewModel.errorEvent.observe(this.viewLifecycleOwner, this::showErrorToast)
//        viewModel.repositoryObservable.observe(viewLifecycleOwner) {
//            viewModel.loadMovieCacheFromBack(it)
//        }
        /** observe WorkManager state for update after 10 sec UI*/
        viewModel.wmObservable.observe(viewLifecycleOwner) { viewModel.workManagerStatesHandler(it) }
    }


    private fun showProgressBar(loadingProgressBar: Boolean) {
        when (loadingProgressBar) {
            true -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.GONE
        }
    }

    private fun setRecycler(view: View) {
        recyclerView = view.findViewById(R.id.rv_movie_list)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, 30, true))
        recyclerView.adapter = adapter

    }

    private fun setList(list: List<Movie>) {
        CoroutineScope(Dispatchers.Default).launch {
            adapter.submitList(list) { }//recyclerView.scrollToPosition(0) }
        }


    }

    private fun showErrorToast(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
    }


    companion object {
        @JvmStatic
        fun newInstance() = FragmentMovieList()
    }
}