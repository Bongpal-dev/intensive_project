package com.android.intensiveproject.domain.usecase

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RetrofitUsecase(private val baseURL: String) {
    val contentType = "application/json".toMediaType()
    val json = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(OkHttpUsecase.createClient())
        .build()

    fun <T> create(service: Class<T>): T = retrofit.create(service)
}