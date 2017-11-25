package com.awesome.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.awesome.R

class AwesomeColorLine(context: Context, attrs: AttributeSet) : View(context, attrs) {

    internal var colors: IntArray

    private val paint: Paint
    private val rect = Rect()

    // indicate if nothing selected
    internal var isColorSelected = false

    // @Override
    // protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
    // int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
    // this.setMeasuredDimension(parentWidth, parentHeight);
    // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    // }

    /**
     * Return currently selected color.
     */
    var color: Int = 0
        get() = field
        set(value) {
            field = value
        }

    private var onColorChanged: OnColorChangedListener? = null

    private var cellSize: Int = 0

    private var mOrientation = HORIZONTAL

    private var isClick = false
    private var screenW: Int = 0
    private var screenH: Int = 0

    init {
        if (isInEditMode) {
            colors = Palette.DEFAULT
        } else {
            colors = IntArray(1)
        }
    }

    init {

        paint = Paint()
        paint.style = Style.FILL

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.AwesomeColorLine, 0, 0)

        try {
            mOrientation = a.getInteger(R.styleable.AwesomeColorLine_orientation, HORIZONTAL)

            if (!isInEditMode) {
                val colorsArrayResId = a.getResourceId(R.styleable.AwesomeColorLine_colors, -1)

                if (colorsArrayResId > 0) {
                    val colors = context.resources.getIntArray(colorsArrayResId)
                    setColors(colors)
                }
            }

            val selected = a.getInteger(R.styleable.AwesomeColorLine_selectedColorIndex, -1)

            if (selected != -1) {
                val currentColors = getColors()

                val currentColorsLength = currentColors?.size ?: 0

                if (selected < currentColorsLength) {
                    setSelectedColorPosition(selected)
                }
            }
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mOrientation == HORIZONTAL) {
            drawHorizontalPicker(canvas)
        } else {
            drawVerticalPicker(canvas)
        }

    }

    private fun drawVerticalPicker(canvas: Canvas) {
        rect.left = 0
        rect.top = 0
        rect.right = canvas.width
        rect.bottom = 0

        // 8%
        val margin = Math.round(canvas.width * 0.08f)

        for (i in colors.indices) {

            paint.color = colors[i]

            rect.top = rect.bottom
            rect.bottom += cellSize

            if (isColorSelected && colors[i] == color) {
                rect.left = 0
                rect.right = canvas.width
            } else {
                rect.left = margin
                rect.right = canvas.width - margin
            }

            canvas.drawRect(rect, paint)
        }

    }

    private fun drawHorizontalPicker(canvas: Canvas) {
        rect.left = 5
        rect.top = 0
        rect.right = 0
        rect.bottom = canvas.height

        // 8%
        val margin = Math.round(canvas.height * 0.08f)

        for (i in colors.indices) {

            paint.color = colors[i]

            rect.left = rect.right
            rect.right += cellSize

            if (isColorSelected && colors[i] == color) {
                rect.top = 0
                rect.bottom = canvas.height
            } else {
                rect.top = 0
                rect.bottom = canvas.height - margin
            }

            canvas.drawRect(rect, paint)
        }
    }

    private fun onColorChanged(color: Int) {
        if (onColorChanged != null) {
            onColorChanged!!.onColorChanged(color)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val actionId = event.action

        val newColor: Int

        when (actionId) {
            MotionEvent.ACTION_DOWN -> isClick = true
            MotionEvent.ACTION_UP -> {
                newColor = getColorAtXY(event.x, event.y)

                setSelectedColor(newColor)

                if (isClick) {
                    performClick()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                newColor = getColorAtXY(event.x, event.y)

                setSelectedColor(newColor)
            }
            MotionEvent.ACTION_CANCEL -> isClick = false

            MotionEvent.ACTION_OUTSIDE -> isClick = false

            else -> {
            }
        }

        return true
    }

    /**
     * Return color at x,y coordinate of view.
     */
    private fun getColorAtXY(x: Float, y: Float): Int {

        // FIXME: colors.length == 0 -> devision by ZERO.s

        if (mOrientation == HORIZONTAL) {
            var left = 0
            var right = 0

            for (i in colors.indices) {
                left = right
                right += cellSize

                if (left <= x && right >= x) {
                    return colors[i]
                }
            }

        } else {
            var top = 0
            var bottom = 0

            for (i in colors.indices) {
                top = bottom
                bottom += cellSize

                if (y >= top && y <= bottom) {
                    return colors[i]
                }
            }
        }

        return color
    }

    override fun onSaveInstanceState(): Parcelable? {
        // begin boilerplate code that allows parent classes to save state
        val superState = super.onSaveInstanceState()

        val ss = SavedState(superState)
        // end

        ss.selectedColor = this.color
        ss.isColorSelected = this.isColorSelected

        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        // begin boilerplate code so parent classes can restore state
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superState)
        // end

        this.color = state.selectedColor
        this.isColorSelected = state.isColorSelected
    }

    internal class SavedState : View.BaseSavedState {
        var selectedColor: Int = 0
        var isColorSelected: Boolean = false

        constructor(superState: Parcelable) : super(superState) {}

        private constructor(`in`: Parcel) : super(`in`) {
            this.selectedColor = `in`.readInt()
            this.isColorSelected = `in`.readInt() == 1
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(this.selectedColor)
            out.writeInt(if (this.isColorSelected) 1 else 0)
        }

        companion object {

            // required field that makes Parcelables from a Parcel
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        screenW = w
        screenH = h

        recalcCellSize()

        super.onSizeChanged(w, h, oldw, oldh)
    }

    /**
     * Set selected color as color value from palette.
     */
    fun setSelectedColor(color: Int) {

        // not from current palette
        if (!containsColor(colors, color)) {
            return
        }

        // do we need to re-draw view?
        if (!isColorSelected || this.color != color) {
            this.color = color

            isColorSelected = true

            invalidate()

            onColorChanged(color)
        }
    }

    /**
     * Set selected color as index from palete
     */
    fun setSelectedColorPosition(position: Int) {
        setSelectedColor(colors[position])
    }

    /**
     * Set picker palette
     */
    fun setColors(colors: IntArray) {
        // TODO: selected color can be NOT in set of colors
        // FIXME: colors can be null
        this.colors = colors

        if (!containsColor(colors, color)) {
            color = colors[0]
        }

        recalcCellSize()

        invalidate()
    }

    private fun recalcCellSize(): Int {

        if (mOrientation == HORIZONTAL) {
            cellSize = Math.round((screenW + 10) / (colors.size * 1f))
        } else {
            cellSize = Math.round(screenH / (colors.size * 1f))
        }

        return cellSize
    }

    /**
     * Return current picker palete
     */
    fun getColors(): IntArray? {
        return colors
    }

    /**
     * Return true if palette contains this color
     */
    private fun containsColor(colors: IntArray, c: Int): Boolean {
        for (i in colors.indices) {
            if (colors[i] == c)
                return true

        }

        return false
    }

    /**
     * Set onColorChanged listener
     *
     * @param l
     */
    fun setOnColorChangedListener(l: OnColorChangedListener) {
        this.onColorChanged = l
    }

    companion object {

        val HORIZONTAL = 0
        val VERTICAL = 1
    }
}
