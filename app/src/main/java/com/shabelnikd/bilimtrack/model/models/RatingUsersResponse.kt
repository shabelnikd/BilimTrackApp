package com.shabelnikd.bilimtrack.model.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Wrap with List<RatingUsersResponse>
@Serializable
data class RatingUsersResponse(
    var isTopWinnerPosition: Int? = null,
    @SerialName("firstName")
    val firstName: String? = null,
    @SerialName("lastName")
    val lastName: String? = null,
    @SerialName("photo")
    val photo: String? = null,
    @SerialName("points")
    val points: Int? = null,
    @SerialName("rating")
    val rating: Int? = null,
    @SerialName("username")
    val username: String? = null
)
