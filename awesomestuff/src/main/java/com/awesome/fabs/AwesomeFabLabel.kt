package com.awesome.fabs

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.Animation
import android.widget.TextView

@SuppressLint("AppCompatCustomView")
class AwesomeFabLabel : TextView {

    private var mShadowRadius: Int = 0
    private var mShadowXOffset: Int = 0
    private var mShadowYOffset: Int = 0
    private var mShadowColor: Int = 0
    private var mBackgroundDrawable: Drawable? = null
    private var mShowShadow = true
    private var mRawWidth: Int = 0
    private var mRawHeight: Int = 0
    private var mColorNormal: Int = 0
    private var mColorPressed: Int = 0
    private var mColorRipple: Int = 0
    private var mCornerRadius: Int = 0
    private var mFab: AwesomeFab? = null
    private var mShowAnimation: Animation? = null
    private var mHideAnimation: Animation? = null
    private var mUsingStyle: Boolean = false
    internal var isHandleVisibilityChanges = true

    internal var mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            onActionDown()
            if (mFab != null) {
                mFab!!.onActionDown()
            }
            return super.onDown(e)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            onActionUp()
            if (mFab != null) {
                mFab!!.onActionUp()
            }
            return super.onSingleTapUp(e)
        }
    })

    constructor(context: Context?) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(calculateMeasuredWidth(), calculateMeasuredHeight())
    }

    private fun calculateMeasuredWidth(): Int {
        if (mRawWidth == 0) {
            mRawWidth = measuredWidth
        }
        return measuredWidth + calculateShadowWidth()
    }

    private fun calculateMeasuredHeight(): Int {
        if (mRawHeight == 0) {
            mRawHeight = measuredHeight
        }
        return measuredHeight + calculateShadowHeight()
    }

    internal fun calculateShadowWidth(): Int {
        return if (mShowShadow) mShadowRadius + Math.abs(mShadowXOffset) else 0
    }

    internal fun calculateShadowHeight(): Int {
        return if (mShowShadow) mShadowRadius + Math.abs(mShadowYOffset) else 0
    }

    internal fun updateBackground() {
        val layerDrawable: LayerDrawable
        if (mShowShadow) {
            layerDrawable = LayerDrawable(arrayOf(Shadow(), createFillDrawable()))

            val leftInset = mShadowRadius + Math.abs(mShadowXOffset)
            val topInset = mShadowRadius + Math.abs(mShadowYOffset)
            val rightInset = mShadowRadius + Math.abs(mShadowXOffset)
            val bottomInset = mShadowRadius + Math.abs(mShadowYOffset)

            layerDrawable.setLayerInset(
                    1,
                    leftInset,
                    topInset,
                    rightInset,
                    bottomInset
            )
        } else {
            layerDrawable = LayerDrawable(arrayOf(createFillDrawable()))
        }

        setBackgroundCompat(layerDrawable)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createFillDrawable(): Drawable {
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(android.R.attr.state_pressed), createRectDrawable(mColorPressed))
        drawable.addState(intArrayOf(), createRectDrawable(mColorNormal))

        if (AwesomeFabUtil.hasLollipop()) {
            val ripple = RippleDrawable(ColorStateList(arrayOf(intArrayOf()),
                    intArrayOf(mColorRipple)), drawable, null)
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setOval(0, 0, view.width, view.height)
                }
            }
            clipToOutline = true
            mBackgroundDrawable = ripple
            return ripple
        }

        mBackgroundDrawable = drawable
        return drawable
    }

    private fun createRectDrawable(color: Int): Drawable {
        val shape = RoundRectShape(
                floatArrayOf(mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat(), mCornerRadius.toFloat()), null, null)
        val shapeDrawable = ShapeDrawable(shape)
        shapeDrawable.paint.color = color
        return shapeDrawable
    }

    private fun setShadow(fab: AwesomeFab) {
        mShadowColor = fab.shadowColor
        mShadowRadius = fab.shadowRadius
        mShadowXOffset = fab.shadowXOffset
        mShadowYOffset = fab.shadowYOffset
        mShowShadow = fab.hasShadow()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setBackgroundCompat(drawable: Drawable) {
        if (AwesomeFabUtil.hasJellyBean()) {
            background = drawable
        } else {
            setBackgroundDrawable(drawable)
        }
    }

    private fun playShowAnimation() {
        if (mShowAnimation != null) {
            mHideAnimation!!.cancel()
            startAnimation(mShowAnimation)
        }
    }

    private fun playHideAnimation() {
        if (mHideAnimation != null) {
            mShowAnimation!!.cancel()
            startAnimation(mHideAnimation)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    internal fun onActionDown() {
        if (mUsingStyle) {
            mBackgroundDrawable = background
        }

        if (mBackgroundDrawable is StateListDrawable) {
            val drawable = mBackgroundDrawable as StateListDrawable?
            drawable!!.state = intArrayOf(android.R.attr.state_pressed)
        } else if (AwesomeFabUtil.hasLollipop() && mBackgroundDrawable is RippleDrawable) {
            val ripple = mBackgroundDrawable as RippleDrawable?
            ripple!!.state = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed)
            ripple.setHotspot((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat())
            ripple.setVisible(true, true)
        }
        //        setPressed(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    internal fun onActionUp() {
        if (mUsingStyle) {
            mBackgroundDrawable = background
        }

        if (mBackgroundDrawable is StateListDrawable) {
            val drawable = mBackgroundDrawable as StateListDrawable?
            drawable!!.state = intArrayOf()
        } else if (AwesomeFabUtil.hasLollipop() && mBackgroundDrawable is RippleDrawable) {
            val ripple = mBackgroundDrawable as RippleDrawable?
            ripple!!.state = intArrayOf()
            ripple.setHotspot((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat())
            ripple.setVisible(true, true)
        }
        //        setPressed(false);
    }

    internal fun setFab(fab: AwesomeFab) {
        mFab = fab
        setShadow(fab)
    }

    internal fun setShowShadow(show: Boolean) {
        mShowShadow = show
    }

    internal fun setCornerRadius(cornerRadius: Int) {
        mCornerRadius = cornerRadius
    }

    internal fun setColors(colorNormal: Int, colorPressed: Int, colorRipple: Int) {
        mColorNormal = colorNormal
        mColorPressed = colorPressed
        mColorRipple = colorRipple
    }

    internal fun show(animate: Boolean) {
        if (animate) {
            playShowAnimation()
        }
        visibility = View.VISIBLE
    }

    internal fun hide(animate: Boolean) {
        if (animate) {
            playHideAnimation()
        }
        visibility = View.INVISIBLE
    }

    internal fun setShowAnimation(showAnimation: Animation) {
        mShowAnimation = showAnimation
    }

    internal fun setHideAnimation(hideAnimation: Animation) {
        mHideAnimation = hideAnimation
    }

    internal fun setUsingStyle(usingStyle: Boolean) {
        mUsingStyle = usingStyle
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mFab == null || mFab!!.getOnClickListener() == null || !mFab!!.isEnabled) {
            return super.onTouchEvent(event)
        }

        val action = event.action
        when (action) {
            MotionEvent.ACTION_UP -> {
                onActionUp()
                mFab!!.onActionUp()
            }

            MotionEvent.ACTION_CANCEL -> {
                onActionUp()
                mFab!!.onActionUp()
            }
        }

        mGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private inner class Shadow internal constructor() : Drawable() {

        private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val mErase = Paint(Paint.ANTI_ALIAS_FLAG)

        init {
            this.init()
        }

        private fun init() {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            mPaint.style = Paint.Style.FILL
            mPaint.color = mColorNormal

            mErase.xfermode = PORTER_DUFF_CLEAR

            if (!isInEditMode) {
                mPaint.setShadowLayer(mShadowRadius.toFloat(), mShadowXOffset.toFloat(), mShadowYOffset.toFloat(), mShadowColor)
            }
        }

        override fun draw(canvas: Canvas) {
            val shadowRect = RectF(
                    (mShadowRadius + Math.abs(mShadowXOffset)).toFloat(),
                    (mShadowRadius + Math.abs(mShadowYOffset)).toFloat(),
                    mRawWidth.toFloat(),
                    mRawHeight.toFloat()
            )

            canvas.drawRoundRect(shadowRect, mCornerRadius.toFloat(), mCornerRadius.toFloat(), mPaint)
            canvas.drawRoundRect(shadowRect, mCornerRadius.toFloat(), mCornerRadius.toFloat(), mErase)
        }

        override fun setAlpha(alpha: Int) {

        }

        override fun setColorFilter(cf: ColorFilter?) {

        }

        override fun getOpacity(): Int {
            return PixelFormat.UNKNOWN
        }
    }

    companion object {

        private val PORTER_DUFF_CLEAR = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
}
