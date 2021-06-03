package com.example.academyhomework.utils

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import coil.load
import com.example.academyhomework.R
import com.example.academyhomework.entities.MovieDetails
import com.example.academyhomework.view.BaseFragment
import com.example.academyhomework.view.details.FragmentMoviesDetails
import com.example.academyhomework.services.schedule_movie_work_manager.ScheduleMovieTimePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FragmentMoviesDetails.startFadeAnimation() {
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

fun FragmentMoviesDetails.setUpViewFragment(
    view: View,
    mMovie: MovieDetails?,
    onClickWebPage: (Int) -> Unit
) {
    mMovie?.let { movie ->
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
            target (
                onError = {
                    startPostponedEnterTransition()
                },
                onSuccess = { result ->
                image.setImageDrawable(result)
                startPostponedEnterTransition()
            })
            crossfade(true)
            error(R.drawable.black_gradient)
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
        /** set Scheduler to fab listener*/
        fab.setOnClickListener { scheduleMovie(this, movie.id) }
        /** Image button to web page*/
        moreInfoButton.setOnClickListener { onClickWebPage(movie.id) }
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

private fun scheduleMovie(fragment: BaseFragment, movieId: Int) {
    ScheduleMovieTimePicker.schedule(fragment, movieId)
}

