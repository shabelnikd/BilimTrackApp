package com.shabelnikd.bilimtrack.model.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("achievementsCount")
    val achievementsCount: Int? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("firstName")
    val firstName: String? = null,
    @SerialName("group")
    val group: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("lastName")
    val lastName: String? = null,
    @SerialName("photo")
    val photo: String? = null,
    @SerialName("points")
    val points: Int? = null,
    @SerialName("rating")
    val rating: Int? = null,
    @SerialName("role")
    val role: String? = null,
    @SerialName("username")
    val username: String? = null
)
