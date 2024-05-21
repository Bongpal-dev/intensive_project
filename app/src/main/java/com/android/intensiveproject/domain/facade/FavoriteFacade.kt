package com.android.intensiveproject.domain.facade

import com.android.intensiveproject.data.model.ImageModel
import com.android.intensiveproject.domain.usecase.LoadFavoriteImagesUseCase
import com.android.intensiveproject.domain.usecase.RemoveImageFavoritesUseCase
import com.android.intensiveproject.domain.usecase.AddImageToFavoritesUseCase
import javax.inject.Inject

class FavoriteFacade @Inject constructor(
    private val loadFavoriteImagesUseCase: LoadFavoriteImagesUseCase,
    private val addImageToFavoritesUseCase: AddImageToFavoritesUseCase,
    private val removeImageFavoritesUseCase: RemoveImageFavoritesUseCase
) {
    fun loadFavoriteImages() = loadFavoriteImagesUseCase()

    fun addImageToFavorites(image: ImageModel) {
        addImageToFavoritesUseCase(image)
    }

    fun removeImageFromFavorites(image: ImageModel) {
        removeImageFavoritesUseCase(image)
    }
}
