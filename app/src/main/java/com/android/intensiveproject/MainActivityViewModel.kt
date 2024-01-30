package com.android.intensiveproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.intensiveproject.model.PreferenceRepository
import com.android.intensiveproject.model.retrofit.ImageItemDetail

class MainActivityViewModel : ViewModel() {
    private var _MyStorages = MutableLiveData<MutableList<ImageItemDetail>>()
    val MyStorages: LiveData<MutableList<ImageItemDetail>> = _MyStorages

    fun togglePreferenceItem(pref: PreferenceRepository, item: ImageItemDetail) {
        pref.togglePref(item)
        _MyStorages.value = pref.getAll()
    }
}