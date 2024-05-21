package com.android.intensiveproject.data.datasource.local

import android.content.SharedPreferences
import com.android.intensiveproject.data.model.ImageModel
import com.android.intensiveproject.domain.data.local.ImageLDS
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Named

class ImageLDSImpl @Inject constructor(
    @Named("image_prefs") private val imagePref: SharedPreferences
) : ImageLDS {
    override fun setFavoriteImage(image: ImageModel) {
        imagePref.edit()
            .putString(image.url, Json.encodeToString(image))
            .apply()
    }

    override fun removeFavoriteImage(image: ImageModel) {
        imagePref.edit()
            .remove(image.url)
            .apply()
    }

    override fun getFavoriteImages(): List<ImageModel> {
        var items = mutableListOf<ImageModel>()
        val json = Json { ignoreUnknownKeys = true }

        imagePref.all.values.forEach {
            items += json.decodeFromString(it.toString()) ?: ImageModel()
        }
        return items
    }
}