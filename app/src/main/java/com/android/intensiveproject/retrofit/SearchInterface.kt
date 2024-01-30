package com.android.intensiveproject.retrofit

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface SearchInterface {
    @GET("v2/search/image")
    suspend fun getImageItems(
        @Header("Authorization") key: String,
        @QueryMap params: HashMap<String, Any>
    ): ImageItems
}