package com.android.intensiveproject.domain

import com.android.intensiveproject.data.ImageModel

interface ImageRDS {
    suspend fun getByKeyword(keyword: String): List<ImageModel>
}