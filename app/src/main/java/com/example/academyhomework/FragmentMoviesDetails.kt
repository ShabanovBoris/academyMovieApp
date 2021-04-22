package com.example.academyhomework

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.academyhomework.model.MovieDetails
import com.example.academyhomework.model.moviedetails.ActorRecyclerAdapter
import com.example.academyhomework.services.db_update.NotificationsNewMovie
import com.example.academyhomework.services.schedule_watch.WatchMovieSchedule
import com.example.academyhomework.utils.DatePickerFragment
import com.example.academyhomework.utils.TimePickerFragment
import com.example.academyhomework.viewmodel.DetailsViewModel
import com.example.academyhomework.viewmodel.WebResult
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.transition.MaterialContainerTransform

private const val MOVIE_KEY = "movie_param"

class FragmentMoviesDetails : BaseFragment() {


    private var mListener: Router? = null
    private lateinit var mRecyclerView: RecyclerView
    private var mMovie: MovieDetails? = null

    private val viewModel: DetailsViewModel by viewModels{DetailsViewModel.Factory(mListener!!)}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Router) {
            mListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mMovie = it.getSerializable(MOVIE_KEY) as MovieDetails?
        }
        NotificationsNewMovie.dismissNotification(requireContext(),mMovie?.id ?:0)

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
        setViewFragmentMovie(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecycler(view)
        viewModel.openSate.observe(viewLifecycleOwner, { onWebOpenHandler(it) })
    }

    private fun onWebOpenHandler(result: WebResult){
        when(result){
            WebResult.Fail -> Toast.makeText(requireContext(),"Open web page failed",Toast.LENGTH_LONG).show()
            WebResult.Success -> {}
        }
    }

    private fun setRecycler(view: View) {
        mRecyclerView = view.findViewById(R.id.actor_recycler)
        var adapter = ActorRecyclerAdapter()
        adapter.bindActors((mMovie as MovieDetails).actors)
        mRecyclerView.adapter = adapter
        /** Handle click on Back Button*/
        val back = view.findViewById<TextView>(R.id.backButton)
        back.setOnClickListener {
            mListener?.backFromDetails()
        }
    }

    override fun onResume() {
        super.onResume()
        enterFadeAnimate()
    }

    private fun enterFadeAnimate() {
        requireActivity().findViewById<TextView>(R.id.tv_main_title)
            .animate().setDuration(1500L).alpha(1F)
            .setStartDelay(1200)
            .start()

        requireActivity().findViewById<FloatingActionButton>(R.id.fb_schedule)
            .animate().setDuration(1500L).alpha(1F)
            .setStartDelay(1200)
            .start()

        requireActivity().findViewById<TextView>(R.id.tv_running_time)
            .animate().setDuration(1500L).alpha(1F)
            .setStartDelay(1200)
            .start()

        requireActivity().findViewById<ImageView>(R.id.iv_tint_gradient)
            .animate().setDuration(1500L).alpha(1F)
            .setStartDelay(1000)
            .start()

    }

    private fun setViewFragmentMovie(view: View) {
        mMovie?.let {
            val movie = mMovie as MovieDetails
            val image = view.findViewById<ImageView>(R.id.iv_main_screen)
            val title = view.findViewById<TextView>(R.id.tv_main_title)
            val genre = view.findViewById<TextView>(R.id.genre)
            val rating = view.findViewById<RatingBar>(R.id.rating_bar)
            val tvRating = view.findViewById<TextView>(R.id.tv_rating)
            val story = view.findViewById<TextView>(R.id.tv_story)
            val timeRun = view.findViewById<TextView>(R.id.tv_running_time)
            val fab = view.findViewById<FloatingActionButton>(R.id.fb_schedule)
            val moreInfoButton = view.findViewById<ImageView>(R.id.ib_web_info)
            /** Bacjdorp image*/
            image.load(movie.imageBackdrop) {
                crossfade(true)
                placeholder(R.drawable.ic_loading_image)
            }
            setFilter(image)
            /** title*/
            title.text = movie.title
            /** genres*/
            for (g in movie.genres) {
                genre.append(g.name + " ")
            }
            /** rating info*/
            rating.rating = movie.votes.toFloat()
            tvRating.text = movie.votes.toString()
            /**  story line */
            story.text = movie.overview
            /** time run in min*/
            timeRun.text = "${movie.runtime} min"
            /** Schedule fab listener*/
            fab.setOnClickListener { scheduleMovie(movie.id) }
            /** Image button to web page*/
            moreInfoButton.setOnClickListener { viewModel.openWebPage(movie.id) }
        }
    }

    private fun setFilter(image: ImageView) {
        val matrix = ColorMatrix().apply {
            set(
                floatArrayOf(
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }
        image.colorFilter = ColorMatrixColorFilter(matrix)
    }

    @SuppressLint("NewApi")
    private fun scheduleMovie(id: Int) {
        /**     in UI
         * --->[Date] picking first
         * ---> [Time] picking second
         * ---> finally call in [Time] callback[WatchMovieSchedule]*/
        var date = DatePickerFragment(requireContext())
        TimePickerFragment().apply {
            setAfterDoneAction {
                WatchMovieSchedule(
                    appContext = requireContext(),
                    movieId = id,
                    time = this,
                    date = date
                ).start()
            }
        }.show(requireActivity().supportFragmentManager, "tag")

        date = date.showWithClass(
            requireActivity().supportFragmentManager,
            "tag"
        ) as DatePickerFragment


    }


    override fun onDetach() {
        super.onDetach()
        mMovie = null
        mListener = null
    }

    companion object {

        @JvmStatic
        fun newInstance(movie: MovieDetails?) =
            FragmentMoviesDetails().apply {
                arguments = Bundle().apply {
                    putSerializable(MOVIE_KEY, movie)
                }
            }
    }
}