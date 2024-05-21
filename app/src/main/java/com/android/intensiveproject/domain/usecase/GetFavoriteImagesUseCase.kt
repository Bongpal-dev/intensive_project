package com.android.intensiveproject.domain.usecase

import com.android.intensiveproject.domain.ImageRepository
import javax.inject.Inject

class GetFavoriteImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    operator fun invoke() = imageRepository.getFavorite()
}