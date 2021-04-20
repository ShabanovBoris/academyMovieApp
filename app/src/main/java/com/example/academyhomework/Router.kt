package com.example.academyhomework

import android.view.View
import com.example.academyhomework.model.MovieDetails

interface Router {
    fun moveToDetails(movie: MovieDetails)
    fun backFromDetails()
    /**
     * [transitView] for animate transition
     */
    var transitView: View?
    fun openWebPage(movieId: Int):Boolean
}