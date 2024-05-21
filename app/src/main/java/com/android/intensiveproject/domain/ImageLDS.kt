package com.android.intensiveproject.domain

import com.android.intensiveproject.data.ImageModel

interface ImageLDS {
    fun setFavoriteImage(image: ImageModel)

    fun removeFavoriteImage(image: ImageModel)

    fun getFavoriteImages(): List<ImageModel>
}