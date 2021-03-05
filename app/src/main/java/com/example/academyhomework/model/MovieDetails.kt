package com.example.academyhomework.model

import com.example.academyhomework.domain.data.JsonGenre
import java.io.Serializable

data class MovieDetails(
    val title:String,
    val overview:String,
    val runtime:Int,
    val imageBackdrop:String,
    val genres:List<Genre>,
    val actors:List<Actor>
):Serializable