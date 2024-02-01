package com.android.intensiveproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.intensiveproject.data.Contents
import com.android.intensiveproject.model.PreferenceRepository

class MainViewModel : ViewModel() {
    private val pref = PreferenceRepository()
    private var _myStorages = MutableLiveData<MutableList<Contents>>()
    private var _toolBarState = MutableLiveData<Boolean>()
    val toolBarState: LiveData<Boolean> get() = _toolBarState
    val myStorages: LiveData<MutableList<Contents>> get() = _myStorages

    fun togglePreferenceItem(item: Contents) {
        pref.toggleItemInPref(item)
        _myStorages.value = pref.getAll()
        Log.i(TAG, "getAllsize: ${pref.getAll().size}")
        myStorages.value?.forEach { Log.i(TAG, "myStorage: ${it}") }

    }

    fun getAllPrefItems(): MutableList<Contents> {
        return pref.getAll()
    }

    fun setSearchKeyword(keyword: String) {
        pref.setSearchKeyword(keyword)
    }

    fun getSearchKeyword(): String {
        return pref.getSearchKeyword()

    }

    fun showToolBar(show: Boolean) {
        _toolBarState.value = show
    }
}