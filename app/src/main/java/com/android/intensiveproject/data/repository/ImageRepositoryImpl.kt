package com.android.intensiveproject.data.repository

import com.android.intensiveproject.data.model.ImageModel
import com.android.intensiveproject.domain.data.local.ImageLDS
import com.android.intensiveproject.domain.data.remote.ImageRDS
import com.android.intensiveproject.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class ImageRepositoryImpl @Inject constructor(
    @Named("kakao") val kakaoApi: ImageRDS,
    @Named("naver") val naverApi: ImageRDS,
    private val imageLDS: ImageLDS
) : ImageRepository {
    override suspend fun getByQuery(query: String): List<ImageModel> =
        withContext(Dispatchers.IO) {
            val apis = listOf<suspend () -> List<ImageModel>>(
                { kakaoApi.getByQuery(query) },
                { naverApi.getByQuery(query) }
            )
            val deferredResults = apis.map { async { it() } }

            return@withContext deferredResults.awaitAll().flatten().shuffled()
        }

    override fun loadFavorites(): List<ImageModel> {
        return imageLDS.getFavoriteImages()
    }

    override fun addToFavorites(image: ImageModel) {
        imageLDS.setFavoriteImage(image.toggleFavorite())
    }

    override fun removeFromFavorites(image: ImageModel) {
        imageLDS.removeFavoriteImage(image.toggleFavorite())
    }
}