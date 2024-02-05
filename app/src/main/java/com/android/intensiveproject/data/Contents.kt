package com.android.intensiveproject.data

import com.google.gson.annotations.SerializedName

sealed class Contents {
    data class ImageItems(val documents: MutableList<ImageItem>?)

    data class ImageItem(
        val collection: String,
        @SerializedName("image_url")
        val imageUrl: String,
        val datetime: String,
        @SerializedName("display_sitename")
        val siteName: String,
        @SerializedName("doc_url")
        val webPageUrl: String,
        val width: Int,
        val height: Int,
        var keyword: String
    ) : Contents()

    data class VideoItems(val documents: MutableList<VideoItem>?)

    data class VideoItem(
        @SerializedName("thumbnail")
        val imageUrl: String,
        val datetime: String,
        val title: String,
        var keyword: String
    ) : Contents()
}
