package com.awesome.fabs

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateInterpolator
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView

import com.awesome.R

import java.util.ArrayList

class AwesomeFabMenu @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    private val mOpenAnimatorSet = AnimatorSet()
    private val mCloseAnimatorSet = AnimatorSet()
    var iconToggleAnimatorSet: AnimatorSet? = null

    private var mButtonSpacing = AwesomeFabUtil.dpToPx(getContext(), 0f)
    private var mMenuButton: AwesomeFab? = null
    private var mMaxButtonWidth: Int = 0
    private var mLabelsMargin = AwesomeFabUtil.dpToPx(getContext(), 0f)
    private val mLabelsVerticalOffset = AwesomeFabUtil.dpToPx(getContext(), 0f)
    private var mButtonsCount: Int = 0
    /* ===== API methods ===== */

    var isOpened: Boolean = false
        private set

    private var mIsMenuOpening: Boolean = false
    private val mUiHandler = Handler()
    private var mLabelsShowAnimation: Int = 0
    private var mLabelsHideAnimation: Int = 0
    private var mLabelsPaddingTop = AwesomeFabUtil.dpToPx(getContext(), 4f)
    private var mLabelsPaddingRight = AwesomeFabUtil.dpToPx(getContext(), 8f)
    private var mLabelsPaddingBottom = AwesomeFabUtil.dpToPx(getContext(), 4f)
    private var mLabelsPaddingLeft = AwesomeFabUtil.dpToPx(getContext(), 8f)
    private var mLabelsTextColor: ColorStateList? = null
    private var mLabelsTextSize: Float = 0.toFloat()
    private var mLabelsCornerRadius = AwesomeFabUtil.dpToPx(getContext(), 3f)
    private var mLabelsShowShadow: Boolean = false
    private var mLabelsColorNormal: Int = 0
    private var mLabelsColorPressed: Int = 0
    private var mLabelsColorRipple: Int = 0
    private var mMenuShowShadow: Boolean = false
    private var mMenuShadowColor: Int = 0
    private var mMenuShadowRadius = 4f
    private var mMenuShadowXOffset = 1f
    private var mMenuShadowYOffset = 3f
    private var mMenuColorNormal: Int = 0
    private var mMenuColorPressed: Int = 0
    private var mMenuColorRipple: Int = 0
    private var mIcon: Drawable? = null
    var animationDelayPerItem: Int = 0
    private var mOpenInterpolator: Interpolator? = null
    private var mCloseInterpolator: Interpolator? = null
    /**
     * Sets whether open and close actions should be animated
     *
     * @param animated if **false** - menu items will appear/disappear instantly without any animation
     */
    var isAnimated = true
        set(animated) {
            field = animated
            mOpenAnimatorSet.duration = (if (animated) ANIMATION_DURATION else 0).toLong()
            mCloseAnimatorSet.duration = (if (animated) ANIMATION_DURATION else 0).toLong()
        }
    private var mLabelsSingleLine: Boolean = false
    private var mLabelsEllipsize: Int = 0
    private var mLabelsMaxLines: Int = 0
    private var mMenuFabSize: Int = 0
    private var mLabelsStyle: Int = 0
    private var mCustomTypefaceFromFont: Typeface? = null
    var isIconAnimated = true
    var menuIconView: ImageView? = null
        private set
    private var mMenuButtonShowAnimation: Animation? = null
    private var mMenuButtonHideAnimation: Animation? = null
    private var mImageToggleShowAnimation: Animation? = null
    private var mImageToggleHideAnimation: Animation? = null
    private var mIsMenuButtonAnimationRunning: Boolean = false
    private var mIsSetClosedOnTouchOutside: Boolean = false
    private var mOpenDirection: Int = 0
    private var mToggleListener: OnMenuToggleListener? = null

    private var mShowBackgroundAnimator: ValueAnimator? = null
    private var mHideBackgroundAnimator: ValueAnimator? = null
    private var mBackgroundColor: Int = 0

    private var mLabelsPosition: Int = 0
    private var mLabelsContext: Context? = null
    private var mMenuLabelText: String? = null
    private var mUsingMenuLabel: Boolean = false

    private val isBackgroundEnabled: Boolean
        get() = mBackgroundColor != Color.TRANSPARENT

    val isMenuHidden: Boolean
        get() = visibility == View.INVISIBLE

    val isMenuButtonHidden: Boolean
        get() = mMenuButton!!.isHidden

    var menuButtonColorNormal: Int
        get() = mMenuColorNormal
        set(color) {
            mMenuColorNormal = color
            mMenuButton!!.colorNormal = color
        }

    var menuButtonColorPressed: Int
        get() = mMenuColorPressed
        set(color) {
            mMenuColorPressed = color
            mMenuButton!!.colorPressed = color
        }

    var menuButtonColorRipple: Int
        get() = mMenuColorRipple
        set(color) {
            mMenuColorRipple = color
            mMenuButton!!.colorRipple = color
        }

    var menuButtonLabelText: String?
        get() = mMenuLabelText
        set(text) {
            mMenuButton!!.labelText = text
        }

    interface OnMenuToggleListener {
        fun onMenuToggle(opened: Boolean)
    }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.AwesomeFabMenu, 0, 0)
        mButtonSpacing = attr.getDimensionPixelSize(R.styleable.AwesomeFabMenu_menu_buttonSpacing, mButtonSpacing)
        mLabelsMargin = attr.getDimensionPixelSize(R.styleable.AwesomeFabMenu_menu_labels_margin, mLabelsMargin)
        mLabelsPosition = attr.getInt(R.styleable.AwesomeFabMenu_menu_labels_position, LABELS_POSITION_LEFT)
        mLabelsShowAnimation = attr.getResourceId(R.styleable.AwesomeFabMenu_menu_labels_showAnimation,
                if (mLabelsPosition == LABELS_POSITION_LEFT) R.anim.fab_slide_in_from_right else R.anim.fab_slide_in_from_left)
        mLabelsHideAnimation = attr.getResourceId(R.styleable.AwesomeFabMenu_menu_labels_hideAnimation,
                if (mLabelsPosition == LABELS_POSITION_LEFT) R.anim.fab_slide_out_to_right else R.anim.fab_slide_out_to_left)
        mLabelsPaddingTop = attr.getDimensionPixelSize(R.styleable.AwesomeFabMenu_menu_labels_paddingTop, mLabelsPaddingTop)
        mLabelsPaddingRight = attr.getDimensionPixelSize(R.styleable.AwesomeFabMenu_menu_labels_paddingRight, mLabelsPaddingRight)
        mLabelsPaddingBottom = attr.getDimensionPixelSize(R.styleable.AwesomeFabMenu_menu_labels_paddingBottom, mLabelsPaddingBottom)
        mLabelsPaddingLeft = attr.getDimensionPixelSize(R.styleable.AwesomeFabMenu_menu_labels_paddingLeft, mLabelsPaddingLeft)
        mLabelsTextColor = attr.getColorStateList(R.styleable.AwesomeFabMenu_menu_labels_textColor)
        // set default value if null same as for textview
        if (mLabelsTextColor == null) {
            mLabelsTextColor = ColorStateList.valueOf(Color.WHITE)
        }
        mLabelsTextSize = attr.getDimension(R.styleable.AwesomeFabMenu_menu_labels_textSize, resources.getDimension(R.dimen.labels_text_size))
        mLabelsCornerRadius = attr.getDimensionPixelSize(R.styleable.AwesomeFabMenu_menu_labels_cornerRadius, mLabelsCornerRadius)
        mLabelsShowShadow = attr.getBoolean(R.styleable.AwesomeFabMenu_menu_labels_showShadow, true)
        mLabelsColorNormal = attr.getColor(R.styleable.AwesomeFabMenu_menu_labels_colorNormal, -0xcccccd)
        mLabelsColorPressed = attr.getColor(R.styleable.AwesomeFabMenu_menu_labels_colorPressed, -0xbbbbbc)
        mLabelsColorRipple = attr.getColor(R.styleable.AwesomeFabMenu_menu_labels_colorRipple, 0x66FFFFFF)
        mMenuShowShadow = attr.getBoolean(R.styleable.AwesomeFabMenu_menu_showShadow, true)
        mMenuShadowColor = attr.getColor(R.styleable.AwesomeFabMenu_menu_shadowColor, 0x66000000)
        mMenuShadowRadius = attr.getDimension(R.styleable.AwesomeFabMenu_menu_shadowRadius, mMenuShadowRadius)
        mMenuShadowXOffset = attr.getDimension(R.styleable.AwesomeFabMenu_menu_shadowXOffset, mMenuShadowXOffset)
        mMenuShadowYOffset = attr.getDimension(R.styleable.AwesomeFabMenu_menu_shadowYOffset, mMenuShadowYOffset)
        mMenuColorNormal = attr.getColor(R.styleable.AwesomeFabMenu_menu_colorNormal, -0x25bcca)
        mMenuColorPressed = attr.getColor(R.styleable.AwesomeFabMenu_menu_colorPressed, -0x18afbd)
        mMenuColorRipple = attr.getColor(R.styleable.AwesomeFabMenu_menu_colorRipple, -0x66000001)
        animationDelayPerItem = attr.getInt(R.styleable.AwesomeFabMenu_menu_animationDelayPerItem, 50)
        mIcon = attr.getDrawable(R.styleable.AwesomeFabMenu_menu_icon)
        if (mIcon == null) {
            mIcon = resources.getDrawable(R.drawable.fab_add)
        }
        mLabelsSingleLine = attr.getBoolean(R.styleable.AwesomeFabMenu_menu_labels_singleLine, false)
        mLabelsEllipsize = attr.getInt(R.styleable.AwesomeFabMenu_menu_labels_ellipsize, 0)
        mLabelsMaxLines = attr.getInt(R.styleable.AwesomeFabMenu_menu_labels_maxLines, -1)
        mMenuFabSize = attr.getInt(R.styleable.AwesomeFabMenu_menu_fab_size, AwesomeFab.SIZE_NORMAL)
        mLabelsStyle = attr.getResourceId(R.styleable.AwesomeFabMenu_menu_labels_style, 0)
        val customFont = attr.getString(R.styleable.AwesomeFabMenu_menu_labels_customFont)
        try {
            if (!TextUtils.isEmpty(customFont)) {
                mCustomTypefaceFromFont = Typeface.createFromAsset(getContext().assets, customFont)
            }
        } catch (ex: RuntimeException) {
            throw IllegalArgumentException("Unable to load specified custom font: " + customFont!!, ex)
        }

        mOpenDirection = attr.getInt(R.styleable.AwesomeFabMenu_menu_openDirection, OPEN_UP)
        mBackgroundColor = attr.getColor(R.styleable.AwesomeFabMenu_menu_backgroundColor, Color.TRANSPARENT)

        if (attr.hasValue(R.styleable.AwesomeFabMenu_menu_fab_label)) {
            mUsingMenuLabel = true
            mMenuLabelText = attr.getString(R.styleable.AwesomeFabMenu_menu_fab_label)
        }

        if (attr.hasValue(R.styleable.AwesomeFabMenu_menu_labels_padding)) {
            val padding = attr.getDimensionPixelSize(R.styleable.AwesomeFabMenu_menu_labels_padding, 0)
            initPadding(padding)
        }

        mOpenInterpolator = OvershootInterpolator()
        mCloseInterpolator = AnticipateInterpolator()
        mLabelsContext = ContextThemeWrapper(getContext(), mLabelsStyle)

        initBackgroundDimAnimation()
        createMenuButton()
        initMenuButtonAnimations(attr)

        attr.recycle()
    }

    private fun initMenuButtonAnimations(attr: TypedArray) {
        val showResId = attr.getResourceId(R.styleable.AwesomeFabMenu_menu_fab_show_animation, R.anim.fab_scale_up)
        setMenuButtonShowAnimation(AnimationUtils.loadAnimation(context, showResId))
        mImageToggleShowAnimation = AnimationUtils.loadAnimation(context, showResId)

        val hideResId = attr.getResourceId(R.styleable.AwesomeFabMenu_menu_fab_hide_animation, R.anim.fab_scale_down)
        setMenuButtonHideAnimation(AnimationUtils.loadAnimation(context, hideResId))
        mImageToggleHideAnimation = AnimationUtils.loadAnimation(context, hideResId)
    }

    private fun initBackgroundDimAnimation() {
        val maxAlpha = Color.alpha(mBackgroundColor)
        val red = Color.red(mBackgroundColor)
        val green = Color.green(mBackgroundColor)
        val blue = Color.blue(mBackgroundColor)

        mShowBackgroundAnimator = ValueAnimator.ofInt(0, maxAlpha)
        mShowBackgroundAnimator!!.duration = ANIMATION_DURATION.toLong()
        mShowBackgroundAnimator!!.addUpdateListener { animation ->
            val alpha = animation.animatedValue as Int
            setBackgroundColor(Color.argb(alpha, red, green, blue))
        }

        mHideBackgroundAnimator = ValueAnimator.ofInt(maxAlpha, 0)
        mHideBackgroundAnimator!!.duration = ANIMATION_DURATION.toLong()
        mHideBackgroundAnimator!!.addUpdateListener { animation ->
            val alpha = animation.animatedValue as Int
            setBackgroundColor(Color.argb(alpha, red, green, blue))
        }
    }

    private fun initPadding(padding: Int) {
        mLabelsPaddingTop = padding
        mLabelsPaddingRight = padding
        mLabelsPaddingBottom = padding
        mLabelsPaddingLeft = padding
    }

    private fun createMenuButton() {
        mMenuButton = AwesomeFab(context)

        mMenuButton!!.mShowShadow = mMenuShowShadow
        if (mMenuShowShadow) {
            mMenuButton!!.mShadowRadius = AwesomeFabUtil.dpToPx(context, mMenuShadowRadius)
            mMenuButton!!.mShadowXOffset = AwesomeFabUtil.dpToPx(context, mMenuShadowXOffset)
            mMenuButton!!.mShadowYOffset = AwesomeFabUtil.dpToPx(context, mMenuShadowYOffset)
        }
        mMenuButton!!.setColors(mMenuColorNormal, mMenuColorPressed, mMenuColorRipple)
        mMenuButton!!.mShadowColor = mMenuShadowColor
        mMenuButton!!.mFabSize = mMenuFabSize
        mMenuButton!!.updateBackground()
        mMenuButton!!.labelText = mMenuLabelText

        menuIconView = ImageView(context)
        menuIconView!!.setImageDrawable(mIcon)

        addView(mMenuButton, super.generateDefaultLayoutParams())
        addView(menuIconView)

        createDefaultIconAnimation()
    }

    private fun createDefaultIconAnimation() {
        val collapseAngle: Float
        val expandAngle: Float
        if (mOpenDirection == OPEN_UP) {
            collapseAngle = if (mLabelsPosition == LABELS_POSITION_LEFT) OPENED_PLUS_ROTATION_LEFT else OPENED_PLUS_ROTATION_RIGHT
            expandAngle = if (mLabelsPosition == LABELS_POSITION_LEFT) OPENED_PLUS_ROTATION_LEFT else OPENED_PLUS_ROTATION_RIGHT
        } else {
            collapseAngle = if (mLabelsPosition == LABELS_POSITION_LEFT) OPENED_PLUS_ROTATION_RIGHT else OPENED_PLUS_ROTATION_LEFT
            expandAngle = if (mLabelsPosition == LABELS_POSITION_LEFT) OPENED_PLUS_ROTATION_RIGHT else OPENED_PLUS_ROTATION_LEFT
        }

        val collapseAnimator = ObjectAnimator.ofFloat(
                menuIconView,
                "rotation",
                collapseAngle,
                CLOSED_PLUS_ROTATION
        )

        val expandAnimator = ObjectAnimator.ofFloat(
                menuIconView,
                "rotation",
                CLOSED_PLUS_ROTATION,
                expandAngle
        )

        mOpenAnimatorSet.play(expandAnimator)
        mCloseAnimatorSet.play(collapseAnimator)

        mOpenAnimatorSet.interpolator = mOpenInterpolator
        mCloseAnimatorSet.interpolator = mCloseInterpolator

        mOpenAnimatorSet.duration = ANIMATION_DURATION.toLong()
        mCloseAnimatorSet.duration = ANIMATION_DURATION.toLong()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = 0
        var height = 0
        mMaxButtonWidth = 0
        var maxLabelWidth = 0

        measureChildWithMargins(menuIconView, widthMeasureSpec, 0, heightMeasureSpec, 0)

        for (i in 0 until mButtonsCount) {
            val child = getChildAt(i)

            if (child.visibility == View.GONE || child === menuIconView) continue

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            mMaxButtonWidth = Math.max(mMaxButtonWidth, child.measuredWidth)
        }

        for (i in 0 until mButtonsCount) {
            var usedWidth = 0
            val child = getChildAt(i)

            if (child.visibility == View.GONE || child === menuIconView) continue

            usedWidth += child.measuredWidth
            height += child.measuredHeight

            val awesomeFabLabel = child.getTag(R.id.fab_label) as AwesomeFabLabel?
            if (awesomeFabLabel != null) {
                val labelOffset = (mMaxButtonWidth - child.measuredWidth) / if (mUsingMenuLabel) 1 else 2
                val labelUsedWidth = child.measuredWidth + awesomeFabLabel.calculateShadowWidth() + mLabelsMargin + labelOffset
                measureChildWithMargins(awesomeFabLabel, widthMeasureSpec, labelUsedWidth, heightMeasureSpec, 0)
                usedWidth += awesomeFabLabel.measuredWidth
                maxLabelWidth = Math.max(maxLabelWidth, usedWidth + labelOffset)
            }
        }

        width = Math.max(mMaxButtonWidth, maxLabelWidth + mLabelsMargin) + paddingLeft + paddingRight

        height += mButtonSpacing * (mButtonsCount - 1) + paddingTop + paddingBottom
        height = adjustForOvershoot(height)

        if (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            width = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        }

        if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            height = View.getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val buttonsHorizontalCenter = if (mLabelsPosition == LABELS_POSITION_LEFT)
            r - l - mMaxButtonWidth / 2 - paddingRight
        else
            mMaxButtonWidth / 2 + paddingLeft
        val openUp = mOpenDirection == OPEN_UP

        val menuButtonTop = if (openUp)
            b - t - mMenuButton!!.measuredHeight - paddingBottom
        else
            paddingTop
        val menuButtonLeft = buttonsHorizontalCenter - mMenuButton!!.measuredWidth / 2

        mMenuButton!!.layout(menuButtonLeft, menuButtonTop, menuButtonLeft + mMenuButton!!.measuredWidth,
                menuButtonTop + mMenuButton!!.measuredHeight)

        val imageLeft = buttonsHorizontalCenter - menuIconView!!.measuredWidth / 2
        val imageTop = menuButtonTop + mMenuButton!!.measuredHeight / 2 - menuIconView!!.measuredHeight / 2

        menuIconView!!.layout(imageLeft, imageTop, imageLeft + menuIconView!!.measuredWidth,
                imageTop + menuIconView!!.measuredHeight)

        var nextY = if (openUp)
            menuButtonTop + mMenuButton!!.measuredHeight + mButtonSpacing
        else
            menuButtonTop

        for (i in mButtonsCount - 1 downTo 0) {
            val child = getChildAt(i)

            if (child === menuIconView) continue

            val fab = child as AwesomeFab

            if (fab.visibility == View.GONE) continue

            val childX = buttonsHorizontalCenter - fab.measuredWidth / 2
            val childY = if (openUp) nextY - fab.measuredHeight - mButtonSpacing else nextY

            if (fab !== mMenuButton) {
                fab.layout(childX, childY, childX + fab.measuredWidth,
                        childY + fab.measuredHeight)

                if (!mIsMenuOpening) {
                    fab.hide(false)
                }
            }

            val label = fab.getTag(R.id.fab_label) as View?
            if (label != null) {
                val labelsOffset = (if (mUsingMenuLabel) mMaxButtonWidth / 2 else fab.measuredWidth / 2) + mLabelsMargin
                val labelXNearButton = if (mLabelsPosition == LABELS_POSITION_LEFT)
                    buttonsHorizontalCenter - labelsOffset
                else
                    buttonsHorizontalCenter + labelsOffset

                val labelXAwayFromButton = if (mLabelsPosition == LABELS_POSITION_LEFT)
                    labelXNearButton - label.measuredWidth
                else
                    labelXNearButton + label.measuredWidth

                val labelLeft = if (mLabelsPosition == LABELS_POSITION_LEFT)
                    labelXAwayFromButton
                else
                    labelXNearButton

                val labelRight = if (mLabelsPosition == LABELS_POSITION_LEFT)
                    labelXNearButton
                else
                    labelXAwayFromButton

                val labelTop = childY - mLabelsVerticalOffset + (fab.measuredHeight - label.measuredHeight) / 2

                label.layout(labelLeft, labelTop, labelRight, labelTop + label.measuredHeight)

                if (!mIsMenuOpening) {
                    label.visibility = View.INVISIBLE
                }
            }

            nextY = if (openUp)
                childY - mButtonSpacing
            else
                childY + child.getMeasuredHeight() + mButtonSpacing
        }
    }

    private fun adjustForOvershoot(dimension: Int): Int {
        return (dimension * 0.03 + dimension).toInt()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        bringChildToFront(mMenuButton)
        bringChildToFront(menuIconView)
        mButtonsCount = childCount
        createLabels()
    }

    private fun createLabels() {
        for (i in 0 until mButtonsCount) {

            if (getChildAt(i) === menuIconView) continue

            val fab = getChildAt(i) as AwesomeFab

            if (fab.getTag(R.id.fab_label) != null) continue

            addLabel(fab)

            if (fab === mMenuButton) {
                mMenuButton!!.setOnClickListener(OnClickListener { toggle(isAnimated) })
            }
        }
    }

    private fun addLabel(fab: AwesomeFab) {
        val text = fab.labelText

        if (TextUtils.isEmpty(text)) return

        val awesomeFabLabel = AwesomeFabLabel(mLabelsContext)
        awesomeFabLabel.isClickable = true
        awesomeFabLabel.setFab(fab)
        awesomeFabLabel.setShowAnimation(AnimationUtils.loadAnimation(context, mLabelsShowAnimation))
        awesomeFabLabel.setHideAnimation(AnimationUtils.loadAnimation(context, mLabelsHideAnimation))

        if (mLabelsStyle > 0) {
            awesomeFabLabel.setTextAppearance(context, mLabelsStyle)
            awesomeFabLabel.setShowShadow(false)
            awesomeFabLabel.setUsingStyle(true)
        } else {
            awesomeFabLabel.setColors(mLabelsColorNormal, mLabelsColorPressed, mLabelsColorRipple)
            awesomeFabLabel.setShowShadow(mLabelsShowShadow)
            awesomeFabLabel.setCornerRadius(mLabelsCornerRadius)
            if (mLabelsEllipsize > 0) {
                setLabelEllipsize(awesomeFabLabel)
            }
            awesomeFabLabel.maxLines = mLabelsMaxLines
            awesomeFabLabel.updateBackground()

            awesomeFabLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLabelsTextSize)
            awesomeFabLabel.setTextColor(mLabelsTextColor)

            var left = mLabelsPaddingLeft
            var top = mLabelsPaddingTop
            if (mLabelsShowShadow) {
                left += fab.shadowRadius + Math.abs(fab.shadowXOffset)
                top += fab.shadowRadius + Math.abs(fab.shadowYOffset)
            }

            awesomeFabLabel.setPadding(
                    left,
                    top,
                    mLabelsPaddingLeft,
                    mLabelsPaddingTop
            )

            if (mLabelsMaxLines < 0 || mLabelsSingleLine) {
                awesomeFabLabel.setSingleLine(mLabelsSingleLine)
            }
        }

        if (mCustomTypefaceFromFont != null) {
            awesomeFabLabel.typeface = mCustomTypefaceFromFont
        }
        awesomeFabLabel.text = text
        awesomeFabLabel.setOnClickListener(fab.getOnClickListener())

        addView(awesomeFabLabel)
        fab.setTag(R.id.fab_label, awesomeFabLabel)
    }

    private fun setLabelEllipsize(awesomeFabLabel: AwesomeFabLabel) {
        when (mLabelsEllipsize) {
            1 -> awesomeFabLabel.ellipsize = TextUtils.TruncateAt.START
            2 -> awesomeFabLabel.ellipsize = TextUtils.TruncateAt.MIDDLE
            3 -> awesomeFabLabel.ellipsize = TextUtils.TruncateAt.END
            4 -> awesomeFabLabel.ellipsize = TextUtils.TruncateAt.MARQUEE
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.MarginLayoutParams {
        return ViewGroup.MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.MarginLayoutParams {
        return ViewGroup.MarginLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.MarginLayoutParams {
        return ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is ViewGroup.MarginLayoutParams
    }

    private fun hideMenuButtonWithImage(animate: Boolean) {
        if (!isMenuButtonHidden) {
            mMenuButton!!.hide(animate)
            if (animate) {
                menuIconView!!.startAnimation(mImageToggleHideAnimation)
            }
            menuIconView!!.visibility = View.INVISIBLE
            mIsMenuButtonAnimationRunning = false
        }
    }

    private fun showMenuButtonWithImage(animate: Boolean) {
        if (isMenuButtonHidden) {
            mMenuButton!!.show(animate)
            if (animate) {
                menuIconView!!.startAnimation(mImageToggleShowAnimation)
            }
            menuIconView!!.visibility = View.VISIBLE
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mIsSetClosedOnTouchOutside) {
            var handled = false
            when (event.action) {
                MotionEvent.ACTION_DOWN -> handled = isOpened
                MotionEvent.ACTION_UP -> {
                    close(isAnimated)
                    handled = true
                }
            }

            return handled
        }

        return super.onTouchEvent(event)
    }

    fun toggle(animate: Boolean) {
        if (isOpened) {
            close(animate)
        } else {
            open(animate)
        }
    }

    fun open(animate: Boolean) {
        if (!isOpened) {
            if (isBackgroundEnabled) {
                mShowBackgroundAnimator!!.start()
            }

            if (isIconAnimated) {
                if (iconToggleAnimatorSet != null) {
                    iconToggleAnimatorSet!!.start()
                } else {
                    mCloseAnimatorSet.cancel()
                    mOpenAnimatorSet.start()
                }
            }

            var delay = 0
            var counter = 0
            mIsMenuOpening = true
            for (i in childCount - 1 downTo 0) {
                val child = getChildAt(i)
                if (child is AwesomeFab && child.getVisibility() != View.GONE) {
                    counter++

                    mUiHandler.postDelayed(Runnable {
                        if (isOpened) return@Runnable

                        if (child !== mMenuButton) {
                            child.show(animate)
                        }

                        val awesomeFabLabel = child.getTag(R.id.fab_label) as AwesomeFabLabel?
                        if (awesomeFabLabel != null && awesomeFabLabel.isHandleVisibilityChanges) {
                            awesomeFabLabel.show(animate)
                        }
                    }, delay.toLong())
                    delay += animationDelayPerItem
                }
            }

            mUiHandler.postDelayed({
                isOpened = true

                if (mToggleListener != null) {
                    mToggleListener!!.onMenuToggle(true)
                }
            }, (++counter * animationDelayPerItem).toLong())
        }
    }

    fun close(animate: Boolean) {
        if (isOpened) {
            if (isBackgroundEnabled) {
                mHideBackgroundAnimator!!.start()
            }

            if (isIconAnimated) {
                if (iconToggleAnimatorSet != null) {
                    iconToggleAnimatorSet!!.start()
                } else {
                    mCloseAnimatorSet.start()
                    mOpenAnimatorSet.cancel()
                }
            }

            var delay = 0
            var counter = 0
            mIsMenuOpening = false
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child is AwesomeFab && child.getVisibility() != View.GONE) {
                    counter++

                    mUiHandler.postDelayed(Runnable {
                        if (!isOpened) return@Runnable

                        if (child !== mMenuButton) {
                            child.hide(animate)
                        }

                        val awesomeFabLabel = child.getTag(R.id.fab_label) as AwesomeFabLabel?
                        if (awesomeFabLabel != null && awesomeFabLabel.isHandleVisibilityChanges) {
                            awesomeFabLabel.hide(animate)
                        }
                    }, delay.toLong())
                    delay += animationDelayPerItem
                }
            }

            mUiHandler.postDelayed({
                isOpened = false

                if (mToggleListener != null) {
                    mToggleListener!!.onMenuToggle(false)
                }
            }, (++counter * animationDelayPerItem).toLong())
        }
    }

    /**
     * Sets the [Interpolator] for **FloatingActionButton's** icon animation.
     *
     * @param interpolator the Interpolator to be used in animation
     */
    fun setIconAnimationInterpolator(interpolator: Interpolator) {
        mOpenAnimatorSet.interpolator = interpolator
        mCloseAnimatorSet.interpolator = interpolator
    }

    fun setIconAnimationOpenInterpolator(openInterpolator: Interpolator) {
        mOpenAnimatorSet.interpolator = openInterpolator
    }

    fun setIconAnimationCloseInterpolator(closeInterpolator: Interpolator) {
        mCloseAnimatorSet.interpolator = closeInterpolator
    }

    fun setOnMenuToggleListener(listener: OnMenuToggleListener) {
        mToggleListener = listener
    }

    fun setMenuButtonShowAnimation(showAnimation: Animation) {
        mMenuButtonShowAnimation = showAnimation
        mMenuButton!!.showAnimation = showAnimation
    }

    fun setMenuButtonHideAnimation(hideAnimation: Animation) {
        mMenuButtonHideAnimation = hideAnimation
        mMenuButton!!.hideAnimation = hideAnimation
    }

    /**
     * Makes the whole [.AwesomeFabMenu] to appear and sets its visibility to [.VISIBLE]
     *
     * @param animate if true - plays "show animation"
     */
    fun showMenu(animate: Boolean) {
        if (isMenuHidden) {
            if (animate) {
                startAnimation(mMenuButtonShowAnimation)
            }
            visibility = View.VISIBLE
        }
    }

    /**
     * Makes the [.AwesomeFabMenu] to disappear and sets its visibility to [.INVISIBLE]
     *
     * @param animate if true - plays "hide animation"
     */
    fun hideMenu(animate: Boolean) {
        if (!isMenuHidden && !mIsMenuButtonAnimationRunning) {
            mIsMenuButtonAnimationRunning = true
            if (isOpened) {
                close(animate)
                mUiHandler.postDelayed({
                    if (animate) {
                        startAnimation(mMenuButtonHideAnimation)
                    }
                    visibility = View.INVISIBLE
                    mIsMenuButtonAnimationRunning = false
                }, (animationDelayPerItem * mButtonsCount).toLong())
            } else {
                if (animate) {
                    startAnimation(mMenuButtonHideAnimation)
                }
                visibility = View.INVISIBLE
                mIsMenuButtonAnimationRunning = false
            }
        }
    }

    fun toggleMenu(animate: Boolean) {
        if (isMenuHidden) {
            showMenu(animate)
        } else {
            hideMenu(animate)
        }
    }

    /**
     * Makes the [AwesomeFab] to appear inside the [.AwesomeFabMenu] and
     * sets its visibility to [.VISIBLE]
     *
     * @param animate if true - plays "show animation"
     */
    fun showMenuButton(animate: Boolean) {
        if (isMenuButtonHidden) {
            showMenuButtonWithImage(animate)
        }
    }

    /**
     * Makes the [AwesomeFab] to disappear inside the [.AwesomeFabMenu] and
     * sets its visibility to [.INVISIBLE]
     *
     * @param animate if true - plays "hide animation"
     */
    fun hideMenuButton(animate: Boolean) {
        if (!isMenuButtonHidden && !mIsMenuButtonAnimationRunning) {
            mIsMenuButtonAnimationRunning = true
            if (isOpened) {
                close(animate)
                mUiHandler.postDelayed({ hideMenuButtonWithImage(animate) }, (animationDelayPerItem * mButtonsCount).toLong())
            } else {
                hideMenuButtonWithImage(animate)
            }
        }
    }

    fun toggleMenuButton(animate: Boolean) {
        if (isMenuButtonHidden) {
            showMenuButton(animate)
        } else {
            hideMenuButton(animate)
        }
    }

    fun setClosedOnTouchOutside(close: Boolean) {
        mIsSetClosedOnTouchOutside = close
    }

    fun setMenuButtonColorNormalResId(colorResId: Int) {
        mMenuColorNormal = resources.getColor(colorResId)
        mMenuButton!!.setColorNormalResId(colorResId)
    }

    fun setMenuButtonColorPressedResId(colorResId: Int) {
        mMenuColorPressed = resources.getColor(colorResId)
        mMenuButton!!.setColorPressedResId(colorResId)
    }

    fun setMenuButtonColorRippleResId(colorResId: Int) {
        mMenuColorRipple = resources.getColor(colorResId)
        mMenuButton!!.setColorRippleResId(colorResId)
    }

    fun addMenuButton(fab: AwesomeFab) {
        addView(fab, mButtonsCount - 2)
        mButtonsCount++
        addLabel(fab)
    }

    fun removeMenuButton(fab: AwesomeFab) {
        removeView(fab.labelView)
        removeView(fab)
        mButtonsCount--
    }

    fun addMenuButton(fab: AwesomeFab, index: Int) {
        var index = index
        val size = mButtonsCount - 2
        if (index < 0) {
            index = 0
        } else if (index > size) {
            index = size
        }

        addView(fab, index)
        mButtonsCount++
        addLabel(fab)
    }

    fun removeAllMenuButtons() {
        close(true)

        val viewsToRemove = ArrayList<AwesomeFab>()
        for (i in 0 until childCount) {
            val v = getChildAt(i)
            if (v !== mMenuButton && v !== menuIconView && v is AwesomeFab) {
                viewsToRemove.add(v)
            }
        }
        for (v in viewsToRemove) {
            removeMenuButton(v)
        }
    }

    fun setOnMenuButtonClickListener(clickListener: View.OnClickListener) {
        mMenuButton!!.setOnClickListener(clickListener)
    }

    fun setOnMenuButtonLongClickListener(longClickListener: View.OnLongClickListener) {
        mMenuButton!!.setOnLongClickListener(longClickListener)
    }

    companion object {

        private val ANIMATION_DURATION = 300
        private val CLOSED_PLUS_ROTATION = 0f
        private val OPENED_PLUS_ROTATION_LEFT = -90f - 45f
        private val OPENED_PLUS_ROTATION_RIGHT = 90f + 45f

        private val OPEN_UP = 0
        private val OPEN_DOWN = 1

        private val LABELS_POSITION_LEFT = 0
        private val LABELS_POSITION_RIGHT = 1
    }
}
