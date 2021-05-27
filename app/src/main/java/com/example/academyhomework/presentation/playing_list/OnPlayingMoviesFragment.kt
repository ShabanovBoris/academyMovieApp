package com.example.academyhomework.presentation.playing_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academyhomework.MovieApp
import com.example.academyhomework.R
import com.example.academyhomework.Router
import com.example.academyhomework.presentation.adapters.MovieListAdapter
import com.example.academyhomework.entities.Movie
import com.example.academyhomework.presentation.BaseFragment
import com.example.academyhomework.presentation.MainViewModelMovie
import com.example.academyhomework.presentation.ViewModelFactory
import com.example.academyhomework.utils.recycler.EndlessRecyclerViewScrollListener
import com.example.academyhomework.utils.recycler.GridSpacingItemDecoration
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class OnPlayingMoviesFragment : BaseFragment() {
    private lateinit var progressBar: ProgressBar
    private var router: Router? = null


    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mainViewModel: MainViewModelMovie by activityViewModels { viewModelFactory }
//    private val onPlayingMoviesViewModel: OnPlayingMoviesViewModel by viewModels{viewModelFactory}
//    private val onPlayingMoviesViewModel: OnPlayingMoviesViewModel by activityViewModels{viewModelFactory}

    /**
     * set click on  [moveToDetails] handler with [movie] ID
     *
     * */
    private val mAdapter by lazy {
        MovieListAdapter { id, view ->
            router?.transitView = view
            mainViewModel.loadDetails(id)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Router) {
            router = context
        }

//        childFragmentManager.beginTransaction()
        /** ApplicationComponent */
        (requireActivity().application as MovieApp).appComponent.inject(this)
    }

    override fun onDetach() {
        super.onDetach()
        router = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_movie_list_onplaying, container, false)
        /**
         * waiting for a recycler view will draw items
         * that [can] transition animate by [MaterialMotion]
         */
        //TODO fix
//        postponeEnterTransition()
//        view.doOnPreDraw {
//            startPostponedEnterTransition()
//        }

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
        mainViewModel.movieList.observe(this.viewLifecycleOwner, this::setList)
        mainViewModel.loadingState.observe(this.viewLifecycleOwner, this::showProgressBar)
        mainViewModel.errorEvent.observe(this.viewLifecycleOwner, this::showErrorToast)
//        viewModel.repositoryObservable.observe(viewLifecycleOwner) {
//            viewModel.loadMovieCacheFromBack(it)
//        }
        /**     with given lifecycle
         *   observe WorkManager state for update after 10 sec delay to UI
         * */
        mainViewModel.wmObservable.observe(viewLifecycleOwner) {
            mainViewModel.workManagerStatesHandler(it)
        }
    }


    private fun showProgressBar(loadingProgressBar: Boolean) {
        when (loadingProgressBar) {
            true -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.GONE
        }
    }

    @ExperimentalCoroutinesApi
    private fun setRecycler(view: View) {
        val gridLayoutManager = GridLayoutManager(view.context, 2)

        recyclerView = view.findViewById(R.id.rv_movie_list)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            addItemDecoration(GridSpacingItemDecoration(2, 30, true))
            adapter = mAdapter
        }
        /** set ScrollListener fro pagination with multi shot callbackFlow */
        callbackFlow<Int> {
            val paginationScrollListener =
                object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        //trigger flow
                        offer(totalItemsCount)
                    }
                }
            recyclerView.addOnScrollListener(paginationScrollListener)

            awaitClose{ recyclerView.clearOnScrollListeners() }
        }
            .buffer(Channel.RENDEZVOUS)
            .onEach { totalItemsCount ->
                //load from VM
                mainViewModel.loadMore()
                Toast.makeText(
                    requireContext(),
                    "need page ${mainViewModel.currentPage} totalItemsCount $totalItemsCount",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .launchIn(lifecycleScope)
    }

    private fun setList(list: List<Movie>) {
        lifecycleScope.launchWhenStarted {
            mAdapter.submitList(list)
        }
    }

    private fun showErrorToast(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
    }


    companion object {
        @JvmStatic
        fun newInstance() = OnPlayingMoviesFragment()
    }
}