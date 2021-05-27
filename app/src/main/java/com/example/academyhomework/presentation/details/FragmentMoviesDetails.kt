package com.example.academyhomework.presentation.details

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.example.academyhomework.MovieApp
import com.example.academyhomework.R
import com.example.academyhomework.Router
import com.example.academyhomework.presentation.adapters.ActorRecyclerAdapter
import com.example.academyhomework.entities.MovieDetails
import com.example.academyhomework.presentation.BaseFragment
import com.example.academyhomework.services.db_update_work_manager.NotificationsNewMovie
import com.example.academyhomework.utils.setUpViewFragment
import com.example.academyhomework.utils.startFadeAnimation
import com.google.android.material.transition.MaterialContainerTransform


class FragmentMoviesDetails : BaseFragment() {

    private var mMovie: MovieDetails? = null
    private var router: Router? = null
    private lateinit var mRecyclerView: RecyclerView


    private val viewModel: DetailsViewModel by viewModels {
        (requireActivity().application as MovieApp).appComponent
            .plusDetailsComponent().create()
            .detailsVMFactory()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Router) {
            router = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mMovie = it.getSerializable(MOVIE_KEY) as MovieDetails?
        }
        NotificationsNewMovie.dismissNotification(requireContext(), mMovie?.id ?: 0)

        /**
         * Animate settings
         */
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.containerMainActivity
            duration = 1000
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(Color.TRANSPARENT)
            startContainerColor = Color.TRANSPARENT

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movies_details, container, false)

        setUpViewFragment(view, mMovie) { movieId ->
            viewModel.openWebPage(movieId, router!!)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecycler(view)
        viewModel.openSate.observe(viewLifecycleOwner, { onWebOpenHandler(it) })
    }

    private fun onWebOpenHandler(result: WebResult) {
        when (result) {
            WebResult.Fail -> Toast.makeText(
                requireContext(),
                "Open web page failed",
                Toast.LENGTH_LONG
            ).show()
            WebResult.Success -> {
            }
        }
    }



    private fun setRecycler(view: View) {
        mRecyclerView = view.findViewById(R.id.actor_recycler)
        val adapter = ActorRecyclerAdapter()
        adapter.bindActors((mMovie as MovieDetails).actors)
        mRecyclerView.adapter = adapter
        /** Handle click on Back Button*/
        val back = view.findViewById<TextView>(R.id.backButton)
        back.setOnClickListener {
            router?.backFromDetails()
        }
    }

    override fun onStart() {
        super.onStart()
        startFadeAnimation()
    }


    override fun onDetach() {
        super.onDetach()
        mMovie = null
        router = null
    }

    companion object {

        private const val MOVIE_KEY = "movie_param"

        @JvmStatic
        fun newInstance(movie: MovieDetails?) =
            FragmentMoviesDetails().apply {
                arguments = Bundle().apply {
                    putSerializable(MOVIE_KEY, movie)
                }
            }
    }
}