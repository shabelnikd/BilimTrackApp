package com.shabelnikd.bilimtrack.model.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenRefreshResponse(
    @SerialName("access")
    val access: String
)