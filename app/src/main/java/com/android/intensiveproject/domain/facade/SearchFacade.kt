package com.android.intensiveproject.domain.facade

import com.android.intensiveproject.domain.usecase.GetImageByKeywordUseCase
import javax.inject.Inject

class SearchFacade @Inject constructor(
    private val getImageByQueryUseCase: GetImageByKeywordUseCase
    ) {
    suspend fun getImagesByQuery(query: String) = getImageByQueryUseCase(query)
}