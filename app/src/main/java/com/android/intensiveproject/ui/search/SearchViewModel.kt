package com.android.intensiveproject.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.intensiveproject.data.model.ImageModel
import com.android.intensiveproject.domain.facade.SearchFacade
import dagger.hilt.android.lifecycle.HiltViewModel
//import com.android.intensiveproject.data.Contents
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchFacade: SearchFacade,
) : ViewModel() {
    private var _searchResult = MutableLiveData<List<ImageModel>>()
    val searchResult: LiveData<List<ImageModel>> get() = _searchResult

    private var _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String> get() = _toastMsg

    private var _scrollBtnState = MutableLiveData<Boolean>()
    val scrollBtnState: LiveData<Boolean> get() = _scrollBtnState


    fun searchImage(query: String) {
        viewModelScope.launch {
            _searchResult.value = searchFacade.getImagesByKeyword(query)
        }
    }

    fun showToast(msg: String) {
        _toastMsg.value = msg
    }

    private var page = 1

    fun showScrollUpButton(state: Boolean) {
        _scrollBtnState.value = state
    }
}
