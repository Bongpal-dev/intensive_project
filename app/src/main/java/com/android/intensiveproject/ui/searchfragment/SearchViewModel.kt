package com.android.intensiveproject.ui.searchfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.intensiveproject.data.ImageModel
import com.android.intensiveproject.domain.ImageFacade
import dagger.hilt.android.lifecycle.HiltViewModel
//import com.android.intensiveproject.data.Contents
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val imageFacade: ImageFacade
) : ViewModel() {
    private var _searchResult = MutableLiveData<List<ImageModel>>()
    private var _toastMsg = MutableLiveData<String>()
    private var _scrollBtnState = MutableLiveData<Boolean>()
    val searchResult: LiveData<List<ImageModel>> get() = _searchResult
    val toastMsg: LiveData<String> get() = _toastMsg
    val scrollBtnState: LiveData<Boolean> get() = _scrollBtnState


    fun searchImage(query: String) {
        viewModelScope.launch {
            _searchResult.value = imageFacade.getImagesByKeyword(query)
        }
    }

    fun addItemWithFavoriteList(imageModel: ImageModel) {
        viewModelScope.launch {
            imageFacade.setFavoriteImage(imageModel)
        }
    }

    fun textIsEmpty(msg: String) {
        _toastMsg.value = msg
    }

    private var page = 1

    fun showScrollUpButton(state: Boolean) {
        _scrollBtnState.value = state
    }
}
