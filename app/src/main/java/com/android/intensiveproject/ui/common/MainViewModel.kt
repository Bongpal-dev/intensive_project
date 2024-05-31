package com.android.intensiveproject.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.intensiveproject.data.model.ImageModel
import com.android.intensiveproject.domain.facade.FavoriteFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val favoriteFacade: FavoriteFacade
) : ViewModel() {
    private var _favorites = MutableLiveData<List<ImageModel>>()
    val favorites: LiveData<List<ImageModel>> get() = _favorites

    private var _toolBarState = MutableLiveData<Boolean>()
    val toolBarState: LiveData<Boolean> get() = _toolBarState

    fun loadFavoriteImages() {
        _favorites.value = favoriteFacade.loadFavoriteImages()
    }

    fun toggleFavoriteImage(imageModel: ImageModel) {
        viewModelScope.launch {
            if(imageModel.favorite) {
                favoriteFacade.removeImageFromFavorites(imageModel)
            } else {
                favoriteFacade.addImageToFavorites(imageModel)
            }
            loadFavoriteImages()
        }
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
