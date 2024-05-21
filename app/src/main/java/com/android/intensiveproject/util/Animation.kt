package com.android.intensiveproject.util

import android.animation.ValueAnimator
import android.view.animation.AnimationUtils
import com.android.intensiveproject.domain.MyApplication

object Animation {
    val fadeIn = AnimationUtils.loadAnimation(MyApplication.appContext, com.google.android.material.R.anim.abc_fade_in)
    val fadeOut = AnimationUtils.loadAnimation(MyApplication.appContext, com.google.android.material.R.anim.abc_fade_out)

    fun setDuration() {
        ValueAnimator.ofFloat(0f, 1f).setDuration(2400)
    }
}