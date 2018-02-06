package com.awesome.fabs

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Xfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.os.SystemClock
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView

import com.awesome.R

@SuppressLint("AppCompatCustomView")
class AwesomeFab : ImageButton {

    internal var mFabSize: Int = 0
    internal var mShowShadow: Boolean = false
    internal var mShadowColor: Int = 0
    internal var mShadowRadius = AwesomeFabUtil.dpToPx(context, 4f)
    internal var mShadowXOffset = AwesomeFabUtil.dpToPx(context, 1f)
    internal var mShadowYOffset = AwesomeFabUtil.dpToPx(context, 3f)

    private var mColorNormal: Int = 0
    private var mColorPressed: Int = 0
    private var mColorDisabled: Int = 0
    private var mColorRipple: Int = 0
    private var mIcon: Drawable? = null
    private val mIconSize = AwesomeFabUtil.dpToPx(context, 24f)
    internal var showAnimation: Animation? = null
    internal var hideAnimation: Animation? = null
    private var mLabelText: String? = null
    private var mClickListener: View.OnClickListener? = null
    private var mBackgroundDrawable: Drawable? = null
    private var mUsingElevation: Boolean = false
    private var mUsingElevationCompat: Boolean = false

    // Progress
    private var mProgressBarEnabled: Boolean = false
    private var mProgressWidth = AwesomeFabUtil.dpToPx(context, 6f)
    private var mProgressColor: Int = 0
    private var mProgressBackgroundColor: Int = 0
    private var mShouldUpdateButtonPosition: Boolean = false
    private var mOriginalX = -1f
    private var mOriginalY = -1f
    private var mButtonPositionSaved: Boolean = false
    private var mProgressCircleBounds = RectF()
    private val mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mProgressIndeterminate: Boolean = false
    private var mLastTimeAnimated: Long = 0
    private var mSpinSpeed = 195.0f //The amount of degrees per second
    private var mPausedTimeWithoutGrowing: Long = 0
    private var mTimeStartGrowing: Double = 0.toDouble()
    private var mBarGrowingFromFront = true
    private val mBarLength = 16
    private var mBarExtraLength: Float = 0.toFloat()
    private var mCurrentProgress: Float = 0.toFloat()
    private var mTargetProgress: Float = 0.toFloat()
    private var mProgress: Int = 0
    private var mAnimateProgress: Boolean = false
    private var mShouldProgressIndeterminate: Boolean = false
    private var mShouldSetProgress: Boolean = false
    @get:Synchronized
    @set:Synchronized
    var max = 100
    @get:Synchronized
    var isProgressBackgroundShown: Boolean = false
        private set

    private val circleSize: Int
        get() = resources.getDimensionPixelSize(if (mFabSize == SIZE_NORMAL)
            R.dimen.fab_size_normal
        else
            R.dimen.fab_size_mini)

    private val shadowX: Int
        get() = mShadowRadius + Math.abs(mShadowXOffset)

    private val shadowY: Int
        get() = mShadowRadius + Math.abs(mShadowYOffset)

    protected val iconDrawable: Drawable
        get() = if (mIcon != null) {
            mIcon!!
        } else {
            ColorDrawable(Color.TRANSPARENT)
        }

    internal val labelView: AwesomeFabLabel?
        get() = getTag(R.id.fab_label) as AwesomeFabLabel?

    internal var mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            val awesomeFabLabel = getTag(R.id.fab_label) as AwesomeFabLabel?
            awesomeFabLabel?.onActionDown()
            onActionDown()
            return super.onDown(e)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val awesomeFabLabel = getTag(R.id.fab_label) as AwesomeFabLabel?
            awesomeFabLabel?.onActionUp()
            onActionUp()
            return super.onSingleTapUp(e)
        }
    })

    /**
     * Sets the size of the **FloatingActionButton** and invalidates its layout.
     *
     * @param size size of the **FloatingActionButton**. Accepted values: SIZE_NORMAL, SIZE_MINI.
     */
    var buttonSize: Int
        get() = mFabSize
        set(size) {
            if (size != SIZE_NORMAL && size != SIZE_MINI) {
                throw IllegalArgumentException("Use @FabSize constants only!")
            }

            if (mFabSize != size) {
                mFabSize = size
                updateBackground()
            }
        }

    var colorNormal: Int
        get() = mColorNormal
        set(color) {
            if (mColorNormal != color) {
                mColorNormal = color
                updateBackground()
            }
        }

    var colorPressed: Int
        get() = mColorPressed
        set(color) {
            if (color != mColorPressed) {
                mColorPressed = color
                updateBackground()
            }
        }

    var colorRipple: Int
        get() = mColorRipple
        set(color) {
            if (color != mColorRipple) {
                mColorRipple = color
                updateBackground()
            }
        }

    var colorDisabled: Int
        get() = mColorDisabled
        set(color) {
            if (color != mColorDisabled) {
                mColorDisabled = color
                updateBackground()
            }
        }

    /**
     * Sets the shadow radius of the **FloatingActionButton** and invalidates its layout.
     *
     * @param dimenResId the resource identifier of the dimension
     */
    var shadowRadius: Int
        get() = mShadowRadius
        set(dimenResId) {
            val shadowRadius = resources.getDimensionPixelSize(dimenResId)
            if (mShadowRadius != shadowRadius) {
                mShadowRadius = shadowRadius
                requestLayout()
                updateBackground()
            }
        }

    /**
     * Sets the shadow x offset of the **FloatingActionButton** and invalidates its layout.
     *
     * @param dimenResId the resource identifier of the dimension
     */
    var shadowXOffset: Int
        get() = mShadowXOffset
        set(dimenResId) {
            val shadowXOffset = resources.getDimensionPixelSize(dimenResId)
            if (mShadowXOffset != shadowXOffset) {
                mShadowXOffset = shadowXOffset
                requestLayout()
                updateBackground()
            }
        }

    /**
     * Sets the shadow y offset of the **FloatingActionButton** and invalidates its layout.
     *
     * @param dimenResId the resource identifier of the dimension
     */
    var shadowYOffset: Int
        get() = mShadowYOffset
        set(dimenResId) {
            val shadowYOffset = resources.getDimensionPixelSize(dimenResId)
            if (mShadowYOffset != shadowYOffset) {
                mShadowYOffset = shadowYOffset
                requestLayout()
                updateBackground()
            }
        }

    var shadowColor: Int
        get() = mShadowColor
        set(color) {
            if (mShadowColor != color) {
                mShadowColor = color
                updateBackground()
            }
        }

    /**
     * Checks whether **FloatingActionButton** is hidden
     *
     * @return true if **FloatingActionButton** is hidden, false otherwise
     */
    val isHidden: Boolean
        get() = visibility == View.INVISIBLE

    var labelText: String?
        get() = mLabelText
        set(text) {
            mLabelText = text
            val labelView = labelView
            if (labelView != null) {
                labelView.text = text
            }
        }

    var labelVisibility: Int
        get() {
            val labelView = labelView
            return labelView?.visibility ?: -1

        }
        set(visibility) {
            val awesomeFabLabelView = labelView
            if (awesomeFabLabelView != null) {
                awesomeFabLabelView.visibility = visibility
                awesomeFabLabelView.isHandleVisibilityChanges = visibility == View.VISIBLE
            }
        }

    val progress: Int
        @Synchronized get() = if (mProgressIndeterminate) 0 else mProgress

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.AwesomeFab, defStyleAttr, 0)
        mColorNormal = attr.getColor(R.styleable.AwesomeFab_fab_colorNormal, -0x25bcca)
        mColorPressed = attr.getColor(R.styleable.AwesomeFab_fab_colorPressed, -0x18afbd)
        mColorDisabled = attr.getColor(R.styleable.AwesomeFab_fab_colorDisabled, -0x555556)
        mColorRipple = attr.getColor(R.styleable.AwesomeFab_fab_colorRipple, -0x66000001)
        mShowShadow = attr.getBoolean(R.styleable.AwesomeFab_fab_showShadow, true)
        mShadowColor = attr.getColor(R.styleable.AwesomeFab_fab_shadowColor, 0x66000000)
        mShadowRadius = attr.getDimensionPixelSize(R.styleable.AwesomeFab_fab_shadowRadius, mShadowRadius)
        mShadowXOffset = attr.getDimensionPixelSize(R.styleable.AwesomeFab_fab_shadowXOffset, mShadowXOffset)
        mShadowYOffset = attr.getDimensionPixelSize(R.styleable.AwesomeFab_fab_shadowYOffset, mShadowYOffset)
        mFabSize = attr.getInt(R.styleable.AwesomeFab_fab_size, SIZE_NORMAL)
        mLabelText = attr.getString(R.styleable.AwesomeFab_fab_label)
        mShouldProgressIndeterminate = attr.getBoolean(R.styleable.AwesomeFab_fab_progress_indeterminate, false)
        mProgressColor = attr.getColor(R.styleable.AwesomeFab_fab_progress_color, -0xff6978)
        mProgressBackgroundColor = attr.getColor(R.styleable.AwesomeFab_fab_progress_backgroundColor, 0x4D000000)
        max = attr.getInt(R.styleable.AwesomeFab_fab_progress_max, max)
        isProgressBackgroundShown = attr.getBoolean(R.styleable.AwesomeFab_fab_progress_showBackground, true)

        if (attr.hasValue(R.styleable.AwesomeFab_fab_progress)) {
            mProgress = attr.getInt(R.styleable.AwesomeFab_fab_progress, 0)
            mShouldSetProgress = true
        }

        if (attr.hasValue(R.styleable.AwesomeFab_fab_elevationCompat)) {
            val elevation = attr.getDimensionPixelOffset(R.styleable.AwesomeFab_fab_elevationCompat, 0).toFloat()
            if (isInEditMode) {
                setElevation(elevation)
            } else {
                setElevationCompat(elevation)
            }
        }

        initShowAnimation(attr)
        initHideAnimation(attr)
        attr.recycle()

        if (isInEditMode) {
            if (mShouldProgressIndeterminate) {
                setIndeterminate(true)
            } else if (mShouldSetProgress) {
                saveButtonOriginalPosition()
                setProgress(mProgress, false)
            }
        }

        //        updateBackground();
        isClickable = true
    }

    private fun initShowAnimation(attr: TypedArray) {
        val resourceId = attr.getResourceId(R.styleable.AwesomeFab_fab_showAnimation, R.anim.fab_scale_up)
        showAnimation = AnimationUtils.loadAnimation(context, resourceId)
    }

    private fun initHideAnimation(attr: TypedArray) {
        val resourceId = attr.getResourceId(R.styleable.AwesomeFab_fab_hideAnimation, R.anim.fab_scale_down)
        hideAnimation = AnimationUtils.loadAnimation(context, resourceId)
    }

    private fun calculateMeasuredWidth(): Int {
        var width = circleSize + calculateShadowWidth()
        if (mProgressBarEnabled) {
            width += mProgressWidth * 2
        }
        return width
    }

    private fun calculateMeasuredHeight(): Int {
        var height = circleSize + calculateShadowHeight()
        if (mProgressBarEnabled) {
            height += mProgressWidth * 2
        }
        return height
    }

    internal fun calculateShadowWidth(): Int {
        return if (hasShadow()) shadowX * 2 else 0
    }

    internal fun calculateShadowHeight(): Int {
        return if (hasShadow()) shadowY * 2 else 0
    }

    private fun calculateCenterX(): Float {
        return (measuredWidth / 2).toFloat()
    }

    private fun calculateCenterY(): Float {
        return (measuredHeight / 2).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(calculateMeasuredWidth(), calculateMeasuredHeight())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mProgressBarEnabled) {
            if (isProgressBackgroundShown) {
                canvas.drawArc(mProgressCircleBounds, 360f, 360f, false, mBackgroundPaint)
            }

            var shouldInvalidate = false

            if (mProgressIndeterminate) {
                shouldInvalidate = true

                val deltaTime = SystemClock.uptimeMillis() - mLastTimeAnimated
                val deltaNormalized = deltaTime * mSpinSpeed / 1000.0f

                updateProgressLength(deltaTime)

                mCurrentProgress += deltaNormalized
                if (mCurrentProgress > 360f) {
                    mCurrentProgress -= 360f
                }

                mLastTimeAnimated = SystemClock.uptimeMillis()
                var from = mCurrentProgress - 90
                var to = mBarLength + mBarExtraLength

                if (isInEditMode) {
                    from = 0f
                    to = 135f
                }

                canvas.drawArc(mProgressCircleBounds, from, to, false, mProgressPaint)
            } else {
                if (mCurrentProgress != mTargetProgress) {
                    shouldInvalidate = true
                    val deltaTime = (SystemClock.uptimeMillis() - mLastTimeAnimated).toFloat() / 1000
                    val deltaNormalized = deltaTime * mSpinSpeed

                    if (mCurrentProgress > mTargetProgress) {
                        mCurrentProgress = Math.max(mCurrentProgress - deltaNormalized, mTargetProgress)
                    } else {
                        mCurrentProgress = Math.min(mCurrentProgress + deltaNormalized, mTargetProgress)
                    }
                    mLastTimeAnimated = SystemClock.uptimeMillis()
                }

                canvas.drawArc(mProgressCircleBounds, -90f, mCurrentProgress, false, mProgressPaint)
            }

            if (shouldInvalidate) {
                invalidate()
            }
        }
    }

    private fun updateProgressLength(deltaTimeInMillis: Long) {
        if (mPausedTimeWithoutGrowing >= PAUSE_GROWING_TIME) {
            mTimeStartGrowing += deltaTimeInMillis.toDouble()

            if (mTimeStartGrowing > BAR_SPIN_CYCLE_TIME) {
                mTimeStartGrowing -= BAR_SPIN_CYCLE_TIME
                mPausedTimeWithoutGrowing = 0
                mBarGrowingFromFront = !mBarGrowingFromFront
            }

            val distance = Math.cos((mTimeStartGrowing / BAR_SPIN_CYCLE_TIME + 1) * Math.PI).toFloat() / 2 + 0.5f
            val length = (BAR_MAX_LENGTH - mBarLength).toFloat()

            if (mBarGrowingFromFront) {
                mBarExtraLength = distance * length
            } else {
                val newLength = length * (1 - distance)
                mCurrentProgress += mBarExtraLength - newLength
                mBarExtraLength = newLength
            }
        } else {
            mPausedTimeWithoutGrowing += deltaTimeInMillis
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        saveButtonOriginalPosition()

        if (mShouldProgressIndeterminate) {
            setIndeterminate(true)
            mShouldProgressIndeterminate = false
        } else if (mShouldSetProgress) {
            setProgress(mProgress, mAnimateProgress)
            mShouldSetProgress = false
        } else if (mShouldUpdateButtonPosition) {
            updateButtonPosition()
            mShouldUpdateButtonPosition = false
        }
        super.onSizeChanged(w, h, oldw, oldh)

        setupProgressBounds()
        setupProgressBarPaints()
        updateBackground()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun setLayoutParams(params: ViewGroup.LayoutParams) {
        if (params is ViewGroup.MarginLayoutParams && mUsingElevationCompat) {
            params.leftMargin += shadowX
            params.topMargin += shadowY
            params.rightMargin += shadowX
            params.bottomMargin += shadowY
        }
        super.setLayoutParams(params)
    }

    internal fun updateBackground() {
        val layerDrawable: LayerDrawable
        if (hasShadow()) {
            layerDrawable = LayerDrawable(arrayOf(Shadow(), createFillDrawable(), iconDrawable))
        } else {
            layerDrawable = LayerDrawable(arrayOf(createFillDrawable(), iconDrawable))
        }

        var iconSize = -1
        if (iconDrawable != null) {
            iconSize = Math.max(iconDrawable.intrinsicWidth, iconDrawable.intrinsicHeight)
        }
        val iconOffset = (circleSize - if (iconSize > 0) iconSize else mIconSize) / 2
        var circleInsetHorizontal = if (hasShadow()) mShadowRadius + Math.abs(mShadowXOffset) else 0
        var circleInsetVertical = if (hasShadow()) mShadowRadius + Math.abs(mShadowYOffset) else 0

        if (mProgressBarEnabled) {
            circleInsetHorizontal += mProgressWidth
            circleInsetVertical += mProgressWidth
        }

        /*layerDrawable.setLayerInset(
                mShowShadow ? 1 : 0,
                circleInsetHorizontal,
                circleInsetVertical,
                circleInsetHorizontal,
                circleInsetVertical
        );*/
        layerDrawable.setLayerInset(
                if (hasShadow()) 2 else 1,
                circleInsetHorizontal + iconOffset,
                circleInsetVertical + iconOffset,
                circleInsetHorizontal + iconOffset,
                circleInsetVertical + iconOffset
        )

        setBackgroundCompat(layerDrawable)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createFillDrawable(): Drawable {
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(-android.R.attr.state_enabled), createCircleDrawable(mColorDisabled))
        drawable.addState(intArrayOf(android.R.attr.state_pressed), createCircleDrawable(mColorPressed))
        drawable.addState(intArrayOf(), createCircleDrawable(mColorNormal))

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

    private fun createCircleDrawable(color: Int): Drawable {
        val shapeDrawable = CircleDrawable(OvalShape())
        shapeDrawable.paint.color = color
        return shapeDrawable
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun setBackgroundCompat(drawable: Drawable) {
        if (AwesomeFabUtil.hasJellyBean()) {
            background = drawable
        } else {
            setBackgroundDrawable(drawable)
        }
    }

    private fun saveButtonOriginalPosition() {
        if (!mButtonPositionSaved) {
            if (mOriginalX == -1f) {
                mOriginalX = x
            }

            if (mOriginalY == -1f) {
                mOriginalY = y
            }

            mButtonPositionSaved = true
        }
    }

    private fun updateButtonPosition() {
        val x: Float
        val y: Float
        if (mProgressBarEnabled) {
            x = if (mOriginalX > getX()) getX() + mProgressWidth else getX() - mProgressWidth
            y = if (mOriginalY > getY()) getY() + mProgressWidth else getY() - mProgressWidth
        } else {
            x = mOriginalX
            y = mOriginalY
        }
        setX(x)
        setY(y)
    }

    private fun setupProgressBarPaints() {
        mBackgroundPaint.color = mProgressBackgroundColor
        mBackgroundPaint.style = Paint.Style.STROKE
        mBackgroundPaint.strokeWidth = mProgressWidth.toFloat()

        mProgressPaint.color = mProgressColor
        mProgressPaint.style = Paint.Style.STROKE
        mProgressPaint.strokeWidth = mProgressWidth.toFloat()
    }

    private fun setupProgressBounds() {
        val circleInsetHorizontal = if (hasShadow()) shadowX else 0
        val circleInsetVertical = if (hasShadow()) shadowY else 0
        mProgressCircleBounds = RectF(
                (circleInsetHorizontal + mProgressWidth / 2).toFloat(),
                (circleInsetVertical + mProgressWidth / 2).toFloat(),
                (calculateMeasuredWidth() - circleInsetHorizontal - mProgressWidth / 2).toFloat(),
                (calculateMeasuredHeight() - circleInsetVertical - mProgressWidth / 2).toFloat()
        )
    }

    internal fun playShowAnimation() {
        hideAnimation!!.cancel()
        startAnimation(showAnimation)
    }

    internal fun playHideAnimation() {
        showAnimation!!.cancel()
        startAnimation(hideAnimation)
    }

    internal fun getOnClickListener(): View.OnClickListener? {
        return mClickListener
    }

    internal fun setColors(colorNormal: Int, colorPressed: Int, colorRipple: Int) {
        mColorNormal = colorNormal
        mColorPressed = colorPressed
        mColorRipple = colorRipple
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    internal fun onActionDown() {
        if (mBackgroundDrawable is StateListDrawable) {
            val drawable = mBackgroundDrawable as StateListDrawable?
            drawable!!.state = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed)
        } else if (AwesomeFabUtil.hasLollipop()) {
            val ripple = mBackgroundDrawable as RippleDrawable?
            ripple!!.state = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed)
            ripple.setHotspot(calculateCenterX(), calculateCenterY())
            ripple.setVisible(true, true)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    internal fun onActionUp() {
        if (mBackgroundDrawable is StateListDrawable) {
            val drawable = mBackgroundDrawable as StateListDrawable?
            drawable!!.state = intArrayOf(android.R.attr.state_enabled)
        } else if (AwesomeFabUtil.hasLollipop()) {
            val ripple = mBackgroundDrawable as RippleDrawable?
            ripple!!.state = intArrayOf(android.R.attr.state_enabled)
            ripple.setHotspot(calculateCenterX(), calculateCenterY())
            ripple.setVisible(true, true)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mClickListener != null && isEnabled) {
            val awesomeFabLabel = getTag(R.id.fab_label) as AwesomeFabLabel? ?: return super.onTouchEvent(event)

            val action = event.action
            when (action) {
                MotionEvent.ACTION_UP -> {
                    awesomeFabLabel.onActionUp()
                    onActionUp()
                }

                MotionEvent.ACTION_CANCEL -> {
                    awesomeFabLabel.onActionUp()
                    onActionUp()
                }
            }
            mGestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()

        val ss = ProgressSavedState(superState)

        ss.mCurrentProgress = this.mCurrentProgress
        ss.mTargetProgress = this.mTargetProgress
        ss.mSpinSpeed = this.mSpinSpeed
        ss.mProgressWidth = this.mProgressWidth
        ss.mProgressColor = this.mProgressColor
        ss.mProgressBackgroundColor = this.mProgressBackgroundColor
        ss.mShouldProgressIndeterminate = this.mProgressIndeterminate
        ss.mShouldSetProgress = this.mProgressBarEnabled && mProgress > 0 && !this.mProgressIndeterminate
        ss.mProgress = this.mProgress
        ss.mAnimateProgress = this.mAnimateProgress
        ss.mShowProgressBackground = this.isProgressBackgroundShown

        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is ProgressSavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superState)

        this.mCurrentProgress = state.mCurrentProgress
        this.mTargetProgress = state.mTargetProgress
        this.mSpinSpeed = state.mSpinSpeed
        this.mProgressWidth = state.mProgressWidth
        this.mProgressColor = state.mProgressColor
        this.mProgressBackgroundColor = state.mProgressBackgroundColor
        this.mShouldProgressIndeterminate = state.mShouldProgressIndeterminate
        this.mShouldSetProgress = state.mShouldSetProgress
        this.mProgress = state.mProgress
        this.mAnimateProgress = state.mAnimateProgress
        this.isProgressBackgroundShown = state.mShowProgressBackground

        this.mLastTimeAnimated = SystemClock.uptimeMillis()
    }

    private inner class CircleDrawable : ShapeDrawable {

        private var circleInsetHorizontal: Int = 0
        private var circleInsetVertical: Int = 0

        private constructor()

        internal constructor(s: Shape) : super(s) {
            circleInsetHorizontal = if (hasShadow()) mShadowRadius + Math.abs(mShadowXOffset) else 0
            circleInsetVertical = if (hasShadow()) mShadowRadius + Math.abs(mShadowYOffset) else 0

            if (mProgressBarEnabled) {
                circleInsetHorizontal += mProgressWidth
                circleInsetVertical += mProgressWidth
            }
        }

        override fun draw(canvas: Canvas) {
            setBounds(circleInsetHorizontal, circleInsetVertical, calculateMeasuredWidth() - circleInsetHorizontal, calculateMeasuredHeight() - circleInsetVertical)
            super.draw(canvas)
        }
    }

    private inner class Shadow internal constructor() : Drawable() {

        private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val mErase = Paint(Paint.ANTI_ALIAS_FLAG)
        private var mRadius: Float = 0.toFloat()

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

            mRadius = (circleSize / 2).toFloat()

            if (mProgressBarEnabled && isProgressBackgroundShown) {
                mRadius += mProgressWidth.toFloat()
            }
        }

        override fun draw(canvas: Canvas) {
            canvas.drawCircle(calculateCenterX(), calculateCenterY(), mRadius, mPaint)
            canvas.drawCircle(calculateCenterX(), calculateCenterY(), mRadius, mErase)
        }

        override fun setAlpha(alpha: Int) {

        }

        override fun setColorFilter(cf: ColorFilter?) {

        }

        override fun getOpacity(): Int {
            return PixelFormat.UNKNOWN
        }
    }

    internal class ProgressSavedState : View.BaseSavedState {

        var mCurrentProgress: Float = 0.toFloat()
        var mTargetProgress: Float = 0.toFloat()
        var mSpinSpeed: Float = 0.toFloat()
        var mProgress: Int = 0
        var mProgressWidth: Int = 0
        var mProgressColor: Int = 0
        var mProgressBackgroundColor: Int = 0
        var mProgressBarEnabled: Boolean = false
        var mProgressBarVisibilityChanged: Boolean = false
        var mProgressIndeterminate: Boolean = false
        var mShouldProgressIndeterminate: Boolean = false
        var mShouldSetProgress: Boolean = false
        var mAnimateProgress: Boolean = false
        var mShowProgressBackground: Boolean = false

        constructor(superState: Parcelable) : super(superState)

        private constructor(`in`: Parcel) : super(`in`) {
            this.mCurrentProgress = `in`.readFloat()
            this.mTargetProgress = `in`.readFloat()
            this.mProgressBarEnabled = `in`.readInt() != 0
            this.mSpinSpeed = `in`.readFloat()
            this.mProgress = `in`.readInt()
            this.mProgressWidth = `in`.readInt()
            this.mProgressColor = `in`.readInt()
            this.mProgressBackgroundColor = `in`.readInt()
            this.mProgressBarVisibilityChanged = `in`.readInt() != 0
            this.mProgressIndeterminate = `in`.readInt() != 0
            this.mShouldProgressIndeterminate = `in`.readInt() != 0
            this.mShouldSetProgress = `in`.readInt() != 0
            this.mAnimateProgress = `in`.readInt() != 0
            this.mShowProgressBackground = `in`.readInt() != 0
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(this.mCurrentProgress)
            out.writeFloat(this.mTargetProgress)
            out.writeInt(if (mProgressBarEnabled) 1 else 0)
            out.writeFloat(this.mSpinSpeed)
            out.writeInt(this.mProgress)
            out.writeInt(this.mProgressWidth)
            out.writeInt(this.mProgressColor)
            out.writeInt(this.mProgressBackgroundColor)
            out.writeInt(if (this.mProgressBarVisibilityChanged) 1 else 0)
            out.writeInt(if (this.mProgressIndeterminate) 1 else 0)
            out.writeInt(if (this.mShouldProgressIndeterminate) 1 else 0)
            out.writeInt(if (this.mShouldSetProgress) 1 else 0)
            out.writeInt(if (this.mAnimateProgress) 1 else 0)
            out.writeInt(if (this.mShowProgressBackground) 1 else 0)
        }

        companion object {

            val CREATOR: Parcelable.Creator<ProgressSavedState> = object : Parcelable.Creator<ProgressSavedState> {
                override fun createFromParcel(`in`: Parcel): ProgressSavedState {
                    return ProgressSavedState(`in`)
                }

                override fun newArray(size: Int): Array<ProgressSavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    /* ===== API methods ===== */

    override fun setImageDrawable(drawable: Drawable?) {
        if (mIcon !== drawable) {
            mIcon = drawable
            updateBackground()
        }
    }

    override fun setImageResource(resId: Int) {
        val drawable = resources.getDrawable(resId)
        if (mIcon !== drawable) {
            mIcon = drawable
            updateBackground()
        }
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        super.setOnClickListener(l)
        mClickListener = l
        val label = getTag(R.id.fab_label) as View?
        label?.setOnClickListener {
            if (mClickListener != null) {
                mClickListener!!.onClick(this@AwesomeFab)
            }
        }
    }

    fun setColorNormalResId(colorResId: Int) {
        colorNormal = resources.getColor(colorResId)
    }

    fun setColorPressedResId(colorResId: Int) {
        colorPressed = resources.getColor(colorResId)
    }

    fun setColorRippleResId(colorResId: Int) {
        colorRipple = resources.getColor(colorResId)
    }

    fun setColorDisabledResId(colorResId: Int) {
        colorDisabled = resources.getColor(colorResId)
    }

    fun setShowShadow(show: Boolean) {
        if (mShowShadow != show) {
            mShowShadow = show
            updateBackground()
        }
    }

    fun hasShadow(): Boolean {
        return !mUsingElevation && mShowShadow
    }

    /**
     * Sets the shadow radius of the **FloatingActionButton** and invalidates its layout.
     *
     *
     * Must be specified in density-independent (dp) pixels, which are then converted into actual
     * pixels (px).
     *
     * @param shadowRadiusDp shadow radius specified in density-independent (dp) pixels
     */
    fun setShadowRadius(shadowRadiusDp: Float) {
        mShadowRadius = AwesomeFabUtil.dpToPx(context, shadowRadiusDp)
        requestLayout()
        updateBackground()
    }

    /**
     * Sets the shadow x offset of the **FloatingActionButton** and invalidates its layout.
     *
     *
     * Must be specified in density-independent (dp) pixels, which are then converted into actual
     * pixels (px).
     *
     * @param shadowXOffsetDp shadow radius specified in density-independent (dp) pixels
     */
    fun setShadowXOffset(shadowXOffsetDp: Float) {
        mShadowXOffset = AwesomeFabUtil.dpToPx(context, shadowXOffsetDp)
        requestLayout()
        updateBackground()
    }

    /**
     * Sets the shadow y offset of the **FloatingActionButton** and invalidates its layout.
     *
     *
     * Must be specified in density-independent (dp) pixels, which are then converted into actual
     * pixels (px).
     *
     * @param shadowYOffsetDp shadow radius specified in density-independent (dp) pixels
     */
    fun setShadowYOffset(shadowYOffsetDp: Float) {
        mShadowYOffset = AwesomeFabUtil.dpToPx(context, shadowYOffsetDp)
        requestLayout()
        updateBackground()
    }

    fun setShadowColorResource(colorResId: Int) {
        val shadowColor = resources.getColor(colorResId)
        if (mShadowColor != shadowColor) {
            mShadowColor = shadowColor
            updateBackground()
        }
    }

    /**
     * Makes the **FloatingActionButton** to appear and sets its visibility to [.VISIBLE]
     *
     * @param animate if true - plays "show animation"
     */
    fun show(animate: Boolean) {
        if (isHidden) {
            if (animate) {
                playShowAnimation()
            }
            super.setVisibility(View.VISIBLE)
        }
    }

    /**
     * Makes the **FloatingActionButton** to disappear and sets its visibility to [.INVISIBLE]
     *
     * @param animate if true - plays "hide animation"
     */
    fun hide(animate: Boolean) {
        if (!isHidden) {
            if (animate) {
                playHideAnimation()
            }
            super.setVisibility(View.INVISIBLE)
        }
    }

    fun toggle(animate: Boolean) {
        if (isHidden) {
            show(animate)
        } else {
            hide(animate)
        }
    }

    override fun setElevation(elevation: Float) {
        if (AwesomeFabUtil.hasLollipop() && elevation > 0) {
            super.setElevation(elevation)
            if (!isInEditMode) {
                mUsingElevation = true
                mShowShadow = false
            }
            updateBackground()
        }
    }

    /**
     * Sets the shadow color and radius to mimic the native elevation.
     *
     *
     * **API 21+**: Sets the native elevation of this view, in pixels. Updates margins to
     * make the view hold its position in layout across different platform versions.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setElevationCompat(elevation: Float) {
        mShadowColor = 0x26000000
        mShadowRadius = Math.round(elevation / 2)
        mShadowXOffset = 0
        mShadowYOffset = Math.round(if (mFabSize == SIZE_NORMAL) elevation else elevation / 2)

        if (AwesomeFabUtil.hasLollipop()) {
            super.setElevation(elevation)
            mUsingElevationCompat = true
            mShowShadow = false
            updateBackground()

            val layoutParams = layoutParams
            if (layoutParams != null) {
                setLayoutParams(layoutParams)
            }
        } else {
            mShowShadow = true
            updateBackground()
        }
    }

    /**
     *
     * Change the indeterminate mode for the progress bar. In indeterminate
     * mode, the progress is ignored and the progress bar shows an infinite
     * animation instead.
     *
     * @param indeterminate true to enable the indeterminate mode
     */
    @Synchronized
    fun setIndeterminate(indeterminate: Boolean) {
        if (!indeterminate) {
            mCurrentProgress = 0.0f
        }

        mProgressBarEnabled = indeterminate
        mShouldUpdateButtonPosition = true
        mProgressIndeterminate = indeterminate
        mLastTimeAnimated = SystemClock.uptimeMillis()
        setupProgressBounds()
        //        saveButtonOriginalPosition();
        updateBackground()
    }

    @Synchronized
    fun setProgress(progress: Int, animate: Boolean) {
        var progress = progress
        if (mProgressIndeterminate) return

        mProgress = progress
        mAnimateProgress = animate

        if (!mButtonPositionSaved) {
            mShouldSetProgress = true
            return
        }

        mProgressBarEnabled = true
        mShouldUpdateButtonPosition = true
        setupProgressBounds()
        saveButtonOriginalPosition()
        updateBackground()

        if (progress < 0) {
            progress = 0
        } else if (progress > max) {
            progress = max
        }

        if (progress.toFloat() == mTargetProgress) {
            return
        }

        mTargetProgress = if (max > 0) progress / max.toFloat() * 360 else "0".toFloat()
        mLastTimeAnimated = SystemClock.uptimeMillis()

        if (!animate) {
            mCurrentProgress = mTargetProgress
        }

        invalidate()
    }

    @Synchronized
    fun hideProgress() {
        mProgressBarEnabled = false
        mShouldUpdateButtonPosition = true
        updateBackground()
    }

    @Synchronized
    fun setShowProgressBackground(show: Boolean) {
        isProgressBackgroundShown = show
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        val awesomeFabLabel = getTag(R.id.fab_label) as AwesomeFabLabel
        if (awesomeFabLabel != null) {
            awesomeFabLabel.isEnabled = enabled
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        val awesomeFabLabel = getTag(R.id.fab_label) as AwesomeFabLabel
        if (awesomeFabLabel != null) {
            awesomeFabLabel.visibility = visibility
        }
    }

    /**
     * **This will clear all AnimationListeners.**
     */
    fun hideButtonInMenu(animate: Boolean) {
        if (!isHidden && visibility != View.GONE) {
            hide(animate)

            val awesomeFabLabel = labelView
            awesomeFabLabel?.hide(animate)

            hideAnimation!!.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    visibility = View.GONE
                    hideAnimation!!.setAnimationListener(null)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
    }

    fun showButtonInMenu(animate: Boolean) {
        if (visibility == View.VISIBLE) return

        visibility = View.INVISIBLE
        show(animate)
        val awesomeFabLabel = labelView
        awesomeFabLabel?.show(animate)
    }

    /**
     * Set the label's background colors
     */
    fun setLabelColors(colorNormal: Int, colorPressed: Int, colorRipple: Int) {
        val awesomeFabLabel = labelView

        val left = awesomeFabLabel!!.paddingLeft
        val top = awesomeFabLabel.paddingTop
        val right = awesomeFabLabel.paddingRight
        val bottom = awesomeFabLabel.paddingBottom

        awesomeFabLabel.setColors(colorNormal, colorPressed, colorRipple)
        awesomeFabLabel.updateBackground()
        awesomeFabLabel.setPadding(left, top, right, bottom)
    }

    fun setLabelTextColor(color: Int) {
        labelView!!.setTextColor(color)
    }

    fun setLabelTextColor(colors: ColorStateList) {
        labelView!!.setTextColor(colors)
    }

    companion object {

        val SIZE_NORMAL = 0
        val SIZE_MINI = 1

        private val PORTER_DUFF_CLEAR = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        private val PAUSE_GROWING_TIME: Long = 200
        private val BAR_SPIN_CYCLE_TIME = 500.0
        private val BAR_MAX_LENGTH = 270
    }
}
