package com.android.intensiveproject.data.datasource.remote

import com.android.intensiveproject.BuildConfig
import com.android.intensiveproject.data.model.ImageModel
import com.android.intensiveproject.domain.data.remote.ImageRDS
import com.android.intensiveproject.domain.usecase.CreateClientForLoggingUseCase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap
import javax.inject.Inject

const val KAKAO = 1

class Kakao @Inject constructor() : ImageRDS {
    override suspend fun getByKeyword(keyword: String): List<ImageModel> {
        val client = KakaoImageSearchClient().retrofitClient
        val response = client.getImageItems(
            BuildConfig.KAKAO_API_KEY,
            makeRequestParam(keyword, 40, 1)
        ).documents

        return response.map {
            ImageModel(
                it.imageUrl,
                it.width,
                it.height,
                KAKAO,
                keyword
            )
        }
    }

    private fun makeRequestParam(query: String, size: Int, page: Int): HashMap<String, Any> {
        return hashMapOf(
            "query" to query,
            "size" to size,
            "page" to page
        )
    }

    class KakaoImageSearchClient {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        private val baseURL = "https://dapi.kakao.com/"
        private val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(CreateClientForLoggingUseCase())
            .build()
        val retrofitClient: KakaoImageSearchAPI = retrofit.create(KakaoImageSearchAPI::class.java)
    }

    interface KakaoImageSearchAPI {
        @GET("v2/search/image")
        suspend fun getImageItems(
            @Header("Authorization") key: String,
            @QueryMap params: HashMap<String, Any>
        ): KakaoImageResponse
    }
}

@Serializable
data class KakaoImageResponse(
    val documents: List<Document>
)

@Serializable
data class Document(
    @SerialName("image_url")
    val imageUrl: String,
    val width: Int,
    val height: Int,
    @SerialName("display_sitename")
    val displaySitename: String,
    val datetime: String
)