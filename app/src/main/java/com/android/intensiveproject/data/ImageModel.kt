package com.android.intensiveproject.data

import kotlinx.serialization.Serializable

@Serializable
data class ImageModel(
    val url: String ="",
    val width: Int = 0,
    val height: Int = 0,
    var engine: Int = -1,
    var keyword: String = "",
    var favorite: Boolean = false
) {
    fun toggleFavorite() {
        favorite = favorite.not()
    }
}