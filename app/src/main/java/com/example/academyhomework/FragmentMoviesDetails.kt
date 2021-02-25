package com.example.academyhomework

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academyhomework.adapters.ActorRecyclerAdapter
import com.example.academyhomework.interfaces.ScreenChangeable
import com.example.academyhomework.models.Movie
import java.io.Serializable

private const val MOVIE_KEY = "movie_param"

class FragmentMoviesDetails : Fragment() {

    private var listener:ScreenChangeable? = null
    private var movie: Serializable? = null
    private lateinit var recyclerView:RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ScreenChangeable){
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = it.getSerializable(MOVIE_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view = inflater.inflate(R.layout.fragment_movies_details, container, false)
        setFragmentMovie(view)

        return view
    }

    private fun setFragmentMovie(view: View) {
        val movie = movie as Movie
        val image = view.findViewById<ImageView>(R.id.iv_main_screen)
        val title = view.findViewById<TextView>(R.id.tv_main_title)
        val genre = view.findViewById<TextView>(R.id.genre)
        val rating = view.findViewById<RatingBar>(R.id.ratingBar)
        val tvRating = view.findViewById<TextView>(R.id.tv_rating)
        val story  = view.findViewById<TextView>(R.id.tv_story)
        Glide.with(requireContext())
            .load(movie.detailImageUrl)
            .into(image)
        title.text = movie.title
        for(g in movie.genres){
            genre.append(g.name+" ")
        }
        rating.rating = movie.rating.toFloat()
        tvRating.text = movie.rating.toString() + " ${movie.reviewCount} review"
        story.text = movie.storyLine


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.actor_recycler)
        var adapter = ActorRecyclerAdapter()
        adapter.bindActors((movie as Movie).actors)
        recyclerView.adapter = adapter

        val back = view.findViewById<TextView>(R.id.backButton)
        back.setOnClickListener{
            listener?.backFromDetails()
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(movie:Serializable) =
            FragmentMoviesDetails().apply {
                arguments = Bundle().apply {
                    putSerializable(MOVIE_KEY, movie)
                }
            }
    }
}