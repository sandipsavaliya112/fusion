package com.awesome.dialog

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.widget.Button


import com.awesome.R

import android.view.View.*

/**
 * Created by Sandip Savaliya on 16.04.2016.
 * If null is passed instead on click listener - dialog will be just closed on click.
 */
class AwesomeGeneralDialog : AwesomeBase<AwesomeGeneralDialog> {

    private var positiveButton: Button? = null
    private var negativeButton: Button? = null
    private var neutralButton: Button? = null

    override val layout: Int
        get() = R.layout.dialog_standard

    constructor(context: Context) : super(context)

    constructor(context: Context, theme: Int) : super(context, theme)

    init {
        positiveButton = findView(R.id.ld_btn_yes)
        negativeButton = findView(R.id.ld_btn_no)
        neutralButton = findView(R.id.ld_btn_neutral)
    }

    fun setPositiveButton(@StringRes text: Int, listener: OnClickListener): AwesomeGeneralDialog {
        return setPositiveButton(string(text), listener)
    }

    fun setPositiveButton(text: String, listener: OnClickListener?): AwesomeGeneralDialog {
        positiveButton!!.visibility = VISIBLE
        positiveButton!!.text = text
        positiveButton!!.setOnClickListener(AwesomeBase.ClickListenerDecorator(listener, true, dialog!!))
        return this
    }

    fun setNegativeButtonText(@StringRes text: Int): AwesomeGeneralDialog {
        return setNegativeButton(string(text), null)
    }

    fun setNegativeButtonText(text: String): AwesomeGeneralDialog {
        return setNegativeButton(text, null)
    }

    fun setNegativeButton(@StringRes text: Int, listener: OnClickListener): AwesomeGeneralDialog {
        return setNegativeButton(string(text), listener)
    }

    fun setNegativeButton(text: String, listener: OnClickListener?): AwesomeGeneralDialog {
        negativeButton!!.visibility = VISIBLE
        negativeButton!!.text = text
        negativeButton!!.setOnClickListener(AwesomeBase.ClickListenerDecorator(listener, true, dialog!!))
        return this
    }

    fun setNeutralButtonText(@StringRes text: Int): AwesomeGeneralDialog {
        return setNeutralButton(string(text), null)
    }

    fun setNeutralButtonText(text: String): AwesomeGeneralDialog {
        return setNeutralButton(text, null)
    }

    fun setNeutralButton(@StringRes text: Int, listener: OnClickListener?): AwesomeGeneralDialog {
        return setNeutralButton(string(text), listener)
    }

    fun setNeutralButton(text: String, listener: OnClickListener?): AwesomeGeneralDialog {
        neutralButton!!.visibility = VISIBLE
        neutralButton!!.text = text
        neutralButton!!.setOnClickListener(AwesomeBase.ClickListenerDecorator(listener, true, dialog!!))
        return this
    }

    fun setButtonsColor(@ColorInt color: Int): AwesomeGeneralDialog {
        positiveButton!!.setTextColor(color)
        negativeButton!!.setTextColor(color)
        neutralButton!!.setTextColor(color)
        return this
    }

    fun setButtonsColorRes(@ColorRes colorRes: Int): AwesomeGeneralDialog {
        return setButtonsColor(color(colorRes))
    }

    fun setOnButtonClickListener(listener: OnClickListener): AwesomeGeneralDialog {
        return setOnButtonClickListener(true, listener)
    }

    fun setOnButtonClickListener(closeOnClick: Boolean, listener: OnClickListener): AwesomeGeneralDialog {
        val clickHandler = AwesomeBase.ClickListenerDecorator(listener, closeOnClick, dialog!!)
        positiveButton!!.setOnClickListener(clickHandler)
        neutralButton!!.setOnClickListener(clickHandler)
        negativeButton!!.setOnClickListener(clickHandler)
        return this
    }

    fun setPositiveButtonText(@StringRes text: Int): AwesomeGeneralDialog {
        return setPositiveButton(string(text), null)
    }

    fun setPositiveButtonText(text: String): AwesomeGeneralDialog {
        return setPositiveButton(text, null)
    }

    fun setPositiveButtonColor(@ColorInt color: Int): AwesomeGeneralDialog {
        positiveButton!!.setTextColor(color)
        return this
    }

    fun setNegativeButtonColor(@ColorInt color: Int): AwesomeGeneralDialog {
        negativeButton!!.setTextColor(color)
        return this
    }

    fun setNeutralButtonColor(@ColorInt color: Int): AwesomeGeneralDialog {
        neutralButton!!.setTextColor(color)
        return this
    }

    fun setPositiveButtonColorRes(@ColorRes colorRes: Int): AwesomeGeneralDialog {
        return setPositiveButtonColor(color(colorRes))
    }

    fun setNegativeButtonColorRes(@ColorRes colorRes: Int): AwesomeGeneralDialog {
        return setNegativeButtonColor(color(colorRes))
    }

    fun setNeutralButtonColorRes(@ColorRes colorRes: Int): AwesomeGeneralDialog {
        return setNeutralButtonColor(color(colorRes))
    }

    companion object {

        val POSITIVE_BUTTON = R.id.ld_btn_yes
        val NEGATIVE_BUTTON = R.id.ld_btn_no
        val NEUTRAL_BUTTON = R.id.ld_btn_neutral
    }
}
