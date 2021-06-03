package com.example.academyhomework

import android.view.View
import com.example.academyhomework.entities.MovieDetails

interface Router {
    fun moveToDetails(movie: MovieDetails)
    fun backFromDetails()
    /**
     * @transitView for animate transition to DetailsFragment screen
     */
    var transitView: View?
    fun openWebPage(movieId: Int):Boolean
}