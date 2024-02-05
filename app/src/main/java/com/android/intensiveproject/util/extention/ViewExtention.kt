package com.android.intensiveproject.util.extention

import android.animation.ObjectAnimator
import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.moveWithAnimation(value: Float) {
    ObjectAnimator.ofFloat(this, "translationY", value.dpToPx()).apply {
        duration = 500
        start()
    }
}