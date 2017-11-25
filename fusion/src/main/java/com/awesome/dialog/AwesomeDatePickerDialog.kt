package com.awesome.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.view.View
import android.widget.Button

import com.awesome.R
import com.awesome.dialog.datepicker.utils.SelectionType
import com.awesome.dialog.datepicker.view.CalendarView
import com.awesome.utils.ColorUtils

class AwesomeDatePickerDialog : AwesomeBase<AwesomeDatePickerDialog> {

    private var calendarView: CalendarView? = null
    private var positiveButton: Button? = null
    private var negativeButton: Button? = null
    private var instanceStateManager: InstanceStateManager? = null

    override val layout: Int
        get() = R.layout.dialog_datepicker

    constructor(context: Context) : super(context) {}

    constructor(context: Context, theme: Int) : super(context, theme) {}

    init {
        positiveButton = findView(R.id.ld_btn_yes)
        negativeButton = findView(R.id.ld_btn_no)
        calendarView = findView(R.id.calendar_view)
        setIcon(R.drawable.ic_calendar)
        setTopColorRes(R.color.color_light_blue)
        setIconTintColor(Color.WHITE)
        setCalendarAccentColor(Color.parseColor("#03a9f4"))
    }

    fun setCalendarAccentColor(@ColorInt color:Int): AwesomeDatePickerDialog{
        setTopColor(color)
        setPositiveButtonColor(color)
        calendarView!!.selectedDayBackgroundColor = color
        calendarView!!.selectedDayBackgroundStartColor = ColorUtils.lighten(color, 0.2)
        calendarView!!.selectedDayBackgroundEndColor = ColorUtils.darken(color, 0.2)
        calendarView!!.otherDayTextColor = Color.parseColor("#dddddd")
        calendarView!!.weekendDayTextColor = ColorUtils.darken(color, 0.3)
        calendarView!!.selectedDates
        return this
    }

    fun setCalendarSelectionType(@SelectionType selectionType: Int): AwesomeDatePickerDialog{
        calendarView!!.setSelectionType(selectionType)
        return this
    }

    fun setPositiveButton(@StringRes text: Int, listener: View.OnClickListener): AwesomeDatePickerDialog {
        return setPositiveButton(string(text), listener)
    }

    fun setPositiveButton(text: String, listener: View.OnClickListener?): AwesomeDatePickerDialog {
        positiveButton!!.visibility = View.VISIBLE
        positiveButton!!.text = text
        positiveButton!!.setOnClickListener(AwesomeBase.ClickListenerDecorator(listener, true, dialog!!))
        return this
    }

    fun setNegativeButtonText(@StringRes text: Int): AwesomeDatePickerDialog {
        return setNegativeButton(string(text), null)
    }

    fun setNegativeButtonText(text: String): AwesomeDatePickerDialog {
        return setNegativeButton(text, null)
    }

    fun setNegativeButton(@StringRes text: Int, listener: View.OnClickListener): AwesomeDatePickerDialog {
        return setNegativeButton(string(text), listener)
    }

    fun setNegativeButton(text: String, listener: View.OnClickListener?): AwesomeDatePickerDialog {
        negativeButton!!.visibility = View.VISIBLE
        negativeButton!!.text = text
        negativeButton!!.setOnClickListener(AwesomeBase.ClickListenerDecorator(listener, true, dialog!!))
        return this
    }

    fun setButtonsColor(@ColorInt color: Int): AwesomeDatePickerDialog {
        positiveButton!!.setTextColor(color)
        negativeButton!!.setTextColor(color)
        return this
    }

    fun setButtonsColorRes(@ColorRes colorRes: Int): AwesomeDatePickerDialog {
        return setButtonsColor(color(colorRes))
    }

    fun setOnButtonClickListener(listener: View.OnClickListener): AwesomeDatePickerDialog {
        return setOnButtonClickListener(true, listener)
    }

    fun setOnButtonClickListener(closeOnClick: Boolean, listener: View.OnClickListener): AwesomeDatePickerDialog {
        val clickHandler = AwesomeBase.ClickListenerDecorator(listener, closeOnClick, dialog!!)
        positiveButton!!.setOnClickListener(clickHandler)
        negativeButton!!.setOnClickListener(clickHandler)
        return this
    }

    fun setPositiveButtonText(@StringRes text: Int): AwesomeDatePickerDialog {
        return setPositiveButton(string(text), null)
    }

    fun setPositiveButtonText(text: String): AwesomeDatePickerDialog {
        return setPositiveButton(text, null)
    }

    fun setPositiveButtonColor(@ColorInt color: Int): AwesomeDatePickerDialog {
        positiveButton!!.setTextColor(color)
        return this
    }

    fun setNegativeButtonColor(@ColorInt color: Int): AwesomeDatePickerDialog {
        negativeButton!!.setTextColor(color)
        return this
    }

    fun setPositiveButtonColorRes(@ColorRes colorRes: Int): AwesomeDatePickerDialog {
        return setPositiveButtonColor(color(colorRes))
    }

    fun setNegativeButtonColorRes(@ColorRes colorRes: Int): AwesomeDatePickerDialog {
        return setNegativeButtonColor(color(colorRes))
    }

    fun setInstanceStateManager(instanceStateManager: InstanceStateManager): AwesomeDatePickerDialog {
        this.instanceStateManager = instanceStateManager
        return this
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateManager!!.saveInstanceState(outState)
    }

    override fun restoreState(savedState: Bundle) {
        super.restoreState(savedState)
        instanceStateManager!!.restoreInstanceState(savedState)
    }

    interface InstanceStateManager {
        fun saveInstanceState(outState: Bundle)
        fun restoreInstanceState(savedState: Bundle)
    }


    companion object {
        val POSITIVE_BUTTON = R.id.ld_btn_yes
        val NEGATIVE_BUTTON = R.id.ld_btn_no
    }
}
