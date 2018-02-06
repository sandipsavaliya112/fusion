package com.awesome.layouts

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Build
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout


class AwesomeDiagonal : FrameLayout {

    internal var settings: AwesomeDiagonalSettings? = null

    internal var height = 0

    internal var width = 0

    lateinit var clipPath: Path
    lateinit var outlinePath: Path

    lateinit var paint: Paint

    internal var defaultMargin_forPosition: Int? = null

    private var pdMode: PorterDuffXfermode? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet?) {
        settings = AwesomeDiagonalSettings(context, attrs)
        settings!!.elevation = ViewCompat.getElevation(this)

        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE

        pdMode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun setBackgroundColor(color: Int) {
        paint.color = color
        postInvalidate()
    }

    fun setPosition(@AwesomeDiagonalSettings.Position position: Int) {
        settings!!.position = position
        postInvalidate()
    }

    fun setDirection(@AwesomeDiagonalSettings.Direction direction: Int) {
        settings!!.direction = direction
        postInvalidate()
    }

    fun setAngle(angle: Float) {
        settings!!.angle = angle
        calculateLayout()
        postInvalidate()
    }

    private fun calculateLayout() {
        if (settings == null) {
            return
        }
        height = measuredHeight
        width = measuredWidth
        if (width > 0 && height > 0) {

            val perpendicularHeight = (width * Math.tan(Math.toRadians(settings!!.angle.toDouble()))).toFloat()

            clipPath = createClipPath(perpendicularHeight)
            outlinePath = createOutlinePath(perpendicularHeight)

            handleMargins(perpendicularHeight)

            ViewCompat.setElevation(this, settings!!.elevation)

            //this needs to be fixed for 25.4.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && ViewCompat.getElevation(this) > 0f) {
                try {
                    outlineProvider = outlineProvider
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun createClipPath(perpendicularHeight: Float): Path {
        val path = Path()
        when (settings!!.position) {
            AwesomeDiagonalSettings.BOTTOM -> if (settings!!.isDirectionLeft) {
                path.moveTo(width - paddingRight + EPSILON, height.toFloat() - perpendicularHeight - paddingBottom.toFloat() + EPSILON)
                path.lineTo(width - paddingRight + EPSILON, height - paddingBottom + EPSILON)
                path.lineTo(paddingLeft - EPSILON, height - paddingBottom + EPSILON)
                path.close()
            } else {
                path.moveTo(width - paddingRight + EPSILON, height - paddingBottom + EPSILON)
                path.lineTo(paddingLeft - EPSILON, height - paddingBottom + EPSILON)
                path.lineTo(paddingLeft - EPSILON, height.toFloat() - perpendicularHeight - paddingBottom.toFloat() + EPSILON)
                path.close()
            }
            AwesomeDiagonalSettings.TOP -> if (settings!!.isDirectionLeft) {
                path.moveTo(width - paddingRight + EPSILON, paddingTop + perpendicularHeight - EPSILON)
                path.lineTo(paddingLeft - EPSILON, paddingTop - EPSILON)
                path.lineTo(width - paddingRight + EPSILON, paddingTop - EPSILON)
                path.close()
            } else {
                path.moveTo(width - paddingRight + EPSILON, paddingTop - EPSILON)
                path.lineTo(paddingLeft - EPSILON, paddingTop + perpendicularHeight - EPSILON)
                path.lineTo(paddingLeft - EPSILON, paddingTop - EPSILON)
                path.close()
            }
            AwesomeDiagonalSettings.RIGHT -> if (settings!!.isDirectionLeft) {
                path.moveTo(width - paddingRight + EPSILON, paddingTop - EPSILON)
                path.lineTo(width - paddingRight + EPSILON, height - paddingBottom + EPSILON)
                path.lineTo(width.toFloat() - perpendicularHeight - paddingRight.toFloat() + EPSILON, height - paddingBottom + EPSILON)
                path.close()
            } else {
                path.moveTo(width.toFloat() - perpendicularHeight - paddingRight.toFloat() - EPSILON, paddingTop - EPSILON)
                path.lineTo(width - paddingRight + EPSILON, paddingTop - EPSILON)
                path.lineTo(width - paddingRight + EPSILON, height - paddingBottom + EPSILON)
                path.close()
            }
            AwesomeDiagonalSettings.LEFT -> if (settings!!.isDirectionLeft) {
                path.moveTo(paddingLeft - EPSILON, paddingTop - EPSILON)
                path.lineTo(paddingLeft.toFloat() + perpendicularHeight + EPSILON, paddingTop - EPSILON)
                path.lineTo(paddingLeft - EPSILON, height - paddingBottom + EPSILON)
                path.close()
            } else {
                path.moveTo(paddingLeft - EPSILON, paddingTop - EPSILON)
                path.lineTo(paddingLeft.toFloat() + perpendicularHeight + EPSILON, height - paddingBottom + EPSILON)
                path.lineTo(paddingLeft - EPSILON, height - paddingBottom + EPSILON)
                path.close()
            }
        }
        return path
    }

    private fun createOutlinePath(perpendicularHeight: Float): Path {
        val path = Path()
        when (settings!!.direction) {
            AwesomeDiagonalSettings.BOTTOM -> if (settings!!.isDirectionLeft) {
                path.moveTo(paddingLeft.toFloat(), paddingRight.toFloat())
                path.lineTo((width - paddingRight).toFloat(), paddingTop.toFloat())
                path.lineTo((width - paddingRight).toFloat(), height.toFloat() - perpendicularHeight - paddingBottom.toFloat())
                path.lineTo(paddingLeft.toFloat(), (height - paddingBottom).toFloat())
                path.close()
            } else {
                path.moveTo((width - paddingRight).toFloat(), (height - paddingBottom).toFloat())
                path.lineTo(paddingLeft.toFloat(), height.toFloat() - perpendicularHeight - paddingBottom.toFloat())
                path.lineTo(paddingLeft.toFloat(), paddingTop.toFloat())
                path.lineTo((width - paddingRight).toFloat(), paddingTop.toFloat())
                path.close()
            }
            AwesomeDiagonalSettings.TOP -> if (settings!!.isDirectionLeft) {
                path.moveTo((width - paddingRight).toFloat(), (height - paddingBottom).toFloat())
                path.lineTo((width - paddingRight).toFloat(), paddingTop + perpendicularHeight)
                path.lineTo(paddingLeft.toFloat(), paddingTop.toFloat())
                path.lineTo(paddingLeft.toFloat(), (height - paddingBottom).toFloat())
                path.close()
            } else {
                path.moveTo((width - paddingRight).toFloat(), (height - paddingBottom).toFloat())
                path.lineTo((width - paddingRight).toFloat(), paddingTop.toFloat())
                path.lineTo(paddingLeft.toFloat(), paddingTop + perpendicularHeight)
                path.lineTo(paddingLeft.toFloat(), (height - paddingBottom).toFloat())
                path.close()
            }
            AwesomeDiagonalSettings.RIGHT -> if (settings!!.isDirectionLeft) {
                path.moveTo(paddingLeft.toFloat(), paddingTop.toFloat())
                path.lineTo((width - paddingRight).toFloat(), paddingTop.toFloat())
                path.lineTo(width.toFloat() - paddingRight.toFloat() - perpendicularHeight, (height - paddingBottom).toFloat())
                path.lineTo(paddingLeft.toFloat(), (height - paddingBottom).toFloat())
                path.close()
            } else {
                path.moveTo(paddingLeft.toFloat(), paddingTop.toFloat())
                path.lineTo(width.toFloat() - paddingRight.toFloat() - perpendicularHeight, paddingTop.toFloat())
                path.lineTo((width - paddingRight).toFloat(), (height - paddingBottom).toFloat())
                path.lineTo(paddingLeft.toFloat(), (height - paddingBottom).toFloat())
                path.close()
            }
            AwesomeDiagonalSettings.LEFT -> if (settings!!.isDirectionLeft) {
                path.moveTo(paddingLeft + perpendicularHeight, paddingTop.toFloat())
                path.lineTo((width - paddingRight).toFloat(), paddingTop.toFloat())
                path.lineTo((width - paddingRight).toFloat(), (height - paddingBottom).toFloat())
                path.lineTo(paddingLeft.toFloat(), (height - paddingBottom).toFloat())
                path.close()
            } else {
                path.moveTo(paddingLeft.toFloat(), paddingTop.toFloat())
                path.lineTo((width - paddingRight).toFloat(), paddingTop.toFloat())
                path.lineTo((width - paddingRight).toFloat(), (height - paddingBottom).toFloat())
                path.lineTo(paddingLeft + perpendicularHeight, (height - paddingBottom).toFloat())
                path.close()
            }
        }
        return path
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getOutlineProvider(): ViewOutlineProvider {
        return object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setConvexPath(outlinePath)
            }
        }
    }

    private fun handleMargins(perpendicularHeight: Float) {
        if (settings!!.isHandleMargins) {
            val layoutParams = layoutParams
            if (layoutParams is ViewGroup.MarginLayoutParams) {

                when (settings!!.direction) {
                    AwesomeDiagonalSettings.BOTTOM -> {
                        if (defaultMargin_forPosition == null) {
                            defaultMargin_forPosition = layoutParams.bottomMargin
                        } else {
                            defaultMargin_forPosition = 0
                        }
                        layoutParams.bottomMargin = (defaultMargin_forPosition!! - perpendicularHeight).toInt()
                    }
                    AwesomeDiagonalSettings.TOP -> {
                        if (defaultMargin_forPosition == null) {
                            defaultMargin_forPosition = layoutParams.topMargin
                        } else {
                            defaultMargin_forPosition = 0
                        }
                        layoutParams.topMargin = (defaultMargin_forPosition!! - perpendicularHeight).toInt()
                    }
                }

                setLayoutParams(layoutParams)
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            calculateLayout()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (!isInEditMode) {
            val saveCount = canvas.saveLayer(0f, 0f, getWidth().toFloat(), getHeight().toFloat(), null, Canvas.ALL_SAVE_FLAG)

            super.dispatchDraw(canvas)

            paint.xfermode = pdMode
            canvas.drawPath(clipPath, paint)

            canvas.restoreToCount(saveCount)
            paint.xfermode = null
        } else {
            super.dispatchDraw(canvas)
        }
    }

    companion object {

        private val EPSILON = 0.5f
    }
}
