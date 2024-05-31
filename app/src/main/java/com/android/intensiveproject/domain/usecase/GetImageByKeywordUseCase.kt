package com.android.intensiveproject.domain.usecase

import com.android.intensiveproject.data.model.ImageModel
import com.android.intensiveproject.domain.repository.ImageRepository
import javax.inject.Inject

class GetImageByKeywordUseCase @Inject constructor(
    private val imageRepo: ImageRepository
) {
    suspend operator fun invoke(query: String): List<ImageModel> {
        return imageRepo.getByQuery(query)
    }
}