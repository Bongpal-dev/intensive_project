package com.android.intensiveproject.view.searchfragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.intensiveproject.view.mainactivity.TAG
import com.android.intensiveproject.data.Contents
import com.android.intensiveproject.model.retrofit.SearchClient
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private var _searchResult = MutableLiveData<MutableList<Contents>>()
    private var _toastMsg = MutableLiveData<String>()
    private var _scrollBtnState = MutableLiveData<Boolean>()
    private val NEW_RESULT = 0
    private val MORE_RESULT = 1
    val searchResult: LiveData<MutableList<Contents>> get() = _searchResult
    val toastMsg: LiveData<String> get() = _toastMsg
    val scrollBtnState: LiveData<Boolean> get() = _scrollBtnState
    val searchClient = SearchClient.retrofitClient

    fun textIsEmpty(msg: String) {
        _toastMsg.value = msg
    }

    fun getSearchResults(keyWord: String) {
        keyWord.getItemFromAPI(NEW_RESULT, keyWord)
    }

    fun getMoreResult(keyWord: String) {
        keyWord.getItemFromAPI(MORE_RESULT, keyWord)
    }

    private var page = 1

    private fun String.getItemFromAPI(resultState: Int, keyWord: String) {
        val API_KEY = "KakaoAK aa9dcfa994967af44de93c594a380bab"

        if (resultState == NEW_RESULT) page = 1 else page++
        Log.i(TAG, "page: ${page}")
        viewModelScope.launch {
            var results = mutableListOf<Contents>()
            val imageParams = makeRequestParam(this@getItemFromAPI, 70, page)
            val videoParams = makeRequestParam(this@getItemFromAPI, 10, page)

            with(searchClient) {
                getImageItems(API_KEY, imageParams).documents?.forEach {
                    it.keyword = keyWord
                    results += it
                }
                getVidioItems(API_KEY, videoParams).documents?.forEach {
                    it.keyword = keyWord
                    results += it
                }
            }
            results = results.sortedByDescending {
                when (it) {
                    is Contents.ImageItem -> it.datetime
                    is Contents.VideoItem -> it.datetime
                }
            }.toMutableList()
            when (resultState) {
                NEW_RESULT -> _searchResult.value = results
                MORE_RESULT -> _searchResult.postValue((searchResult.value?.plus(results))?.toMutableList()
                    ?: results)
            }
        }
    }

    private fun makeRequestParam(query: String, size: Int, page: Int): HashMap<String, Any> {
        return hashMapOf(
            "query" to query,
            "size" to size,
            "page" to page
        )
    }

    fun showScrollUpButton(state: Boolean) {
        _scrollBtnState.value = state
    }
}
