package com.shabelnikd.bilimtrack.model.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Uses with wrap List<SubjectsMeResponse>
@Serializable
data class SubjectsMeResponse(
    @SerialName("description")
    val description: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("photo")
    val photo: String? = null,
    @SerialName("makalaboxUrl")
    val makalaBoxUrl: String? = null
)
