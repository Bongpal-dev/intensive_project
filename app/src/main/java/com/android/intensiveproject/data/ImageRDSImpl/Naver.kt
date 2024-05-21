package com.android.intensiveproject.data.ImageRDSImpl

import com.android.intensiveproject.BuildConfig
import com.android.intensiveproject.data.ImageModel
import com.android.intensiveproject.domain.ImageRDS
import com.android.intensiveproject.domain.usecase.OkHttpUsecase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.QueryMap
import javax.inject.Inject

const val NAVER = 2

class Naver @Inject constructor() : ImageRDS {
    override suspend fun getByKeyword(keyword: String): List<ImageModel> {
        val client = NaverImageSearchClient().retrofitClient
        val response = client.getImageItems(
            makeClientInfo(),
            makeRequestParam(keyword, 40, 1)
        ).items

        return response.map {
            ImageModel(
                it.link,
                it.sizewidth.toInt(),
                it.sizeheight.toInt(),
                NAVER,
                keyword
            )
        }
    }

    private fun makeClientInfo(): HashMap<String, String> {
        return hashMapOf(
            "X-Naver-Client-Id" to BuildConfig.NAVER_CLIENT_ID,
            "X-Naver-Client-Secret" to BuildConfig.NAVER_CLIENT_SECRET
        )
    }

    private fun makeRequestParam(query: String, size: Int, page: Int): HashMap<String, Any> {
        return hashMapOf(
            "query" to query,
            "display" to size,
            "start" to page
        )
    }

    class NaverImageSearchClient {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        private val baseURL = "https://openapi.naver.com/"
        private val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(OkHttpUsecase.createClient())
            .build()
        val retrofitClient: NaverImageSearchAPI = retrofit.create(NaverImageSearchAPI::class.java)
    }

    interface NaverImageSearchAPI {
        @GET("v1/search/image")
        suspend fun getImageItems(
            @HeaderMap clientInfo: HashMap<String, String>,
            @QueryMap params: HashMap<String, Any>
        ): NaverImageResponse
    }
}

@Serializable
data class NaverImageResponse(
    val items: List<Item>
)

@Serializable
data class Item(
    val title: String,
    val link: String,
    val sizeheight: String,
    val sizewidth: String
)