package com.example.academyhomework.models

data class MovieCard(
    val imageId:Int,
    val title:String,
    val genre:String,
    val watchTime:Int,
    val revueAmount:Int,
    val ratingValue:Int = 0

)