package com.android.intensiveproject.domain.data.local

import com.android.intensiveproject.data.model.ImageModel

interface ImageLDS {
    fun setFavoriteImage(image: ImageModel)

    fun removeFavoriteImage(image: ImageModel)

    fun getFavoriteImages(): List<ImageModel>
}