package com.example.academyhomework

import com.example.academyhomework.model.MovieDetails

interface Router {
    fun moveToDetails(movie: MovieDetails)
    fun backFromDetails()
}