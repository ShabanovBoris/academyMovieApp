package com.example.academyhomework.model

import com.example.academyhomework.model.Actor
import com.example.academyhomework.model.Genre
import java.io.Serializable

data class Movie(
    val id: Int,
    val pgAge: Int? = null,
    val title: String,
    val genres: List<Genre>,
    val reviewCount: Int,
    val isLiked: Boolean? = null,
    val rating: Double,
    val imageUrl: String,
    val detailImageUrl: String,
    val storyLine: String,
    val releaseDate:String

) :Serializable