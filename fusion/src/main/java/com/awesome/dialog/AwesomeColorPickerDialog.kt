package com.awesome.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.StringRes
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.awesome.R
import com.awesome.utils.AwesomeColorLine
import com.awesome.utils.OnColorChangedListener


/**
 * Created by Sandip Savaliya on 16.04.2016.
 */

class AwesomeColorPickerDialog : AwesomeBase<AwesomeColorPickerDialog> {

    private var color:Int = Color.WHITE
    private var colorPicker: AwesomeColorLine? = null
    private var confirmButton: TextView? = null

    override val layout: Int
        get() = R.layout.dialog_colorpicker

    constructor(context: Context) : super(context) {}

    constructor(context: Context, theme: Int) : super(context, theme) {}

    init {
        findView<View>(R.id.ld_color_area).minimumHeight = 350
        confirmButton = findView(R.id.ld_btn_confirm)
        colorPicker = findView(R.id.aw_color_line)
        colorPicker!!.setOnColorChangedListener(onColorChange())
        colorPicker!!.setSelectedColorPosition(0)
        this.setIcon(R.drawable.ic_colorpicker)
    }

    private inner class onColorChange : OnColorChangedListener {
        override fun onColorChanged(c: Int) {
            color = c
            findView<View>(R.id.ld_color_area).setBackgroundColor(c)
        }
    }

    fun setColorsArray(colors: IntArray): AwesomeColorPickerDialog {
        colorPicker!!.setColors(colors)
        return this
    }

    fun setConfirmButton(@StringRes text: Int, listener: OnColorConfirmListener): AwesomeColorPickerDialog {
        return setConfirmButton(string(text), listener)
    }

    fun setConfirmButton(text: String, listener: OnColorConfirmListener): AwesomeColorPickerDialog {
        confirmButton!!.text = text
        confirmButton!!.setOnClickListener(ColorPickedListener(listener))
        return this
    }

    fun setConfirmButtonColor(color: Int): AwesomeColorPickerDialog {
        confirmButton!!.setTextColor(color)
        return this
    }

    fun setInitialSelectedColorPosition(position : Int): AwesomeColorPickerDialog {
        colorPicker!!.setSelectedColorPosition(position)
        return this
    }

    fun setInitialColor(@ColorInt color: Int): AwesomeColorPickerDialog {
        colorPicker!!.setSelectedColor(color)
        return this
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SELECTED_COLOR, color.toString())
    }

    override fun restoreState(savedState: Bundle) {
        super.restoreState(savedState)
        findView<View>(R.id.ld_color_area).setBackgroundColor(savedState.getString(KEY_SELECTED_COLOR, ""+Color.WHITE).toInt())
    }

    private inner class ColorPickedListener(private val wrapped: OnColorConfirmListener?) : View.OnClickListener {

        override fun onClick(v: View) {
            wrapped?.onColorConfirmed(color)
            dismiss()
        }
    }


    interface OnColorConfirmListener {
        fun onColorConfirmed(color: Int)
    }

    companion object {
        private val KEY_SELECTED_COLOR = "key_selected_color"
    }
}
