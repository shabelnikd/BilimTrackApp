package com.shabelnikd.bilimtrack.model.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenCreateResponse(
    @SerialName("access")
    val access: String,
    @SerialName("refresh")
    val refresh: String
)