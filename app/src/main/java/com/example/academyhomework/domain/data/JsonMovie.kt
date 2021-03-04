package com.example.academyhomework.domain.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class JsonMovieNew(
	@SerialName("overview")
	val overview: String? = null,
	@SerialName("original_language")
	val originalLanguage: String? = null,
	@SerialName("original_title")
	val originalTitle: String? = null,
	@SerialName("video")
	val video: Boolean? = null,
	@SerialName("title")
	val title: String? = null,
	@SerialName("genre_ids")
	val genreIds: List<Int?>? = null,
	@SerialName("poster_path")
	val posterPath: String? = null,
	@SerialName("backdrop_path")
	val backdropPath: String? = null,
	@SerialName("release_date")
	val releaseDate: String? = null,
	@SerialName("popularity")
	val popularity: Double? = null,
	@SerialName("vote_average")
	val voteAverage: Double? = null,
	@SerialName("id")
	val id: Int? = null,
	@SerialName("adult")
	val adult: Boolean? = null,
	@SerialName("vote_count")
	val voteCount: Int? = null
)



@Deprecated("Old class")
@Serializable
internal class JsonMovie(
	val id: Int,
	val title: String,
	@SerialName("poster_path")
	val posterPicture: String,
	@SerialName("backdrop_path")
	val backdropPicture: String,
	val runtime: Int,
	@SerialName("genre_ids")
	val genreIds: List<Int>,
	val actors: List<Int>,
	@SerialName("vote_average")
	val ratings: Float,
	@SerialName("vote_count")
	val votesCount: Int,
	val overview: String,
	val adult: Boolean
)