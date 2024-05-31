package com.android.intensiveproject.domain.repository

import com.android.intensiveproject.data.model.ImageModel

interface ImageRepository {
    suspend fun getByQuery(query: String): List<ImageModel>

    fun loadFavorites(): List<ImageModel>

    fun addToFavorites(image: ImageModel)

    fun removeFromFavorites(image: ImageModel)
}