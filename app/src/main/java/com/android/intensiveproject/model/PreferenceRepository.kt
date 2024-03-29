package com.android.intensiveproject.model

import android.content.Context
import com.android.intensiveproject.MyApp
import com.android.intensiveproject.data.Contents
import com.android.intensiveproject.data.Contents.*
import com.google.gson.Gson

class PreferenceRepository {
    private val MY_IMAGE = "MyImage"
    private val MY_VIDEO = "MYImage"
    private val SEARCH_KEYWORD = "SearchKeyword"
    private val imagePref = MyApp.appContext.getSharedPreferences(MY_IMAGE, Context.MODE_PRIVATE)
    private val videoPref = MyApp.appContext.getSharedPreferences(MY_VIDEO, Context.MODE_PRIVATE)
    private val searchKeywordPref =
        MyApp.appContext.getSharedPreferences(SEARCH_KEYWORD, Context.MODE_PRIVATE)
    private val gson = Gson()


    fun getAll(): MutableList<Contents> {
        var items = mutableListOf<Contents>()

        imagePref.all.values.forEach {
            items += gson.fromJson(it.toString(), ImageItem::class.java)
        }
        videoPref.all.values.forEach {
            items += gson.fromJson(it.toString(), VideoItem::class.java)
        }
        return items
    }

    private fun removeItem(item: Contents) {
        when (item) {
            is ImageItem -> {
                imagePref.edit()
                    .remove(item.imageUrl)
                    .apply()
            }

            is VideoItem -> {
                videoPref.edit()
                    .remove(item.imageUrl)
                    .apply()
            }
        }
    }

    private fun addItem(item: Contents) {
        when (item) {
            is ImageItem -> {
                imagePref.edit()
                    .putString(item.imageUrl, gson.toJson(item))
                    .apply()
            }

            is VideoItem -> {
                videoPref.edit()
                    .putString(item.imageUrl, gson.toJson(item))
                    .apply()
            }
        }
    }

    fun toggleItemInPref(item: Contents) {
        val allPrefs = getAll()

        if (allPrefs.contains(item)) {
            removeItem(item)
            return
        }
        addItem(item)
    }

    fun setSearchKeyword(keyword: String) {
        searchKeywordPref.edit().run {
            putString(SEARCH_KEYWORD, keyword)
            apply()
        }
    }

    fun getSearchKeyword(): String {
        return searchKeywordPref.getString(SEARCH_KEYWORD, "") ?: ""
    }
}