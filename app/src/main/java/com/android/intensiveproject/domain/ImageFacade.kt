package com.android.intensiveproject.domain

import com.android.intensiveproject.data.ImageModel
import com.android.intensiveproject.domain.usecase.GetFavoriteImagesUseCase
import com.android.intensiveproject.domain.usecase.GetImageByKeywordUseCase
import com.android.intensiveproject.domain.usecase.RemoveFavoriteImageUseCase
import com.android.intensiveproject.domain.usecase.SetFavoriteImageUseCase
import javax.inject.Inject

class ImageFacade @Inject constructor(
    private val getImageByKeywordUseCase: GetImageByKeywordUseCase,
    private val getFavoriteImagesUseCase: GetFavoriteImagesUseCase,
    private val setFavoriteImageUseCase: SetFavoriteImageUseCase,
    private val removeFavoriteImageUseCase: RemoveFavoriteImageUseCase
) {
    suspend fun getImagesByKeyword(keyword: String) = getImageByKeywordUseCase(keyword)

    fun getFavoriteImages() = getFavoriteImagesUseCase()

    fun setFavoriteImage(image: ImageModel) {
        setFavoriteImageUseCase(image)
    }

    fun removeFavoriteImage(image: ImageModel) {
        removeFavoriteImageUseCase(image)
    }
}