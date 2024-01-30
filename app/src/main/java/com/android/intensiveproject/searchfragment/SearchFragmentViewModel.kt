package com.android.intensiveproject.searchfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.intensiveproject.model.retrofit.ImageItemDetail
import com.android.intensiveproject.model.retrofit.SearchClient
import kotlinx.coroutines.launch
import kotlin.random.Random

class SearchFragmentViewModel : ViewModel() {
    private var _searchResult = MutableLiveData<MutableList<ImageItemDetail>>()
    private var _toastMsg = MutableLiveData<String>()
    val searchResult: LiveData<MutableList<ImageItemDetail>> = _searchResult
    val toastMsg: LiveData<String> = _toastMsg

    fun textIsEmpty(msg: String) {
        _toastMsg.value = msg
    }

    fun getDataFromAPI(keyWord: String) {
        keyWord.getItemFromAPI()
    }

    private fun String.getItemFromAPI() {
        val API_KEY = "aa9dcfa994967af44de93c594a380bab"

        viewModelScope.launch {
            val resultImages = mutableListOf<ImageItemDetail>()

            makeRequestParam(this@getItemFromAPI).let {
                SearchClient.retrofitClient.getImageItems("KakaoAK ${API_KEY}", it)
            }.documents?.forEach {
                resultImages += it
            }
            _searchResult.value = resultImages
        }
    }

    private fun makeRequestParam(query: String): HashMap<String, Any> {
        var page = Random.nextInt(1, 51)
        return hashMapOf(
            "query" to query,
            "page" to page,
            "size" to 80,
        )
    }
}
