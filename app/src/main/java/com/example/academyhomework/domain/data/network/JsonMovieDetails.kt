package com.example.academyhomework.domain.data.network

import com.example.academyhomework.domain.data.network.JsonGenre
import kotlinx.serialization.Serializable

@Serializable
data class JsonMovieDetails (
    val id:Int,
    val original_title:String,
    val overview:String?,
    val runtime:Int?,
    val backdrop_path:String?,
    val genres:List<JsonGenre>,
)