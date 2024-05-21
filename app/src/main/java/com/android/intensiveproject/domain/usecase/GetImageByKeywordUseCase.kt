package com.android.intensiveproject.domain.usecase

import com.android.intensiveproject.data.ImageModel
import com.android.intensiveproject.domain.ImageRepository
import javax.inject.Inject

class GetImageByKeywordUseCase @Inject constructor(
    private val imageRepo: ImageRepository
) {
    suspend operator fun invoke(keyword: String): List<ImageModel> {
        return imageRepo.getByKeyword(keyword)
    }
}