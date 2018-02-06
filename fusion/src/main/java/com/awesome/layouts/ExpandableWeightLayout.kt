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
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.RelativeLayout

import com.awesome.R

class ExpandableWeightLayout : RelativeLayout, ExpandableLayout {

    private var duration: Int = 0
    private var interpolator: TimeInterpolator = LinearInterpolator()
    private var defaultExpanded: Boolean = false

    private var listener: ExpandableLayoutListener? = null
    private var savedState: ExpandableSavedState? = null
    private var isExpanded: Boolean = false
    private var layoutWeight = 0.0f
    private var isArranged = false
    private var isCalculatedSize = false
    private var isAnimating = false
    private var mGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    /**
     * Gets current weight of expandable layout.
     *
     * @return weight
     */
    val currentWeight: Float
        get() = (layoutParams as LinearLayout.LayoutParams).weight

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
        duration = a.getInteger(R.styleable.expandableLayout_ael_duration, ExpandableLayout.DEFAULT_DURATION)
        defaultExpanded = a.getBoolean(R.styleable.expandableLayout_ael_expanded, ExpandableLayout.DEFAULT_EXPANDED)
        val interpolatorType = a.getInteger(R.styleable.expandableLayout_ael_interpolator,
                com.awesome.utils.Utils.LINEAR_INTERPOLATOR)
        a.recycle()
        interpolator = com.awesome.utils.Utils.createInterpolator(interpolatorType)
        isExpanded = defaultExpanded
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        // Check this layout using the attribute of weight
        if (layoutParams !is LinearLayout.LayoutParams) {
            throw AssertionError("You must arrange in LinearLayout.")
        }
        if (0 >= currentWeight) throw AssertionError("You must set a weight than 0.")

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (!isCalculatedSize) {
            layoutWeight = currentWeight
            isCalculatedSize = true
        }

        if (isArranged) return
        setWeight(if (defaultExpanded) layoutWeight else 0F)
        isArranged = true

        if (savedState == null) return
        setWeight(savedState!!.weight)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val parcelable = super.onSaveInstanceState()

        val ss = ExpandableSavedState(parcelable)
        ss.weight = currentWeight
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
        if (0 < currentWeight) {
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

        createExpandAnimator(0f, layoutWeight, duration.toLong(), interpolator).start()
    }

    /**
     * {@inheritDoc}
     */
    override fun expand(duration: Long, interpolator: TimeInterpolator?) {
        if (isAnimating) return

        if (duration <= 0) {
            isExpanded = true
            setWeight(layoutWeight)
            requestLayout()
            notifyListeners()
            return
        }
        createExpandAnimator(currentWeight, layoutWeight, duration, interpolator).start()
    }

    /**
     * {@inheritDoc}
     */
    override fun collapse() {
        if (isAnimating) return

        createExpandAnimator(currentWeight, 0f, duration.toLong(), interpolator).start()
    }

    /**
     * {@inheritDoc}
     */
    override fun collapse(duration: Long, interpolator: TimeInterpolator?) {
        if (isAnimating) return

        if (duration <= 0) {
            isExpanded = false
            setWeight(0f)
            requestLayout()
            notifyListeners()
            return
        }
        createExpandAnimator(currentWeight, 0f, duration, interpolator).start()
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
        val currentWeight = currentWeight
        if (expanded && currentWeight == layoutWeight || !expanded && currentWeight == 0f)
            return

        isExpanded = expanded
        setWeight(if (expanded) layoutWeight else 0f)
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
     * Sets weight of expandable layout.
     *
     * @param expandWeight expand to this weight by [.expand]
     */
    fun setExpandWeight(expandWeight: Float) {
        layoutWeight = expandWeight
    }

    /**
     * Change to weight.
     * Sets 0 to duration if you want to move immediately.
     *
     * @param weight
     * @param duration
     * @param interpolator use the default interpolator if the argument is null.
     */
    @JvmOverloads
    fun move(weight: Float, duration: Long, interpolator: TimeInterpolator?) {
        if (isAnimating) return

        if (duration <= 0L) {
            isExpanded = weight > 0
            setWeight(weight)
            requestLayout()
            notifyListeners()
            return
        }
        createExpandAnimator(currentWeight, weight, duration, interpolator).start()
    }

    /**
     * Creates value animator.
     * Expand the layout if @param.to is bigger than @param.from.
     * Collapse the layout if @param.from is bigger than @param.to.
     *
     * @param from
     * @param to
     * @param duration
     * @param interpolator TimeInterpolator
     *
     * @return
     */
    private fun createExpandAnimator(from: Float, to: Float, duration: Long,
                                     interpolator: TimeInterpolator?): ValueAnimator {
        val valueAnimator = ValueAnimator.ofFloat(from, to)
        valueAnimator.duration = duration
        valueAnimator.interpolator = interpolator ?: this.interpolator
        valueAnimator.addUpdateListener { animation ->
            setWeight(animation.animatedValue as Float)
            requestLayout()
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                isAnimating = true

                if (listener == null) return

                listener!!.onAnimationStart()
                if (layoutWeight == to) {
                    listener!!.onPreOpen()
                    return
                }
                if (0f == to) {
                    listener!!.onPreClose()
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                isAnimating = false
                isExpanded = to > 0

                if (listener == null) return

                listener!!.onAnimationEnd()
                if (to == layoutWeight) {
                    listener!!.onOpened()
                    return
                }
                if (to == 0f) {
                    listener!!.onClosed()
                }
            }
        })
        return valueAnimator
    }

    private fun setWeight(weight: Float) {
        (layoutParams as LinearLayout.LayoutParams).weight = weight
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
 * @param weight
 *
 * @see .move
 */