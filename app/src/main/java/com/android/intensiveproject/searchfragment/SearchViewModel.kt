package com.android.intensiveproject.searchfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.intensiveproject.data.Contents
import com.android.intensiveproject.model.retrofit.SearchClient
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private var _searchResult = MutableLiveData<MutableList<Contents>>()
    private var _toastMsg = MutableLiveData<String>()
    val searchResult: LiveData<MutableList<Contents>> get() = _searchResult
    val toastMsg: LiveData<String> get() = _toastMsg
    val searchClient = SearchClient.retrofitClient

    fun textIsEmpty(msg: String) {
        _toastMsg.value = msg
    }

    fun getSearchResults(keyWord: String) {
        keyWord.getItemFromAPI()
    }

    private fun String.getItemFromAPI() {
        val API_KEY = "KakaoAK aa9dcfa994967af44de93c594a380bab"

        viewModelScope.launch {
            val results = mutableListOf<Contents>()
            val imageParams = makeRequestParam(this@getItemFromAPI, 80)
            val videoParams = makeRequestParam(this@getItemFromAPI, 30)

            with(searchClient) {
                getImageItems(API_KEY, imageParams).documents?.forEach {
                    results += it
                }
                getVidioItems(API_KEY, videoParams).documents?.forEach {
                    results += it
                }
            }
            _searchResult.value = results
                .sortedByDescending {
                    when (it) {
                        is Contents.ImageItem -> it.datetime
                        is Contents.VideoItem -> it.datetime
                    }
                }.toMutableList()
        }
    }

    private fun makeRequestParam(query: String, size: Int): HashMap<String, Any> {
        return hashMapOf(
            "query" to query,
            "size" to size,
        )
    }
}
