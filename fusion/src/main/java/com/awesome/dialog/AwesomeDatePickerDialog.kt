package com.awesome.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.StringRes
import android.view.View
import android.widget.Button

import com.awesome.R
import com.awesome.dialog.datepicker.DatePickerListener
import com.awesome.dialog.datepicker.utils.SelectionType
import com.awesome.dialog.datepicker.view.CalendarView
import com.awesome.utils.ColorUtils

class AwesomeDatePickerDialog : AwesomeBase<AwesomeDatePickerDialog> {

    private var calendarView: CalendarView? = null
    private var confirmButton: Button? = null
    private var instanceStateManager: InstanceStateManager? = null

    override val layout: Int
        get() = R.layout.dialog_datepicker

    constructor(context: Context) : super(context)

    constructor(context: Context, theme: Int) : super(context, theme)

    init {
        confirmButton = findView(R.id.ld_btn_confirm)
        calendarView = findView(R.id.calendar_view)
        setIcon(R.drawable.ic_calendar)
        setTopColorRes(R.color.color_light_blue)
        setIconTintColor(Color.WHITE)
        setCalendarAccentColor(Color.parseColor("#03a9f4"))
    }

    fun setCalendarAccentColor(@ColorInt color:Int): AwesomeDatePickerDialog{
        setTopColor(color)
        setConfirmButtonColor(color)
        calendarView!!.selectedDayBackgroundColor = color
        calendarView!!.selectedDayBackgroundStartColor = ColorUtils.lighten(color, 0.2)
        calendarView!!.selectedDayBackgroundEndColor = ColorUtils.darken(color, 0.2)
        calendarView!!.otherDayTextColor = Color.parseColor("#dddddd")
        calendarView!!.weekendDayTextColor = ColorUtils.darken(color, 0.3)
        calendarView!!.selectedDates
        return this
    }

    fun setCalendarSelectionType(@SelectionType selectionType: Int): AwesomeDatePickerDialog{
        calendarView!!.selectionType = selectionType
        return this
    }

    fun setConfirmButton(@StringRes text: Int, listener: DatePickerListener): AwesomeDatePickerDialog {
        return setConfirmButton(string(text), listener)
    }

    fun setConfirmButton(text: String, listener: DatePickerListener): AwesomeDatePickerDialog {
        confirmButton!!.text = text
        confirmButton!!.setOnClickListener(DatePickedListener(listener))
        return this
    }

    fun setConfirmButtonColor(color: Int): AwesomeDatePickerDialog {
        confirmButton!!.setTextColor(color)
        return this
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

    private inner class DatePickedListener(private val wrapped: DatePickerListener?) : View.OnClickListener {

        override fun onClick(v: View) {
            wrapped?.onDatePicked(v, calendarView!!.selectedDates)
            dismiss()
        }
    }

    companion object {
        val POSITIVE_BUTTON = R.id.ld_btn_yes
        val NEGATIVE_BUTTON = R.id.ld_btn_no
    }
}
