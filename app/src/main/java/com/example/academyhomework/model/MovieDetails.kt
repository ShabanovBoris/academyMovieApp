package com.example.academyhomework.model

import java.io.Serializable

data class MovieDetails(
    val title:String,
    val overview:String,
    val runtime:Int,
    val imageBackdrop:String,
    val genres:List<Genre>,
    val actors:List<Actor>
):Serializable