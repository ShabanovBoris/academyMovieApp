package com.example.academyhomework.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academyhomework.MovieApp
import com.example.academyhomework.Router
import com.example.academyhomework.databinding.FragmentSearchBinding
import com.example.academyhomework.entities.Movie
import com.example.academyhomework.presentation.BaseFragment
import com.example.academyhomework.presentation.ViewModelFactory
import com.example.academyhomework.presentation.adapters.SearchListAdapter
import com.example.academyhomework.utils.extensions.*
import com.example.academyhomework.utils.recycler.GridSpacingItemDecoration
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var router: Router? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val searchViewModel: SearchViewModel by viewModels { viewModelFactory }

    /**
     * set click on  [moveToDetails] handler with [movie] ID
     *
     * */
    private val mAdapter by lazy {
        SearchListAdapter { id, view ->
            router?.transitView = view
            searchViewModel.loadDetails(id, router)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        router = (context as? Router)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        /** ApplicationComponent */
        (requireActivity().application as MovieApp).appComponent.inject(this)

        /**
         * waiting for a recycler view will draw items
         * that [can] transition animate by [MaterialMotion]
         */
        postponeEnterTransition()
        binding.root.doOnPreDraw {
            startPostponedEnterTransition()
        }

        return binding.root
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etSearching.requestFocus()
        binding.etSearching.showKeyboard()

        setRecycler(view)
        setReenterExitAnim()

        searchViewModel.searchResult
            .onEach(::setList)
            .catch { showErrorToast(it) }
            .launchIn(lifecycleScope)

        searchViewModel.searchState
            .onEach(::showProgressBar)
            .launchIn(lifecycleScope)

        searchViewModel.setSearchFlow(binding.etSearching, viewLifecycleOwner)
    }

    private fun setReenterExitAnim() {
        /**
         *  [exit] and [reenter] transitions animate with elevation effect
         */
        exitTransition = MaterialElevationScale(false).apply {
            duration = 1000
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 1000
        }
    }

    private fun showProgressBar(loadingProgressBar: Boolean) {
        when (loadingProgressBar) {
            true -> binding.progressBar.visibility = View.VISIBLE
            false -> binding.progressBar.visibility = View.GONE
        }
    }


    private fun setRecycler(view: View) {
        val gridLayoutManager = GridLayoutManager(view.context, 2)

        binding.rvMovieList.apply {
            setPadding(0, (45).toPx, 0, 0)
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            addItemDecoration(GridSpacingItemDecoration(2, 30, true))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        setPadding(0, 0, 0, 0)
                        recyclerView.hideKeyboard()
                        hideEditText(binding)
                    }
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        showEditText(binding)
                    }

                }
            })
            adapter = mAdapter
        }

    }

    private fun setList(list: List<Movie>) {
        lifecycleScope.launchWhenStarted {

            mAdapter.submitList(list) { }
        }
    }

    private fun showErrorToast(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
    }


    override fun onDetach() {
        super.onDetach()
        binding.rvMovieList.clearOnScrollListeners()
        router = null
    }

    companion object {
        @JvmStatic
        fun newInstance(
            /***/
        ) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    /**stub*/
                }
            }
    }
}