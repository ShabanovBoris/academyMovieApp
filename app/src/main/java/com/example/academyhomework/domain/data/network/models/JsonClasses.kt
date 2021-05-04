package com.example.academyhomework.domain.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseClass(
    @SerialName("dates")
    val dates: Dates? = null,
    @SerialName("page")
    val page: Int? = null,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("results")
    val results: List<JsonMovie>,
    @SerialName("total_results")
    val totalResults: Int? = null
)

@Serializable
data class Dates(
    @SerialName("maximum")
    val maximum: String? = null,
    @SerialName("minimum")
    val minimum: String? = null
)

@Serializable
data class ResponseGenreClass(
    val genres: List<JsonGenre>
)

@Serializable
data class ConfigurationInfoClass(
    val images: ListOfConfigs
)

@Serializable
data class ListOfConfigs(
    val base_url: String?,
    val secure_base_url: String?,
    val backdrop_sizes: List<String?>?,
    val logo_sizes: List<String?>?,
    val poster_sizes: List<String?>?,
    val profile_sizes: List<String?>?,
    val still_sizes: List<String?>?,
)