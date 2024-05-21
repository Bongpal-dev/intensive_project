package com.android.intensiveproject.util.extention

import android.util.TypedValue
import com.android.intensiveproject.domain.MyApplication

fun Int.dpToPx(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        MyApplication.appContext.resources.displayMetrics
    ).toInt()
}

fun Float.dpToPx(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        MyApplication.appContext.resources.displayMetrics
    )
}