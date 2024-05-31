package com.android.intensiveproject.domain.data.remote

import com.android.intensiveproject.data.model.ImageModel

interface ImageRDS {
    suspend fun getByQuery(query: String): List<ImageModel>
}