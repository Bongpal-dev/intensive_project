package com.android.intensiveproject.model

import android.app.Application
import android.content.Context
import com.android.intensiveproject.searchfragment.MY_FAVORITE
import com.android.intensiveproject.model.retrofit.ImageItemDetail
import com.google.gson.Gson

class PreferenceRepository(private val context: Context) : Application() {
    private val pref = context.getSharedPreferences(MY_FAVORITE, Context.MODE_PRIVATE)
    private val prefEditor = pref.edit()
    private val gson = Gson()

    fun getAll(): MutableList<ImageItemDetail> {
        var items = mutableListOf<ImageItemDetail>()

        pref.all.values.forEach {
            items += gson.fromJson(it.toString(), ImageItemDetail::class.java)
        }
        return items
    }

    fun togglePref(item: ImageItemDetail) {
        val allPrefs = getAll()

        if (allPrefs.contains(item)) {
            prefEditor.run {
                remove(item.imageUrl)
                apply()
            }
        } else {
            prefEditor.run {
                putString(item.imageUrl, gson.toJson(item))
                apply()
            }
        }
    }
}