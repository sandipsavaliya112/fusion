package com.awesome.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.awesome.R
import com.awesome.layouts.AwesomeDiagonal
import com.awesome.layouts.ExpandableLayoutListener
import com.awesome.layouts.ExpandableRelativeLayout
import com.awesome.utils.ResizeAnimator


/**
 * Created by Sandip Savaliya on 16.04.2016.
 */
abstract class AwesomeBase<T : AwesomeBase<T>> {

    var dialog: Dialog? = null
    private var dialogView: View? = null

    lateinit var vSwitcher: RelativeLayout

    lateinit var vSwitcherDiagonal: AwesomeDiagonal
    lateinit var topButton: ImageView
    lateinit var bottomButton: ImageView
    lateinit var expandableLayout: ExpandableRelativeLayout
    lateinit var expandableLayoutTwo: ExpandableRelativeLayout

    lateinit var ll_left: LinearLayout
    lateinit var ll_right: LinearLayout

    private var iconView: ImageView? = null
    private var topTitleView: TextView? = null
    private var titleView: TextView? = null
    private var messageView: TextView? = null
    internal var flagInc = 0
    internal var firstFlag = true

    @get:LayoutRes
    protected abstract val layout: Int

    internal val isShowing: Boolean
        get() = dialog != null && dialog!!.isShowing

    protected val context: Context
        get() = dialogView!!.context

    constructor(context: Context) {
        init(AlertDialog.Builder(context))
    }

    constructor(context: Context, theme: Int) {
        init(AlertDialog.Builder(context, theme))
    }

    private fun init(dialogBuilder: AlertDialog.Builder) {
        dialogView = LayoutInflater.from(dialogBuilder.context).inflate(layout, null)
        dialog = dialogBuilder.setView(dialogView).create()

        if (layout == R.layout.dialog_top_bottom) {
            vSwitcher = findView(R.id.vSwitcher)
            vSwitcherDiagonal = findView(R.id.vSwitcherDiagonal)
            expandableLayout = findView(R.id.expandableLayout)
            expandableLayoutTwo = findView(R.id.expandableLayoutTwo)
            topButton = findView(R.id.topButton)
            bottomButton = findView(R.id.bottomButton)
            ll_left = findView(R.id.ll_left)
            ll_right = findView(R.id.ll_right)

            expandableLayout.collapse()
            expandableLayoutTwo.collapse()

            expandableLayout.setListener(object : ExpandableLayoutListener {
                override fun onAnimationStart() {

                    if (firstFlag) {
                        val vSwitcherDiagonAnim = ResizeAnimator(vSwitcherDiagonal, 0, 165, firstFlag)
                        vSwitcherDiagonAnim.interpolator = AccelerateDecelerateInterpolator()
                        vSwitcherDiagonAnim.duration = 1500
                        vSwitcherDiagonal.startAnimation(vSwitcherDiagonAnim)

                        val topButtonAnim = ResizeAnimator(topButton, 88, 88, false)
                        topButtonAnim.interpolator = AccelerateDecelerateInterpolator()
                        topButtonAnim.duration = 1500
                        topButton.startAnimation(topButtonAnim)

                        val bottomButtonAnim = ResizeAnimator(bottomButton, 88, 88, false)
                        bottomButtonAnim.interpolator = AccelerateDecelerateInterpolator()
                        bottomButtonAnim.duration = 1500
                        bottomButton.startAnimation(bottomButtonAnim)

                        val vSwitcherAnim = ResizeAnimator(vSwitcher, 0, 190, false)
                        vSwitcherAnim.interpolator = AccelerateDecelerateInterpolator()
                        vSwitcherAnim.duration = 1500
                        vSwitcher.startAnimation(vSwitcherAnim)
                    }
                }

                override fun onAnimationEnd() {
                    flagInc = flagInc + 1
                    if (flagInc >= 3) {
                        firstFlag = false
                    }
                }

                override fun onPreOpen() {}

                override fun onPreClose() {}

                override fun onOpened() {}

                override fun onClosed() {}
            })


            expandableLayoutTwo.setListener(object : ExpandableLayoutListener {
                override fun onAnimationStart() {

                    if (firstFlag) {
                        val vSwitcherDiagonAnim = ResizeAnimator(vSwitcherDiagonal, 0, 165, firstFlag)
                        vSwitcherDiagonAnim.interpolator = AccelerateDecelerateInterpolator()
                        vSwitcherDiagonAnim.duration = 1500
                        vSwitcherDiagonal.startAnimation(vSwitcherDiagonAnim)

                        val topButtonAnim = ResizeAnimator(topButton, 88, 88, false)
                        topButtonAnim.interpolator = AccelerateDecelerateInterpolator()
                        topButtonAnim.duration = 1500
                        topButton.startAnimation(topButtonAnim)

                        val bottomButtonAnim = ResizeAnimator(bottomButton, 88, 88, false)
                        bottomButtonAnim.interpolator = AccelerateDecelerateInterpolator()
                        bottomButtonAnim.duration = 1500
                        bottomButton.startAnimation(bottomButtonAnim)

                        val vSwitcherAnim = ResizeAnimator(vSwitcher, 0, 190, false)
                        vSwitcherAnim.interpolator = AccelerateDecelerateInterpolator()
                        vSwitcherAnim.duration = 1500
                        vSwitcher.startAnimation(vSwitcherAnim)
                    }
                }

                override fun onAnimationEnd() {
                    flagInc = flagInc + 1
                    if (flagInc >= 3) {
                        firstFlag = false
                    }
                }

                override fun onPreOpen() {}

                override fun onPreClose() {}

                override fun onOpened() {}

                override fun onClosed() {}
            })

            ll_left.setOnClickListener {
                expandableLayout.expand()
                expandableLayoutTwo.collapse()
            }

            ll_right.setOnClickListener {
                expandableLayout.collapse()
                expandableLayoutTwo.expand()
            }
        } else {
            iconView = findView(R.id.ld_icon)
            titleView = findView(R.id.ld_title)
            messageView = findView(R.id.ld_message)
            topTitleView = findView(R.id.ld_top_title)
        }
    }

    fun setMessage(@StringRes message: Int): T {
        return setMessage(string(message))
    }

    fun setMessage(message: CharSequence): T {
        messageView!!.visibility = View.VISIBLE
        messageView!!.text = message
        return this as T
    }

    fun setTitle(@StringRes title: Int): T {
        return setTitle(string(title))
    }

    fun setTopTitle(@StringRes title: Int): T {
        return setTopTitle(string(title))
    }

    fun setTitle(title: CharSequence): T {
        titleView!!.visibility = View.VISIBLE
        titleView!!.text = title
        return this as T
    }

    fun setTopTitle(title: CharSequence): T {
        topTitleView!!.visibility = View.VISIBLE
        topTitleView!!.text = title
        return this as T
    }

    fun setTopTitleColor(color: Int): T {
        topTitleView!!.setTextColor(color)
        return this as T
    }

    fun setIcon(bitmap: Bitmap): T {
        iconView!!.visibility = View.VISIBLE
        iconView!!.setImageBitmap(bitmap)
        return this as T
    }

    fun setIcon(drawable: Drawable): T {
        iconView!!.visibility = View.VISIBLE
        iconView!!.setImageDrawable(drawable)
        return this as T
    }

    fun setIcon(@DrawableRes iconRes: Int): T {
        iconView!!.visibility = View.VISIBLE
        iconView!!.setImageResource(iconRes)
        return this as T
    }

    fun setIconTintColor(iconTintColor: Int): T {
        iconView!!.setColorFilter(iconTintColor)
        return this as T
    }

    fun setTitleGravity(gravity: Int): T {
        titleView!!.gravity = gravity
        return this as T
    }

    fun setMessageGravity(gravity: Int): T {
        messageView!!.gravity = gravity
        return this as T
    }

    fun setTopColor(@ColorInt topColor: Int): T {
        findView<View>(R.id.ld_color_area).setBackgroundColor(topColor)
        return this as T
    }

    fun setTopViewIcon(@DrawableRes icon: Int, @ColorInt color: Int): T {
        if (layout == R.layout.dialog_top_bottom) {
            (findView<View>(R.id.topButton) as ImageView).setImageResource(icon)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                (findView<View>(R.id.topButton) as ImageView).imageTintList = ColorStateList.valueOf(color)
            }
        } else {
            throw IllegalStateException(string(R.string.ex_msg_dialog_top_not_set))
        }
        return this as T
    }

    fun setBottomViewIcon(@DrawableRes icon: Int, @ColorInt color: Int): T {
        if (layout == R.layout.dialog_top_bottom) {
            (findView<View>(R.id.bottomButton) as ImageView).setImageResource(icon)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                (findView<View>(R.id.bottomButton) as ImageView).imageTintList = ColorStateList.valueOf(color)
            }
        } else {
            throw IllegalStateException(string(R.string.ex_msg_dialog_top_not_set))
        }
        return this as T
    }


    fun setTopViewColor(@ColorInt topColor: Int): T {
        if (layout == R.layout.dialog_top_bottom) {
            findView<View>(R.id.expandableLayout).setBackgroundColor(topColor)
            findView<View>(R.id.dividerShape).setBackgroundColor(topColor)
        } else {
            throw IllegalStateException(string(R.string.ex_msg_dialog_top_not_set))
        }
        return this as T
    }

    fun setBottomViewColor(@ColorInt topColor: Int): T {
        if (layout == R.layout.dialog_top_bottom) {
            findView<View>(R.id.vSwitcher).setBackgroundColor(topColor)
            findView<View>(R.id.expandableLayoutTwo).setBackgroundColor(topColor)
        } else {
            throw IllegalStateException(string(R.string.ex_msg_dialog_bottom_not_set))
        }
        return this as T
    }

    fun setTopColorRes(@ColorRes topColoRes: Int): T {
        return setTopColor(color(topColoRes))
    }

    /*
     * You should call method saveInstanceState on handler object and then use saved info to restore
     * your dialog in onRestoreInstanceState. Static methods wasDialogOnScreen and getDialogId will
     * help you in this.
     */
    fun setInstanceStateHandler(id: Int, handler: AwesomeSaveStateHandler): T {
        handler.handleDialogStateSave(id, this)
        return this as T
    }

    fun setCancelable(cancelable: Boolean): T {
        dialog!!.setCancelable(cancelable)
        return this as T
    }

    fun setSavedInstanceState(savedInstanceState: Bundle?): T {
        if (savedInstanceState != null) {
            val hasSavedStateHere = savedInstanceState.keySet().contains(KEY_SAVED_STATE_TOKEN) && savedInstanceState.getSerializable(KEY_SAVED_STATE_TOKEN) === javaClass
            if (hasSavedStateHere) {
                restoreState(savedInstanceState)
            }
        }
        return this as T
    }

    open fun show(): Dialog {
        dialog!!.show()
        return dialog!!
    }

    fun create(): Dialog? {
        return dialog
    }

    fun dismiss() {
        dialog!!.dismiss()
    }

    internal open fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(KEY_SAVED_STATE_TOKEN, javaClass)
    }

    internal open fun restoreState(savedState: Bundle) {}

    protected fun string(@StringRes res: Int): String {
        return dialogView!!.context.getString(res)
    }

    protected fun color(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
    }

    protected fun <ViewClass : View> findView(id: Int): ViewClass {
        return dialogView!!.findViewById<View>(id) as ViewClass
    }

    protected class ClickListenerDecorator(private val clickListener: View.OnClickListener?, private val closeOnClick: Boolean, val dialog: Dialog) : View.OnClickListener {

        override fun onClick(v: View) {
            if (clickListener != null) {
                if (clickListener is AwesomeCompatDialog.DialogOnClickListenerAdapter) {
                    val listener = clickListener as AwesomeCompatDialog.DialogOnClickListenerAdapter?
                    listener!!.onClick(dialog, v.id)
                } else {
                    clickListener.onClick(v)
                }
            }
            if (closeOnClick) {
                dialog.dismiss()
            }
        }
    }

    companion object {

        private val KEY_SAVED_STATE_TOKEN = "key_saved_state_token"
    }
}
