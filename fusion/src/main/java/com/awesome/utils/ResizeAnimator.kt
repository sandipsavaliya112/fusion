package com.awesome.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

import com.awesome.R
import com.awesome.layouts.AwesomeDiagonal

/**
 * Created by Sandip Savaliya on 23/11/17.
 */

class ResizeAnimator(internal var view: View, internal val targetWidth: Int, internal val targetHeight: Int, isDiagonal: Boolean) : Animation() {
    internal val startWidth: Int
    internal val startHeight: Int
    internal var isDiagonal = false
    internal var angle = 15f

    init {
        startWidth = view.width
        startHeight = view.height
        this.isDiagonal = isDiagonal
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val newWidth = (startWidth + (targetWidth - startWidth) * interpolatedTime).toInt()
        val newHeight = (startHeight + (targetHeight - startHeight) * interpolatedTime).toInt()
        if (targetWidth != 0) {
            view.layoutParams.width = newWidth
        }
        if (targetHeight != 0) {
            if (isDiagonal) {
                if (view.id == R.id.vSwitcherDiagonal) {
                    angle -= 0.35f
                    (view as AwesomeDiagonal).setAngle(angle)
                }
            }
            view.layoutParams.height = newHeight
        }
        view.requestLayout()
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
    }

    override fun willChangeBounds(): Boolean {
        return true
    }
}