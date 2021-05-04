package com.example.academyhomework.domain.data.network.models

import com.example.academyhomework.domain.data.network.models.JsonGenre
import kotlinx.serialization.Serializable

@Serializable
data class JsonMovieDetails (
    val id:Int,
    val original_title:String,
    val overview:String?,
    val runtime:Int?,
    val backdrop_path:String?,
    val genres:List<JsonGenre>,
    val vote_average:Double
)