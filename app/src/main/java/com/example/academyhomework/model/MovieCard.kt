package com.example.academyhomework.model

data class MovieCard(
    val imageId:Int,
    val title:String,
    val genre:List<String>,
    val watchTime:Int,
    val revueAmount:Int,
    val ratingValue:Int = 0

)