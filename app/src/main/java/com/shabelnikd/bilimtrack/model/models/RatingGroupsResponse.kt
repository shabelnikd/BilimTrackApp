package com.shabelnikd.bilimtrack.model.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Wrap with List<RatingGroupsResponse>
@Serializable
data class RatingGroupsResponse(
    val isTopWinnerPosition: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("points")
    val points: Int? = null
)
