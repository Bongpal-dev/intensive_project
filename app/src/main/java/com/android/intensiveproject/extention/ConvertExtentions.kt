package com.android.intensiveproject.extention

import android.util.TypedValue
import com.android.intensiveproject.MyApp

fun Int.dpToPx(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        MyApp.appContext.resources.displayMetrics
    ).toInt()
}

fun Float.dpToPx(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        MyApp.appContext.resources.displayMetrics
    )
}