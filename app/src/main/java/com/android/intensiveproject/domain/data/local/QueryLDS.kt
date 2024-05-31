package com.android.intensiveproject.domain.data.local

interface QueryLDS {
    fun updatePreviousQueries(query: String)

    fun clearPreviousQueries()
}