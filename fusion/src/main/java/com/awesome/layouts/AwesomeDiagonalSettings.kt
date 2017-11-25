package com.awesome.layouts

import android.content.Context
import android.support.annotation.IntDef
import android.util.AttributeSet

import com.awesome.R

import java.lang.annotation.Retention

import java.lang.annotation.RetentionPolicy.SOURCE

class AwesomeDiagonalSettings internal constructor(context: Context, attrs: AttributeSet?) {

    var angle = 15f
    var elevation: Float = 0.toFloat()
    var position = BOTTOM
    var direction = DIRECTION_LEFT
    var isHandleMargins: Boolean = false

    val isDirectionLeft: Boolean
        get() = direction == DIRECTION_LEFT

    @Retention(SOURCE)
    @IntDef(LEFT.toLong(), RIGHT.toLong(), BOTTOM.toLong(), TOP.toLong())
    annotation class Position

    @Retention(SOURCE)
    @IntDef(DIRECTION_LEFT.toLong(), DIRECTION_RIGHT.toLong())
    annotation class Direction


    init {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.AwesomeDiagonal, 0, 0)
        try {
            angle = styledAttributes.getInt(R.styleable.AwesomeDiagonal_diagonal_angle, 0).toFloat()
            position = styledAttributes.getInt(R.styleable.AwesomeDiagonal_diagonal_position, BOTTOM)
            isHandleMargins = styledAttributes.getBoolean(R.styleable.AwesomeDiagonal_diagonal_handleMargins, false)
            direction = styledAttributes.getInt(R.styleable.AwesomeDiagonal_diagonal_direction, DIRECTION_LEFT)
        } finally {
            styledAttributes.recycle()
        }
    }

    companion object {

        const val LEFT = 1
        const val RIGHT = 2
        const val BOTTOM = 4
        const val TOP = 8

        const val DIRECTION_LEFT = 1
        const val DIRECTION_RIGHT = 2
    }
}
