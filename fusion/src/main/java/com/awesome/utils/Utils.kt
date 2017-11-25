package com.awesome.utils

import android.animation.TimeInterpolator
import android.support.annotation.IntRange
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator

object Utils {

    val ACCELERATE_DECELERATE_INTERPOLATOR = 0
    val ACCELERATE_INTERPOLATOR = 1
    val ANTICIPATE_INTERPOLATOR = 2
    val ANTICIPATE_OVERSHOOT_INTERPOLATOR = 3
    val BOUNCE_INTERPOLATOR = 4
    val DECELERATE_INTERPOLATOR = 5
    val FAST_OUT_LINEAR_IN_INTERPOLATOR = 6
    val FAST_OUT_SLOW_IN_INTERPOLATOR = 7
    val LINEAR_INTERPOLATOR = 8
    val LINEAR_OUT_SLOW_IN_INTERPOLATOR = 9
    val OVERSHOOT_INTERPOLATOR = 10

    /**
     * Creates interpolator.
     *
     * @param interpolatorType
     * @return
     */
    fun createInterpolator(@IntRange(from = 0, to = 10) interpolatorType: Int): TimeInterpolator {
        when (interpolatorType) {
            ACCELERATE_DECELERATE_INTERPOLATOR -> return AccelerateDecelerateInterpolator()
            ACCELERATE_INTERPOLATOR -> return AccelerateInterpolator()
            ANTICIPATE_INTERPOLATOR -> return AnticipateInterpolator()
            ANTICIPATE_OVERSHOOT_INTERPOLATOR -> return AnticipateOvershootInterpolator()
            BOUNCE_INTERPOLATOR -> return BounceInterpolator()
            DECELERATE_INTERPOLATOR -> return DecelerateInterpolator()
            FAST_OUT_LINEAR_IN_INTERPOLATOR -> return FastOutLinearInInterpolator()
            FAST_OUT_SLOW_IN_INTERPOLATOR -> return FastOutSlowInInterpolator()
            LINEAR_INTERPOLATOR -> return LinearInterpolator()
            LINEAR_OUT_SLOW_IN_INTERPOLATOR -> return LinearOutSlowInInterpolator()
            OVERSHOOT_INTERPOLATOR -> return OvershootInterpolator()
            else -> return LinearInterpolator()
        }
    }
}