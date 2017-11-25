package com.awesome.layouts

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout

import com.awesome.R

import java.util.ArrayList

class ExpandableRelativeLayout : RelativeLayout, ExpandableLayout {

    private var duration: Int = 0
    private var interpolator: TimeInterpolator = LinearInterpolator()
    private var orientation: Int = 0
    /**
     * Default state of expanse
     *
     * @see .defaultChildIndex
     *
     * @see .defaultPosition
     */
    private var defaultExpanded: Boolean = false
    /**
     * You cannot define [.defaultExpanded], [.defaultChildIndex]
     * and [.defaultPosition] at the same time.
     * [.defaultPosition] has priority over [.defaultExpanded]
     * and [.defaultChildIndex] if you set them at the same time.
     *
     *
     *
     *
     * Priority
     * [.defaultPosition] > [.defaultChildIndex] > [.defaultExpanded]
     */
    private var defaultChildIndex: Int = 0
    private var defaultPosition: Int = 0
    /**
     * The close position is width from left of layout if orientation is horizontal.
     * The close position is height from top of layout if orientation is vertical.
     */
    /**
     * Gets the width from left of layout if orientation is horizontal.
     * Gets the height from top of layout if orientation is vertical.
     *
     * @return
     *
     * @see .closePosition
     */
    /**
     * Sets the close position directly.
     *
     * @param position
     *
     * @see .closePosition
     *
     * @see .setClosePositionIndex
     */
    var closePosition = 20

    private var listener: ExpandableLayoutListener? = null
    private var savedState: ExpandableSavedState? = null
    private var isExpanded: Boolean = false
    private var layoutSize = 0
    private var isArranged = false
    private var isCalculatedSize = false
    private var isAnimating = false
    /**
     * view size of children
     */
    private val childSizeList = ArrayList<Int>()
    /**
     * view position top or left of children
     */
    private val childPositionList = ArrayList<Int>()
    private var mGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    /**
     * Gets the current position.
     *
     * @return
     */
    val currentPosition: Int
        get() = if (isVertical) measuredHeight else measuredWidth

    private val isVertical: Boolean
        get() = orientation == ExpandableLayout.VERTICAL

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                              defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet,
                defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.expandableLayout, defStyleAttr, 0)
        duration = 1500
        defaultExpanded = a.getBoolean(R.styleable.expandableLayout_ael_expanded, ExpandableLayout.DEFAULT_EXPANDED)
        orientation = a.getInteger(R.styleable.expandableLayout_ael_orientation, ExpandableLayout.VERTICAL)
        defaultChildIndex = a.getInteger(R.styleable.expandableLayout_ael_defaultChildIndex,
                Integer.MAX_VALUE)
        defaultPosition = a.getDimensionPixelSize(R.styleable.expandableLayout_ael_defaultPosition,
                Integer.MIN_VALUE)
        val interpolatorType = a.getInteger(R.styleable.expandableLayout_ael_interpolator,
                com.awesome.utils.Utils.LINEAR_INTERPOLATOR)
        a.recycle()
        interpolator = com.awesome.utils.Utils.createInterpolator(interpolatorType)
        isExpanded = defaultExpanded
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (isCalculatedSize) return

        val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        if (isVertical) {
            val measuredHeight = measuredHeight

            super.onMeasure(widthMeasureSpec, measureSpec)
            layoutSize = getMeasuredHeight()

            setMeasuredDimension(measuredWidth, measuredHeight)
        } else {
            val measuredWidth = measuredWidth

            super.onMeasure(measureSpec, heightMeasureSpec)
            layoutSize = getMeasuredWidth()

            setMeasuredDimension(measuredWidth, measuredHeight)
        }

        // calculate a size of children
        childSizeList.clear()
        var view: View
        var params: RelativeLayout.LayoutParams
        for (i in 0 until childCount) {
            view = getChildAt(i)
            params = view.layoutParams as RelativeLayout.LayoutParams

            childSizeList.add(if (isVertical)
                view.measuredHeight + params.topMargin + params.bottomMargin
            else
                view.measuredWidth + params.leftMargin + params.rightMargin)
        }
        isCalculatedSize = true
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        if (isArranged) return

        childPositionList.clear()
        // calculate a top position of children
        for (i in 0 until childCount) {
            childPositionList.add((if (isVertical) getChildAt(i).y else getChildAt(i).x).toInt())
        }

        // adjust default position if a user set a value.
        if (!defaultExpanded) {
            setLayoutSize(closePosition)
        }
        val childNumbers = childSizeList.size
        if (childNumbers > defaultChildIndex && childNumbers > 0) {
            moveChild(defaultChildIndex, 0, null)
        }
        if (defaultPosition > 0 && layoutSize >= defaultPosition && layoutSize > 0) {
            move(defaultPosition, 0, null)
        }
        isArranged = true

        if (savedState == null) return
        setLayoutSize(savedState!!.size)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val parcelable = super.onSaveInstanceState()
        val ss = ExpandableSavedState(parcelable)
        ss.size = currentPosition
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is ExpandableSavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superState)
        savedState = state
    }

    /**
     * {@inheritDoc}
     */
    override fun setListener(listener: ExpandableLayoutListener) {
        this.listener = listener
    }

    /**
     * {@inheritDoc}
     */
    override fun toggle() {
        toggle(duration.toLong(), interpolator)
    }

    /**
     * {@inheritDoc}
     */
    override fun toggle(duration: Long, interpolator: TimeInterpolator?) {
        if (closePosition < currentPosition) {
            collapse(duration, interpolator)
        } else {
            expand(duration, interpolator)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun expand() {
        if (isAnimating) return

        createExpandAnimator(currentPosition, layoutSize, duration.toLong(), interpolator).start()
    }

    /**
     * {@inheritDoc}
     */
    override fun expand(duration: Long, interpolator: TimeInterpolator?) {
        if (isAnimating) return

        if (duration <= 0) {
            move(layoutSize, duration, interpolator)
            return
        }
        createExpandAnimator(currentPosition, layoutSize, duration, interpolator).start()
    }

    /**
     * {@inheritDoc}
     */
    override fun collapse() {
        if (isAnimating) return

        createExpandAnimator(currentPosition, closePosition, duration.toLong(), interpolator).start()
    }

    /**
     * {@inheritDoc}
     */
    override fun collapse(duration: Long, interpolator: TimeInterpolator?) {
        if (isAnimating) return

        if (duration <= 0) {
            move(closePosition, duration, interpolator)
            return
        }
        createExpandAnimator(currentPosition, closePosition, duration, interpolator).start()
    }

    /**
     * {@inheritDoc}
     */
    override fun setDuration(duration: Int) {
        if (duration < 0) {
            throw IllegalArgumentException("Animators cannot have negative duration: " + duration)
        }
        this.duration = duration
    }

    /**
     * {@inheritDoc}
     */
    fun setExpanded(expanded: Boolean) {
        val currentPosition = currentPosition
        if (expanded && currentPosition == layoutSize || !expanded && currentPosition == closePosition)
            return

        isExpanded = expanded
        setLayoutSize(if (expanded) layoutSize else closePosition)
        requestLayout()
    }

    /**
     * {@inheritDoc}
     */
    fun isExpanded(): Boolean {
        return isExpanded
    }

    /**
     * {@inheritDoc}
     */
    override fun setInterpolator(interpolator: TimeInterpolator) {
        this.interpolator = interpolator
    }

    /**
     * Moves to position.
     * Sets 0 to duration if you want to move immediately.
     *
     * @param position
     * @param duration
     * @param interpolator use the default interpolator if the argument is null.
     */
    @JvmOverloads
    fun move(position: Int, duration: Long , interpolator: TimeInterpolator?) {
        if (isAnimating || 0 > position || layoutSize < position) return

        if (duration <= 0) {
            isExpanded = position > closePosition
            setLayoutSize(position)
            requestLayout()
            notifyListeners()
            return
        }
        createExpandAnimator(currentPosition, position, duration,
                interpolator ?: this.interpolator).start()
    }

    /**
     * Moves to bottom(VERTICAL) or right(HORIZONTAL) of child view
     * Sets 0 to duration if you want to move immediately.
     *
     * @param index        index child view index
     * @param duration
     * @param interpolator use the default interpolator if the argument is null.
     */
    @JvmOverloads
    fun moveChild(index: Int, duration: Long, interpolator: TimeInterpolator?) {
        if (isAnimating) return

        val destination = getChildPosition(index) + if (isVertical) paddingBottom else paddingRight
        if (duration <= 0) {
            isExpanded = destination > closePosition
            setLayoutSize(destination)
            requestLayout()
            notifyListeners()
            return
        }
        createExpandAnimator(currentPosition, destination,
                duration, interpolator ?: this.interpolator).start()
    }

    /**
     * Sets orientation of expanse animation.
     *
     * @param orientation Set 0 if orientation is horizontal, 1 if orientation is vertical
     */
    fun setOrientation(@ExpandableLayout.Orientation orientation: Int) {
        this.orientation = orientation
    }

    /**
     * Gets the width from left of layout if orientation is horizontal.
     * Gets the height from top of layout if orientation is vertical.
     *
     * @param index index of child view
     *
     * @return position from top or left
     */
    fun getChildPosition(index: Int): Int {
        if (0 > index || childSizeList.size <= index) {
            throw IllegalArgumentException("There aren't the view having this index.")
        }
        return childPositionList[index] + childSizeList[index]
    }

    /**
     * Sets close position using index of child view.
     *
     * @param childIndex
     *
     * @see .closePosition
     *
     * @see .setClosePosition
     */
    fun setClosePositionIndex(childIndex: Int) {
        this.closePosition = getChildPosition(childIndex)
    }

    private fun setLayoutSize(size: Int) {
        if (isVertical) {
            layoutParams.height = size
        } else {
            layoutParams.width = size
        }
    }

    /**
     * Creates value animator.
     * Expand the layout if {@param to} is bigger than {@param from}.
     * Collapse the layout if {@param from} is bigger than {@param to}.
     *
     * @param from
     * @param to
     * @param duration
     * @param interpolator
     *
     * @return
     */
    private fun createExpandAnimator(
            from: Int, to: Int, duration: Long, interpolator: TimeInterpolator?): ValueAnimator {
        val valueAnimator = ValueAnimator.ofInt(from, to)
        valueAnimator.duration = duration
        valueAnimator.interpolator = interpolator
        valueAnimator.addUpdateListener { animator ->
            if (isVertical) {
                layoutParams.height = animator.animatedValue as Int
            } else {
                layoutParams.width = animator.animatedValue as Int
            }
            requestLayout()
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animator: Animator) {
                isAnimating = true
                if (listener == null) return

                listener!!.onAnimationStart()
                if (layoutSize == to) {
                    listener!!.onPreOpen()
                    return
                }
                if (closePosition == to) {
                    listener!!.onPreClose()
                }
            }

            override fun onAnimationEnd(animator: Animator) {
                isAnimating = false
                isExpanded = to > closePosition

                if (listener == null) return

                listener!!.onAnimationEnd()
                if (to == layoutSize) {
                    listener!!.onOpened()
                    return
                }
                if (to == closePosition) {
                    listener!!.onClosed()
                }
            }
        })
        return valueAnimator
    }

    /**
     * Notify listeners
     */
    private fun notifyListeners() {
        if (listener == null) return

        listener!!.onAnimationStart()
        if (isExpanded) {
            listener!!.onPreOpen()
        } else {
            listener!!.onPreClose()
        }
        mGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                viewTreeObserver.removeGlobalOnLayoutListener(mGlobalLayoutListener)
            } else {
                viewTreeObserver.removeOnGlobalLayoutListener(mGlobalLayoutListener)
            }

            listener!!.onAnimationEnd()
            if (isExpanded) {
                listener!!.onOpened()
            } else {
                listener!!.onClosed()
            }
        }
        viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener)
    }
}
/**
 * @param position
 *
 * @see .move
 */
/**
 * @param index child view index
 *
 * @see .moveChild
 */