package com.android.intensiveproject.domain

import com.android.intensiveproject.data.ImageModel

interface ImageRepository {
    suspend fun getByKeyword(keyword: String): List<ImageModel>

    fun getFavorite(): List<ImageModel>

    fun setFavorite(image: ImageModel)

    fun removeFavorite(image: ImageModel)
}