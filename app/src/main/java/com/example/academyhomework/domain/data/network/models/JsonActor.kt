package com.example.academyhomework.domain.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
 class JsonActor(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val profilePicture: String?
)

@Serializable
class CreditResponse(
    val id: Int,
    val cast: List<JsonActor>
)