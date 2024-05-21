package com.android.intensiveproject.domain.usecase

import com.android.intensiveproject.domain.repository.ImageRepository
import javax.inject.Inject

class LoadFavoriteImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    operator fun invoke() = imageRepository.loadFavorites()
}