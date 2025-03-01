package com.shabelnikd.bilimtrack.model.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class AchievementsResponse(
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("isOpened")
    val isOpened: Boolean? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("photo")
    val photo: String? = null,
    @SerialName("rarity")
    val rarity: Rarity? = null
) : Parcelable {
    @Parcelize
    @Serializable
    data class Rarity(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("name")
        val name: String? = null
    ) : Parcelable
}