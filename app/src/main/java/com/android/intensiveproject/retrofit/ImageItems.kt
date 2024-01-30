package com.android.intensiveproject.retrofit

import com.google.gson.annotations.SerializedName


data class ImageItems(val documents: MutableList<ImageItemDetail>?)

data class ImageItemDetail(
    val collection: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val datetime: String,
    @SerializedName("display_sitename")
    val siteName: String,
    @SerializedName("doc_url")
    val webPageUrl: String,
    val width: Int,
    val height: Int
)