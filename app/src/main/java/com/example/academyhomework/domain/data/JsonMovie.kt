package com.example.academyhomework.domain.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class JsonMovie(
	@SerialName("overview")
	val overview: String,
	@SerialName("title")
	val title: String,
	@SerialName("genre_ids")
	val genreIds: List<Int>,
	@SerialName("poster_path")
	val posterPath: String? = null,
	@SerialName("backdrop_path")
	val backdropPath: String? = null,
	@SerialName("release_date")
	val releaseDate: String,
	@SerialName("vote_average")
	val voteAverage: Double,
	@SerialName("id")
	val id: Int,
	@SerialName("adult")
	val adult: Boolean? = null,
	@SerialName("vote_count")
	val voteCount: Int
)


