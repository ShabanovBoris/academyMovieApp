package com.example.academyhomework.view.launcher

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import com.example.academyhomework.Router
import com.example.academyhomework.databinding.FragmentLaunchBinding
import com.example.academyhomework.entities.Movie
import com.example.academyhomework.view.BaseFragment
import com.example.academyhomework.view.MainViewModel
import com.example.academyhomework.view.ViewModelFactory
import com.example.academyhomework.view.adapters.MovieListAdapterMini
import javax.inject.Inject

class LaunchFragment : BaseFragment() {
    private var _binding: FragmentLaunchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mainViewModel: MainViewModel by activityViewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /** Router component*/
        (requireActivity() as Router).getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaunchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbSearch.setOnMenuItemClickListener {
            router.moveToSearchFragment()
            return@setOnMenuItemClickListener true
        }
        binding.tvOnPlayingMore.setOnClickListener {
            router.moveToOnPlayingMovies()
        }
        initRecyclerView()
        mainViewModel.movieList.observe(viewLifecycleOwner){setUpList(it)}
    }

    private fun initRecyclerView() {
        binding.rvOnPlaying.apply {
            val adapterMini = MovieListAdapterMini()
            adapterMini.setOnContentClick { mainViewModel.loadDetails(it)}
            adapter = adapterMini
            setHasFixedSize(true)
        }
    }

    /**
     * load list from map
     */
    private fun setUpList(map: Map<String, List<Movie>>) {
        (binding.rvOnPlaying.adapter as MovieListAdapterMini).apply {
            bindMovies(
                map[MainViewModel.ON_PlAYING].orEmpty()
            )

            /**
             * add new lists
             * @sample
             *  bindMovies(
             *  map[MainViewModel.[NEW_LIST_TAG]].orEmpty()
             *  )
             */

            notifyDataSetChanged()
        }


    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }
}