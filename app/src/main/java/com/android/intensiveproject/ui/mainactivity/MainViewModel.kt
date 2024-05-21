package com.android.intensiveproject.ui.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import com.android.intensiveproject.data.Contents
import com.android.intensiveproject.model.PreferenceRepository

class MainViewModel : ViewModel() {
    private val pref = PreferenceRepository()
    private var _toolBarState = MutableLiveData<Boolean>()
//    private var _myStorages = MutableLiveData<MutableList<Contents>>()
//    private var _searchResult = MutableLiveData<MutableList<Contents>>()
    val toolBarState: LiveData<Boolean> get() = _toolBarState
//    val myStorages: LiveData<MutableList<Contents>> get() = _myStorages
//    val searchResult: LiveData<MutableList<Contents>> get() = _searchResult

//    fun togglePreferenceItem(item: Contents) {
//        pref.toggleItemInPref(item)
//        _myStorages.value = pref.getAll()
//    }

//    fun getAllPrefItems(): MutableList<Contents> {
//        return pref.getAll()
//    }

    fun setSearchKeyword(keyword: String) {
        pref.setSearchKeyword(keyword)
    }

    fun getSearchKeyword(): String {
        return pref.getSearchKeyword()

    }

    fun showToolBar(show: Boolean) {
        _toolBarState.value = show
    }

//    fun filterWithKeyword(searchKeyword: String) {
//        var filter = mutableListOf<Contents>()
//
//        pref.getAll().forEach { content ->
//            when (content) {
//                is Contents.ImageItem -> {
//                    if (content.keyword.contains(searchKeyword)) {
//                        filter += content
//                    }
//                }
//
//                is Contents.VideoItem -> {
//                    if (content.keyword.contains(searchKeyword)) {
//                        filter += content
//                    }
//                }
//            }
//        }
//        _searchResult.value = filter
//    }
}
