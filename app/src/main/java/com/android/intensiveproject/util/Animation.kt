package com.android.intensiveproject.util

import android.animation.ValueAnimator
import android.view.animation.AnimationUtils
import com.android.intensiveproject.view.mainactivity.MyApp

object Animation {
    val fadeIn = AnimationUtils.loadAnimation(MyApp.appContext, com.google.android.material.R.anim.abc_fade_in)
    val fadeOut = AnimationUtils.loadAnimation(MyApp.appContext, com.google.android.material.R.anim.abc_fade_out)

    fun setDuration() {
        ValueAnimator.ofFloat(0f, 1f).setDuration(2400)
    }
}