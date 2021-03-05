package com.example.academyhomework

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.academyhomework.model.moviedetails.ActorRecyclerAdapter
import com.example.academyhomework.model.MovieDetails

private const val MOVIE_KEY = "movie_param"

class FragmentMoviesDetails : BaseFragment() {

    private var mListener: Router? = null
    private lateinit var mRecyclerView:RecyclerView

    private var mMovie: MovieDetails? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is Router){
            mListener = context
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            mMovie = it.getSerializable(MOVIE_KEY) as MovieDetails?

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

    private fun setViewFragmentMovie(view: View) {
        mMovie?.let{
        val movie = mMovie as MovieDetails
        val image = view.findViewById<ImageView>(R.id.iv_main_screen)
        val title = view.findViewById<TextView>(R.id.tv_main_title)
        val genre = view.findViewById<TextView>(R.id.genre)
        val rating = view.findViewById<RatingBar>(R.id.rating_bar)
        val tvRating = view.findViewById<TextView>(R.id.tv_rating)
        val story = view.findViewById<TextView>(R.id.tv_story)
        val timeRun = view.findViewById<TextView>(R.id.tv_running_time)


        image.load(movie.imageBackdrop) {
            crossfade(true)
            placeholder(R.drawable.ic_loading_image)
        }
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
        title.text = movie.title
        for (g in movie.genres) {
            genre.append(g.name + " ")
        }
//        rating.rating = movie.rating.toFloat()
//        tvRating.text = movie.rating.toString() + " by ${movie.reviewCount} review"
        story.text = movie.overview

        timeRun.text = "${movie.runtime} min"
    }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRecyclerView = view.findViewById(R.id.actor_recycler)
        var adapter = ActorRecyclerAdapter()
        adapter.bindActors((mMovie as MovieDetails).actors)
        mRecyclerView.adapter = adapter

        val back = view.findViewById<TextView>(R.id.backButton)
        back.setOnClickListener{
            mListener?.backFromDetails()
        }

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