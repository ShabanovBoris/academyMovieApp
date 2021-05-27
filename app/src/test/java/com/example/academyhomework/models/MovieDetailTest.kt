package com.example.academyhomework.models

import com.example.academyhomework.entities.Actor
import com.example.academyhomework.entities.Genre
import com.example.academyhomework.entities.MovieDetails

data class MovieDetailsTest(
    val id:Int = 0,
    val title:String = "",
    val overview:String = "",
    val runtime:Int =0,
    val imageBackdrop:String= "",
    val genres:List<Genre> = listOf(),
    val actors:List<Actor> = listOf(),
    val votes:Double = 0.0
)