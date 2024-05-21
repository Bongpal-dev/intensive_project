package com.android.intensiveproject.domain.usecase

import com.android.intensiveproject.data.model.ImageModel
import com.android.intensiveproject.domain.repository.ImageRepository
import javax.inject.Inject

class AddImageToFavoritesUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    operator fun invoke(image: ImageModel) {
        imageRepository.addToFavorites(image)
    }
}