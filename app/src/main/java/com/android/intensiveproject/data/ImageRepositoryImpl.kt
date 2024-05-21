package com.android.intensiveproject.data

import android.content.SharedPreferences
import com.android.intensiveproject.domain.ImageLDS
import com.android.intensiveproject.domain.ImageRDS
import com.android.intensiveproject.domain.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject
import javax.inject.Named

class ImageRepositoryImpl @Inject constructor(
    @Named("kakao") val kakaoApi: ImageRDS,
    @Named("naver") val naverApi: ImageRDS,
    private val imageLDS: ImageLDS
) : ImageRepository {
    override suspend fun getByKeyword(keyword: String): List<ImageModel> = withContext(Dispatchers.IO) {
        val apis = listOf<suspend () -> List<ImageModel>>(
            { kakaoApi.getByKeyword(keyword) },
            { naverApi.getByKeyword(keyword) }
        )
        val deferredResults = apis.map { async { it() } }

        return@withContext deferredResults.awaitAll().flatten().shuffled()
    }

    override fun getFavorite(): List<ImageModel> {
        return imageLDS.getFavoriteImages()
    }

    override fun setFavorite(image: ImageModel) {
        image.toggleFavorite()
        imageLDS.setFavoriteImage(image)
    }

    override fun removeFavorite(image: ImageModel) {
        image.toggleFavorite()
        imageLDS.removeFavoriteImage(image)
    }
}