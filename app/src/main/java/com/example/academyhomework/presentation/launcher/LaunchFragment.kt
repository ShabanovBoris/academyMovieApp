package com.example.academyhomework.presentation.launcher

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import com.example.academyhomework.Router
import com.example.academyhomework.databinding.FragmentLaunchBinding
import com.example.academyhomework.presentation.BaseFragment
import com.example.academyhomework.presentation.MainViewModel
import com.example.academyhomework.presentation.ViewModelFactory
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
    }


    override fun onDetach() {
        super.onDetach()
        _binding = null
    }
}