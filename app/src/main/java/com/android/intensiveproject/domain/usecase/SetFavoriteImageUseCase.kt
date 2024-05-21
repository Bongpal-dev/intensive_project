package com.android.intensiveproject.domain.usecase

import com.android.intensiveproject.data.ImageModel
import com.android.intensiveproject.domain.ImageRepository
import javax.inject.Inject

class SetFavoriteImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    operator fun invoke(image: ImageModel) {
        imageRepository.setFavorite(image)
    }
}